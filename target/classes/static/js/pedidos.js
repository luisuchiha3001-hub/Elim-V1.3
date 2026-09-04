document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.form-pedido').forEach((form) => {
        form.addEventListener('submit', () => {
            const button = form.querySelector('button[type="submit"]');
            if (button) {
                button.textContent = 'Guardando...';
                button.disabled = true;
            }
        });
    });
});
