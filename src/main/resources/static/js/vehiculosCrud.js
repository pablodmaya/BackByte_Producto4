document.addEventListener('DOMContentLoaded', function () {
  var editVehicleModal = document.getElementById('editVehicleModal');

  if (editVehicleModal) {
    editVehicleModal.addEventListener('show.bs.modal', function (event) {
      var button = event.relatedTarget;

      if (!button) {
        console.error("Error: botón que disparó el evento no encontrado.");
        return;
      }

      var editVehicleId = document.getElementById('editVehicleId');
      var editMarca = document.getElementById('editMarca');
      var editModelo = document.getElementById('editModelo');
      var editMatricula = document.getElementById('editMatricula');
      var editPrecioDia = document.getElementById('editPrecioDia');
      var editLocalizacion = document.getElementById('editLocalizacion');
      var editTipoVehiculo = document.getElementById('editTipoVehiculo');
      var editPlazas = document.getElementById('editPlazas');
      var editPuertas = document.getElementById('editPuertas');
      var editColor = document.getElementById('editColor');
      var editCc = document.getElementById('editCc');

      // Set values to the modal fields
      editVehicleId.value = button.getAttribute('data-id');
      editMarca.value = button.getAttribute('data-marca');
      editModelo.value = button.getAttribute('data-modelo');
      editMatricula.value = button.getAttribute('data-matricula');
      editPrecioDia.value = button.getAttribute('data-precio');
      editLocalizacion.value = button.getAttribute('data-localizacion');
      editTipoVehiculo.value = button.getAttribute('data-tipo');

      // Mostrar los campos adicionales según el tipo de vehículo
      actualizarCamposTipoVehiculoEditar();

      if (editTipoVehiculo.value.toUpperCase() === "COCHE") {
        editPlazas.value = button.getAttribute('data-plazas');
        editPuertas.value = button.getAttribute('data-puertas');
        editColor.value = button.getAttribute('data-color');
      } else if (editTipoVehiculo.value.toUpperCase() === "MOTO") {
        editCc.value = button.getAttribute('data-cc');
      }

      // Set the form action dynamically
      var editVehicleForm = document.getElementById('editVehicleForm');
      editVehicleForm.action = '/vehiculos/editar/' + editVehicleId.value;
    });

    // Mostrar u ocultar campos según el tipo de vehículo seleccionado en la edición
    var editTipoVehiculoSelect = document.getElementById("editTipoVehiculo");
    if (editTipoVehiculoSelect) {
      editTipoVehiculoSelect.addEventListener('change', actualizarCamposTipoVehiculoEditar);
    }
  } else {
    console.error("Error: modal de edición no encontrado");
  }

  // Función para mostrar u ocultar campos en el modal de edición
  function actualizarCamposTipoVehiculoEditar() {
    var tipo = document.getElementById("editTipoVehiculo").value;
    var camposCoche = document.getElementById("editCamposCoche");
    var camposMoto = document.getElementById("editCamposMoto");

    if (tipo.toUpperCase() === "COCHE") {
      camposCoche.style.display = "block";
      camposMoto.style.display = "none";
    } else if (tipo.toUpperCase() === "MOTO") {
      camposCoche.style.display = "none";
      camposMoto.style.display = "block";
    } else {
      camposCoche.style.display = "none";
      camposMoto.style.display = "none";
    }
  }

  // Función para mostrar u ocultar campos en el modal de "Añadir Vehículo"
  function actualizarCamposTipoVehiculo() {
    var tipo = document.getElementById("tipoVehiculo").value;
    var camposCoche = document.getElementById("camposCoche");
    var camposMoto = document.getElementById("camposMoto");

    if (tipo.toUpperCase() === "COCHE") {
      camposCoche.style.display = "block";
      camposMoto.style.display = "none";
    } else if (tipo.toUpperCase() === "MOTO") {
      camposCoche.style.display = "none";
      camposMoto.style.display = "block";
    } else {
      camposCoche.style.display = "none";
      camposMoto.style.display = "none";
    }
  }

  // Asigna la función `actualizarCamposTipoVehiculo` al evento de cambio del campo tipo en el modal de "Añadir Vehículo"
  document.getElementById("tipoVehiculo").addEventListener('change', actualizarCamposTipoVehiculo);
});
