// Confirmación elegante con SweetAlert2 para eliminar prendas
function confirmarBorrado(id) {
    Swal.fire({
        title: '¿Eliminar esta prenda?',
        text: 'Esta acción removerá la pijama de manera permanente de la tienda y del inventario.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#f3a1b7',
        cancelButtonColor: '#7d6671',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Conservar',
        background: '#ffffff',
        color: '#2b1f24',
        customClass: {
            popup: 'border-radius: var(--radius);'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '../../controlles/admin/eliminar_producto.php?id=' + id;
        }
    });
}

// Alertas de éxito o error tras operaciones del dashboard
const params = new URLSearchParams(window.location.search);
if (params.has('res')) {
    const res = params.get('res');
    if (res === 'eliminado') {
        Swal.fire({ title: '¡Eliminado!', text: 'La pijama ha sido retirada de la base de datos.', icon: 'success', confirmButtonColor: '#f3a1b7' });
    } else if (res === 'agregado') {
        Swal.fire({ title: '¡Registrada!', text: 'Nueva prenda añadida con éxito al inventario.', icon: 'success', confirmButtonColor: '#f3a1b7' });
    } else if (res === 'error') {
        Swal.fire({ title: 'Error', text: 'No se pudo procesar la solicitud interna.', icon: 'error', confirmButtonColor: '#f3a1b7' });
    }
}