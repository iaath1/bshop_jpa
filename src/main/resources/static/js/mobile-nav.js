/* mobile-nav.js */
document.addEventListener('DOMContentLoaded', function () {
    'use strict';

    // ── Language dropdown (click-based) ─────────────────────────────
    document.querySelectorAll('.dropdown').forEach(function (dropdown) {
        var btn = dropdown.querySelector('.dropbtn');
        if (!btn) return;

        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            document.querySelectorAll('.dropdown.open').forEach(function (other) {
                if (other !== dropdown) other.classList.remove('open');
            });
            dropdown.classList.toggle('open');
        });
    });

    document.addEventListener('click', function () {
        document.querySelectorAll('.dropdown.open').forEach(function (d) {
            d.classList.remove('open');
        });
    });

    // ── Store: hamburger nav ─────────────────────────────────────────
    var navToggle = document.querySelector('.nav-toggle');
    var nav = document.querySelector('.header .nav');

    if (navToggle && nav) {
        navToggle.addEventListener('click', function (e) {
            e.stopPropagation();
            var isOpen = nav.classList.toggle('open');
            navToggle.classList.toggle('open', isOpen);
            navToggle.setAttribute('aria-expanded', String(isOpen));
        });

        document.addEventListener('click', function (e) {
            if (!nav.contains(e.target) && !navToggle.contains(e.target)) {
                nav.classList.remove('open');
                navToggle.classList.remove('open');
                navToggle.setAttribute('aria-expanded', 'false');
            }
        });

        nav.querySelectorAll('a').forEach(function (a) {
            a.addEventListener('click', function () {
                nav.classList.remove('open');
                navToggle.classList.remove('open');
            });
        });
    }

    // ── Admin: sidebar toggle ────────────────────────────────────────
    var sidebarToggle = document.querySelector('.sidebar-toggle');
    var sidebar = document.querySelector('.sidebar');

    // Create overlay if not present
    var overlay = document.querySelector('.sidebar-overlay');
    if (!overlay && sidebar) {
        overlay = document.createElement('div');
        overlay.className = 'sidebar-overlay';
        document.body.appendChild(overlay);
    }

    function openSidebar() {
        sidebar.classList.add('open');
        sidebarToggle.classList.add('open');
        if (overlay) overlay.classList.add('visible');
        document.body.style.overflow = 'hidden';
    }

    function closeSidebar() {
        sidebar.classList.remove('open');
        sidebarToggle.classList.remove('open');
        if (overlay) overlay.classList.remove('visible');
        document.body.style.overflow = '';
    }

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function (e) {
            e.stopPropagation();
            if (sidebar.classList.contains('open')) {
                closeSidebar();
            } else {
                openSidebar();
            }
        });

        if (overlay) {
            overlay.addEventListener('click', closeSidebar);
        }

        // Close on nav link click
        sidebar.querySelectorAll('a').forEach(function (a) {
            a.addEventListener('click', closeSidebar);
        });
    }
});

// ── Global Language Switcher ─────────────────────────────────────
window.changeLanguage = function(lang) {
    if (!lang) return;
    var url = new URL(window.location.href);
    url.searchParams.set('lang', lang);
    window.location.href = url.toString();
};