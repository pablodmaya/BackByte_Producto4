document.addEventListener('DOMContentLoaded', () => {
    const editReservationModal = document.getElementById('editReservationModal');

    if (editReservationModal) {
        editReservationModal.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget; // Botón que activó el modal

            if (button) {
                const id = button.getAttribute('data-id');
                const fechaInicio = button.getAttribute('data-fechainicio');
                const fechaFin = button.getAttribute('data-fechafin');

                document.getElementById('editReservaId').value = id;
                document.getElementById('editFechaInicio').value = fechaInicio;
                document.getElementById('editFechaFin').value = fechaFin;
            }
        });
    }
});
