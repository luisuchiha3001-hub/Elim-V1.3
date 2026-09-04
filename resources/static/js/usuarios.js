/**
 * Alerta interactiva para inactivar o reactivar un usuario
 * @param {number} id - ID del usuario
 * @param {number} estadoActual - Estado del usuario (1 para Activo, 0 para Inactivo)
 */
function cambiarEstadoUsuario(id, estadoActual) {
    const accion = estadoActual === 1 ? 'inactivar' : 'activar';
    const mensaje = estadoActual === 1
        ? 'El usuario no podrá iniciar sesión en la plataforma hasta que sea reactivado.'
        : 'El usuario recuperará el acceso completo al sistema inmediatamente.';

    Swal.fire({
        title: `¿Deseas ${accion} este usuario?`,
        text: mensaje,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: estadoActual === 1 ? '#d9534f' : '#f3a1b7', // Rojo o Rosa según la acción
        cancelButtonColor: '#7d6671',
        confirmButtonText: `Sí, ${accion}`,
        cancelButtonText: 'Cancelar',
        background: '#ffffff',
        color: '#2b1f24'
    }).then((result) => {
        if (result.isConfirmed) {
            // Redirige al controlador encargado de procesar la inactivación
            window.location.href = `../../controllers/admin/InactivarUsuario.php?id=${id}&estado=${estadoActual === 1 ? 0 : 1}`;
        }
    });
}

// Lector de respuestas de estado mediante URL para lanzar alertas al retornar
const urlParams = new URLSearchParams(window.location.search);
if (urlParams.has('res')) {
    const respuesta = urlParams.get('res');
    if (respuesta === 'status_cambiado') {
        Swal.fire({
            title: '¡Operación Exitosa!',
            text: 'El estado del usuario ha sido modificado correctamente.',
            icon: 'success',
            confirmButtonColor: '#f3a1b7'
        });
    } else if (respuesta === 'error') {
        Swal.fire({
            title: 'Error del Servidor',
            text: 'No se pudo alterar el estado del usuario seleccionado.',
            icon: 'error',
            confirmButtonColor: '#f3a1b7'
        });
    }
}