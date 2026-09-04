document.addEventListener('DOMContentLoaded', function() {
    // lógica para el menú del catálogo
    const catalogButton = document.querySelector('.catalog-menu .btn-nav');
    const submenu = document.querySelector('.submenu');

    if (catalogButton && submenu) {
        catalogButton.addEventListener('click', function(e) {
            e.preventDefault(); // Evita comportamientos nativos inesperados del botón
            e.stopPropagation(); // Evita que el clic se propague al documento
            submenu.classList.toggle('active'); // Muestra u oculta agregando la clase
        });
    }

    // cerrar el menú automáticamente al hacer clic en cualquiera de las opciones (Bebé, Niños...)
    const submenuLinks = document.querySelectorAll('.submenu a');
    submenuLinks.forEach(link => {
        link.addEventListener('click', function() {
            submenu.classList.remove('active');
        });
    });

    // cerrar el menú si haces un clic en cualquier otra parte fuera de la barra de catálogo
    document.addEventListener('click', function(e) {
        if (submenu && !e.target.closest('.catalog-menu')) {
            submenu.classList.remove('active');
        }
    });
});