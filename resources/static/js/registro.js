document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formRegistro");
    const documento = document.getElementById("documento");
    const telefono = document.getElementById("telefono");

    // 1. Restricción en vivo: Evitar que escriban letras en campos de número
    documento.addEventListener("input", function() {
        this.value = this.value.replace(/\D/g, "");
    });

    telefono.addEventListener("input", function() {
        this.value = this.value.replace(/\D/g, "");
    });

    // 2. Validaciones estrictas antes del submit
    form.addEventListener("submit", function (e) {
        const correo = document.getElementById("correo").value.trim().toLowerCase();
        const telValue = telefono.value.trim();
        const pass = document.getElementById("password").value;
        const confPass = document.getElementById("confirm_password").value;

        // Validación de Correo electrónico (@gmail.com, @hotmail.com, @outlook.com)
        const dominiosValidos = ["@gmail.com", "@hotmail.com", "@outlook.com"];
        const correoCorrecto = dominiosValidos.some(dominio => correo.endsWith(dominio));

        if (!correoCorrecto) {
            e.preventDefault();
            alert("El correo electrónico debe pertenecer a uno de los siguientes dominios: @gmail.com, @hotmail.com o @outlook.com");
            return;
        }

        // Validación de Teléfono (Exactamente 10 dígitos y empezar con 3)
        if (telValue.length !== 10 || !telValue.startsWith("3")) {
            e.preventDefault();
            alert("El número de teléfono debe tener exactamente 10 dígitos numéricos y comenzar con el número 3.");
            return;
        }

        // Validación de Contraseñas
        if (pass !== confPass) {
            e.preventDefault();
            alert("Las contraseñas ingresadas no coinciden.");
            return;
        }
    });
});

// Función nativa para alternar visibilidad de claves
function togglePass(inputId, btn) {
    const input = document.getElementById(inputId);
    const icon = btn.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}