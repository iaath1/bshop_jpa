(() => {
    'use strict';

    let map = null;
    let markers = [];
    let selectedPoint = null;
    let selectedService = 'inpost';

    document.addEventListener('DOMContentLoaded', () => {
        const selectBtn = document.getElementById('select-btn');
        const cityInput = document.getElementById('city-search');

        selectBtn.addEventListener('click', () => {
            if (selectedPoint) {
                document.getElementById('lockerForm').submit();
            }
        });

        cityInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                loadPointsForSelectedService();
            }
        });

        cityInput.addEventListener('change', loadPointsForSelectedService);

        document.querySelectorAll('.service-btn').forEach(btn => {
            btn.addEventListener('click', function () {
                document.querySelectorAll('.service-btn')
                    .forEach(b => b.classList.remove('active'));

                this.classList.add('active');
                selectedService = this.dataset.service;
                loadPointsForSelectedService();
            });
        });
    });

    window.initMap = function () {
        const warsaw = { lat: 52.2297, lng: 21.0122 };

        map = new google.maps.Map(document.getElementById("map"), {
            zoom: 10,
            center: warsaw,
            styles: [{ featureType: "poi", stylers: [{ visibility: "off" }] }]
        });

        loadPointsForSelectedService();
    };

    async function loadPointsForSelectedService() {
        const city = document.getElementById('city-search').value.trim();
        if (!city) {
            showError('Wpisz miasto');
            return;
        }

        showLoading();

        try {
            let points = [];

            switch (selectedService) {
                case 'inpost':
                    points = await getInPostPoints(city);
                    break;
                case 'novapost':
                    points = await getNovaPostPoints(city);
                    break;
                default:
                    points = [];
            }

            displayPoints(points);
            updateMap(points);

        } catch (error) {
            console.error(error);
            showError('Błąd ładowania punktów');
        }
    }

    async function getInPostPoints(city) {
        const url = `https://api-pl-points.easypack24.net/v1/points?city=${encodeURIComponent(city)}`;

        const response = await fetch(url);
        if (!response.ok) throw new Error('InPost API error');

        const data = await response.json();

        return data.items.map(item => ({
            id: item.name,
            name: `Paczkomat ${item.name}`,
            address: `${item.address.line1 || ''} ${item.address.line2 || ''}`.trim(),
            lat: parseFloat(item.location.latitude),
            lng: parseFloat(item.location.longitude),
            hours: item.opening_hours?.[0]?.description || '24/7',
            phone: item.phone_number || null,
            service: 'inpost'
        }));
    }

    async function getNovaPostPoints(city) {
        const response = await fetch(`/api/novapost/warehouses?city=${encodeURIComponent(city)}`, {
            method: "POST"
        });

        if (!response.ok) throw new Error('NovaPost API error');

        const result = await response.json();
        if (!result.success) return [];

        return result.data
            .filter(item => item.Latitude && item.Longitude)
            .map(item => ({
                id: item.Ref,
                name: item.Description,
                address: item.ShortAddress,
                lat: parseFloat(item.Latitude),
                lng: parseFloat(item.Longitude),
                hours: formatWorkSchedule(item.WorkSchedule),
                phone: item.Phone,
                service: 'novapost'
            }));
    }

    function updateMap(points) {
        markers.forEach(marker => marker.setMap(null));
        markers = [];

        if (!points.length) return;

        const bounds = new google.maps.LatLngBounds();

        points.forEach(point => {
            const marker = new google.maps.Marker({
                position: { lat: point.lat, lng: point.lng },
                map,
                title: point.name
            });

            marker.addListener('click', () => selectPoint(point));
            markers.push(marker);
            bounds.extend(marker.getPosition());
        });

        map.fitBounds(bounds);
    }

    function displayPoints(points) {
        const container = document.getElementById('points-list');
        container.innerHTML = '';

        if (!points.length) {
            container.innerHTML = '<div class="no-points">Nie znaleziono punktów</div>';
            return;
        }

        points.forEach(point => {
            const div = document.createElement('div');
            div.className = 'pickup-point';
            div.innerHTML = `
                <div class="point-name">${escapeHtml(point.name)}</div>
                <div class="point-address">${escapeHtml(point.address)}</div>
                <div class="point-hours">Godziny: ${escapeHtml(point.hours)}</div>
            `;

            div.addEventListener('click', () => selectPoint(point));
            container.appendChild(div);
        });
    }

    function selectPoint(point) {
        selectedPoint = point;
        updateSelectButton();

        document.getElementById('lockerId').value = point.id;
        document.getElementById('lockerName').value = point.name;
        document.getElementById('lockerAddress').value = point.address;

        map.setCenter({ lat: point.lat, lng: point.lng });
        map.setZoom(16);
    }

    function updateSelectButton() {
        const btn = document.getElementById('select-btn');
        btn.disabled = !selectedPoint;
    }

    function showLoading() {
        document.getElementById('points-list')
            .innerHTML = '<div class="loading">Ładowanie...</div>';
    }

    function showError(msg) {
        document.getElementById('points-list')
            .innerHTML = `<div class="error">${escapeHtml(msg)}</div>`;
    }

    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function formatWorkSchedule(schedule) {
        if (!schedule) return "Brak danych";
        if (typeof schedule === 'string') return schedule;
        if (Array.isArray(schedule)) {
            return schedule.map(s => `${s.Days}: ${s.Hours}`).join(', ');
        }
        return "Pon-Pt 9:00-18:00";
    }

})();