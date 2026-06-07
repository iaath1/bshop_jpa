/* size-picker.js — переиспользуемый пикер размеров */
(function () {
    'use strict';

    // Скрытая форма для добавления в корзину
    const cartForm = document.createElement('form');
    cartForm.method = 'POST';
    cartForm.style.display = 'none';

    const sizeInput = document.createElement('input');
    sizeInput.type = 'hidden';
    sizeInput.name = 'sizeId';
    cartForm.appendChild(sizeInput);
    document.body.appendChild(cartForm);

    // Открыть/закрыть пикер
    window.togglePicker = function (e, btn) {
        e.stopPropagation();
        const pid    = btn.dataset.pid;
        const picker = document.getElementById('picker-' + pid);
        if (!picker) return;

        const isOpen = picker.classList.contains('open');

        // закрыть все
        document.querySelectorAll('.size-picker.open')
                .forEach(p => p.classList.remove('open'));

        if (!isOpen) picker.classList.add('open');
    };

    // Добавить в корзину (авторизованный)
    window.addToCart = function (e, btn) {
        e.stopPropagation();
        cartForm.action = '/store/cart/add/' + btn.dataset.productId;
        sizeInput.value = btn.dataset.sizeId;
        cartForm.submit();
    };

    // Закрыть при клике вне пикера
    document.addEventListener('click', function (e) {
        if (!e.target.closest('.card-buy-wrap')) {
            document.querySelectorAll('.size-picker.open')
                    .forEach(p => p.classList.remove('open'));
        }
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            document.querySelectorAll('.size-picker.open')
                    .forEach(p => p.classList.remove('open'));
        }
    });
}());