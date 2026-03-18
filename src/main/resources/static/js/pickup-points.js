(() => {
'use strict';

const POINTS_PER_PAGE = 100;
const MAX_POINTS = 1000;

const pointsCache = new Map();

let selectedPoint = null;
let allPoints = [];
let filteredPoints = [];
let currentPage = 1;

let currentCity = '';
let currentStreet = '';
let currentNumber = '';
let currentService = null;

let searchTimeout = null;

document.addEventListener('DOMContentLoaded', () => {
    initializeEventListeners();
});

function initializeEventListeners() {

    const cityInput = document.getElementById('city-search');
    const streetInput = document.getElementById('street-search');
    const numberInput = document.getElementById('number-search');
    const selectBtn = document.getElementById('select-btn');

    function autoSearch() {

        clearTimeout(searchTimeout);

        searchTimeout = setTimeout(() => {
            handleCitySearch();
        }, 500);
    }

    cityInput?.addEventListener('input', autoSearch);
    streetInput?.addEventListener('input', autoSearch);
    numberInput?.addEventListener('input', autoSearch);

    selectBtn.addEventListener('click', () => {

    if (!selectedPoint) return;

    const form =
        document.getElementById('lockerForm') ||
        document.getElementById('lockerFormAnon');

    if (!form) return;

    const isAuthenticated = form.id === 'lockerForm';

    if (isAuthenticated) {

        document.getElementById('lockerId').value = selectedPoint.id;
        document.getElementById('lockerName').value = selectedPoint.name;
        document.getElementById('lockerAddress').value = selectedPoint.address;

    } else {

        document.getElementById('lockerIdAnon').value = selectedPoint.id;
        document.getElementById('lockerNameAnon').value = selectedPoint.name;
        document.getElementById('lockerAddressAnon').value = selectedPoint.address;
    }

    form.submit();
});
}

function containsCyrillic(text) {
    return /[а-яёіїє]/i.test(text);
}

async function handleCitySearch() {

    const city = document.getElementById('city-search')?.value.trim();
    const street = document.getElementById('street-search')?.value.trim() || '';
    const number = document.getElementById('number-search')?.value.trim() || '';

    if (!city) {
        showError('Введіть місто');
        return;
    }

    currentCity = city;
    currentStreet = street;
    currentNumber = number;

    const service = containsCyrillic(city) ? 'novapost' : 'inpost';
    currentService = service;

    const cacheKey = `${service}-${city}-${street}-${number}`;

    if (pointsCache.has(cacheKey)) {

        allPoints = pointsCache.get(cacheKey);
        filteredPoints = allPoints;
        currentPage = 1;

        displayPoints();
        updateServiceIndicator(service, city, allPoints.length);

        return;
    }

    showLoading('Завантаження відділень...');

    try {

        let points = [];

        if (service === 'inpost') {
            points = await getInPostPoints(city, street, number);
        }

        if (service === 'novapost') {
            points = await getNovaPostPoints(city, street, number);
        }

        if (!points.length) {
            showError('Нічого не знайдено');
            return;
        }

        pointsCache.set(cacheKey, points);

        allPoints = points;
        filteredPoints = points;
        currentPage = 1;

        displayPoints();
        updateServiceIndicator(service, city, points.length);

    } catch (error) {

        console.error(error);
        showError('Помилка пошуку');
    }
}

async function getInPostPoints(city, street = '', number = '') {

    try {

        const url =
            `https://api-pl-points.easypack24.net/v1/points?city=${encodeURIComponent(city)}&per_page=${MAX_POINTS}`;

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error('InPost API error');
        }

        const data = await response.json();

        let points = data.items.map(item => ({

            id: item.name,
            name: `Paczkomat ${item.name}`,
            number: item.name,
            address: item.address?.line1 || '',
            details: item.location_description || '24/7',
            service: 'inpost'

        }));

        // Фильтр по улице (локально)
        if (street) {

            const streetLower = street.toLowerCase();

            points = points.filter(p =>
                p.address.toLowerCase().includes(streetLower)
            );
        }

        // Фильтр по номеру пачкомата
        if (number) {

            const numberLower = number.toLowerCase();

            points = points.filter(p =>
                p.number.toLowerCase().includes(numberLower)
            );
        }

        return points.slice(0, MAX_POINTS);

    } catch (error) {

        console.error(error);
        return [];
    }
}

async function getNovaPostPoints(city, street = '', number = '') {

    try {

        const response = await fetch('/api/novapost/warehouses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                city: city,
                search: street || number || ''
            })
        });

        if (!response.ok) {
            return [];
        }

        const result = await response.json();

        if (!result.success || !result.data) {
            return [];
        }

        let warehouses = result.data.slice(0, MAX_POINTS).map(item => ({

            id: item.Ref,
            name: item.Description,
            number: extractNumber(item.Description),
            address: item.ShortAddress || item.Description,
            details: item.TypeOfWarehouse === '9' ? 'Поштомат' : 'Відділення',
            service: 'novapost'

        }));

        if (street) {

            const streetLower = street.toLowerCase();

            warehouses = warehouses.filter(w =>
                w.address.toLowerCase().includes(streetLower)
            );
        }

        if (number) {

            const numberLower = number.toLowerCase();

            warehouses = warehouses.filter(w =>
                w.number.toLowerCase().includes(numberLower) ||
                w.name.toLowerCase().includes(numberLower)
            );
        }

        return warehouses;

    } catch (error) {

        console.error(error);
        return [];
    }
}

function extractNumber(name) {

    if (!name) return '';

    const match = name.match(/\d+/);

    return match ? match[0] : '';
}

function displayPoints() {

    const container = document.getElementById('points-list');

    if (!filteredPoints.length) {

        container.innerHTML = '<div>Немає відділень</div>';
        return;
    }

    const start = (currentPage - 1) * POINTS_PER_PAGE;
    const end = Math.min(start + POINTS_PER_PAGE, filteredPoints.length);

    const pointsToShow = filteredPoints.slice(start, end);

    container.innerHTML = pointsToShow.map(point => {

        let extraInfo = [];

        if (point.number) extraInfo.push(`№${point.number}`);
        if (point.details) extraInfo.push(point.details);

        return `
        <div class="pickup-point"
             onclick='selectPoint(${JSON.stringify(point).replace(/'/g,"&#39;")})'>
             
            <strong>${escapeHtml(point.name)}</strong><br>
            ${escapeHtml(point.address)}<br>

            <small>
            ${extraInfo.join(' | ')} | 
            ${point.service === 'novapost' ? 'Нова Пошта' : 'InPost'}
            </small>
        </div>
        `;

    }).join('');

    if (filteredPoints.length > POINTS_PER_PAGE) {
        addPagination();
    }
}

function addPagination() {

    const totalPages = Math.ceil(filteredPoints.length / POINTS_PER_PAGE);

    let html = '<div class="pagination">';

    for (let i = 1; i <= totalPages; i++) {

        html += `
        <button onclick="changePage(${i})"
            ${i === currentPage ? 'class="active"' : ''}>
            ${i}
        </button>`;
    }

    html += '</div>';

    document.getElementById('points-list').innerHTML += html;
}

window.selectPoint = function(point) {

    selectedPoint = point;

    const btn = document.getElementById('select-btn');

    btn.disabled = false;
    btn.innerHTML = `✅ ${escapeHtml(point.name)}`;
};

window.changePage = function(page) {

    currentPage = page;
    displayPoints();
};

function showLoading(msg) {

    document.getElementById('points-list').innerHTML =
        `<div>${msg}</div>`;
}

function showError(msg) {

    document.getElementById('points-list').innerHTML =
        `<div>❌ ${msg}</div>`;
}

function updateServiceIndicator(service, city, count) {

    const indicator = document.getElementById('service-indicator');

    indicator.style.display = 'block';

    let text = `${service.toUpperCase()} | ${escapeHtml(city)}`;

    if (currentStreet) {
        text += `, ${escapeHtml(currentStreet)}`;
    }

    if (currentNumber) {
        text += `, №${escapeHtml(currentNumber)}`;
    }

    text += ` | ${count} відділень`;

    indicator.innerHTML = text;
}

function escapeHtml(text) {

    if (!text) return '';

    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };

    return String(text).replace(/[&<>"']/g, m => map[m]);
}

})();