package com.backbyte.controllers;

import com.backbyte.models.*;
import com.backbyte.repository.AlquilerRepository;
import com.backbyte.repository.ClienteRepository;
import com.backbyte.repository.UsuarioRepository;
import com.backbyte.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Controller

public class ReservasVehiculosController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/buscar-vehiculos")
    public String buscarVehiculos(@RequestParam("vehicleType") String vehicleType,
                                  @RequestParam("pickupLocation") String pickupLocation,
                                  @RequestParam("startDate") String startDate,
                                  @RequestParam("endDate") String endDate,
                                  Model model) {

        // Mapea el tipo de vehículo a un valor del enum TipoVehiculo
        TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(vehicleType);

        // Mapea las ciudades a valores del enum Ciudad
        Ciudad ciudadRecogida = Ciudad.valueOf(pickupLocation);

        // Convierte las fechas recibidas en LocalDate
        LocalDate fechaInicio = LocalDate.parse(startDate);
        LocalDate fechaFin = LocalDate.parse(endDate);

        // Convertir las fechas LocalDate a java.sql.Date
        java.sql.Date sqlFechaInicio = java.sql.Date.valueOf(fechaInicio);
        java.sql.Date sqlFechaFin = java.sql.Date.valueOf(fechaFin);

        // Llama al repositorio para obtener los vehículos disponibles
        List<Vehiculo> vehiculosDisponibles = vehiculoRepository.findAvailableVehicles(tipoVehiculo, ciudadRecogida, sqlFechaInicio, sqlFechaFin);

        // Agrega los resultados al modelo
        model.addAttribute("vehiculos", vehiculosDisponibles);
        model.addAttribute("startDate", startDate); // Añadir fecha inicio al modelo
        model.addAttribute("endDate", endDate);

        // En el futuro, puedes usar estos parámetros para filtrar datos
        return "/user/buscarVehiculos";
    }

    @GetMapping("/user/reservar/{idVehiculo}")
    public String mostrarFormularioReserva(@PathVariable("idVehiculo") Integer idVehiculo,
                                           @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate,
                                           Model model) {

// Obtener el nombre de usuario del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre de usuario del usuario autenticado



        // Verificar si el usuario ya es cliente
        Usuario usuario = UsuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener el ID del usuario autenticado
        Integer idUsuario = usuario.getId_Usuario();

        // Comprobar si el usuario es cliente (es_cliente = 1)
        if (!usuario.getEs_Cliente()) {

            model.addAttribute("idVehiculo", idVehiculo);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            // Si el usuario no es cliente, redirigimos al formulario de cliente
            return "/user/formularioReserva"; // Ruta del formulario para registrar al cliente
        }

        // Si el usuario es cliente, buscar el cliente asociado a ese usuario
        Cliente clienteExistente = clienteRepository.findByUsuarioId(Long.valueOf(idUsuario));
        if (clienteExistente == null) {
            throw new RuntimeException("Cliente no encontrado para el usuario");
        }

        // Si el usuario es cliente, agregar los datos de la reserva al modelo
        model.addAttribute("idVehiculo", idVehiculo);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("cliente", clienteExistente);
        Vehiculo vehiculo = vehiculoRepository.findById(Long.valueOf(idVehiculo)).orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        LocalDate fechaInicio = LocalDate.parse(startDate);
        LocalDate fechaFin = LocalDate.parse(endDate);
        java.sql.Date sqlFechaInicio = java.sql.Date.valueOf(fechaInicio);
        java.sql.Date sqlFechaFin = java.sql.Date.valueOf(fechaFin);

        Alquiler alquiler = new Alquiler();
        alquiler.setCliente(clienteExistente);
        alquiler.setVehiculo(vehiculo);
        alquiler.setFecha_Inicio(sqlFechaInicio);
        alquiler.setFecha_Fin(sqlFechaFin);

        // Guardar la reserva (alquiler)
        alquilerRepository.save(alquiler);


        return "redirect:/user/reservaConfirmada?idCliente=" + clienteExistente.getId_Cliente();
    }

    @PostMapping("/user/registrarClienteReserva")
    public String registrarClienteYAlquiler(@ModelAttribute Cliente cliente, @RequestParam Long idVehiculo, @RequestParam String startDate,
                                            @RequestParam String endDate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre de usuario del usuario autenticado



        // Verificar si el usuario ya es cliente
        Usuario usuario = UsuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener el ID del usuario autenticado
        Integer idUsuario = usuario.getId_Usuario();
        // Verificar si el cliente ya existe
        Cliente clienteExistente = clienteRepository.findBydni(cliente.getDni());


        if (clienteExistente == null) {


            usuario.setEs_Cliente(true);
            UsuarioRepository.save(usuario);

            cliente.setUsuario(usuario);
            // Si el cliente no existe, guardarlo en la base de datos
            clienteExistente = clienteRepository.save(cliente);
        }

        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        LocalDate fechaInicio = LocalDate.parse(startDate);
        LocalDate fechaFin = LocalDate.parse(endDate);
        java.sql.Date sqlFechaInicio = java.sql.Date.valueOf(fechaInicio);
        java.sql.Date sqlFechaFin = java.sql.Date.valueOf(fechaFin);

        Alquiler alquiler = new Alquiler();
        alquiler.setCliente(clienteExistente);
        alquiler.setVehiculo(vehiculo);
        alquiler.setFecha_Inicio(sqlFechaInicio);
        alquiler.setFecha_Fin(sqlFechaFin);

        // Guardar la reserva (alquiler)
        alquilerRepository.save(alquiler);

        // Redirigir a la página de confirmación
        return "redirect:/user/reservaConfirmada?idCliente=" + clienteExistente.getId_Cliente();
    }

    @GetMapping("/user/reservaConfirmada")
    public String mostrarReservaConfirmada(@RequestParam Long idCliente, Model model) {
        // Puedes agregar datos al modelo si es necesario, por ejemplo:
        model.addAttribute("mensaje", "Tu reserva ha sido confirmada exitosamente.");
        model.addAttribute("idCliente", idCliente);

        // Retorna la vista reservaConfirmada.html
        return "user/reservaConfirmada";
    }

    @GetMapping("/user/mis-reservas")
    public ResponseEntity<?> mostrarReservas(@RequestParam(required = false) String success) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Nombre del usuario autenticado

        // Buscar usuario por nombre
        Usuario usuario = UsuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Obtener ID de cliente
        Integer idUsuario = usuario.getId_Usuario();
        Cliente cliente = clienteRepository.findByUsuarioId(Long.valueOf(idUsuario));

        // Si el cliente no existe, lo tratamos igual que un cliente no registrado (pero no lanzamos 403)
        boolean esCliente = cliente != null && usuario.getEs_Cliente();

        // Obtener precios totales (aunque esté vacío si no es cliente)
        List<Double> preciosTotales = esCliente ? alquilerRepository.calcularPrecioTotalPorReserva(Long.valueOf(cliente.getId_Cliente())) : new ArrayList<>();

        // Buscar alquileres asociados al cliente (si es cliente, sino, retornamos lista vacía)
        List<Alquiler> alquileres = esCliente ? alquilerRepository.findByClienteId(cliente.getId_Cliente().longValue()) : new ArrayList<>();

        // Construir respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        response.put("cliente", cliente);
        response.put("alquileres", alquileres);
        response.put("preciosTotales", preciosTotales);

        // Si el usuario no es cliente, incluir un mensaje indicando que no hay reservas
        if (!esCliente) {
            response.put("mensajeNoCliente", "Este usuario no tiene reservas ya que no es cliente.");
        }

        // Mensaje de éxito si existe
        if ("true".equals(success)) {
            response.put("mensajeExito", "Reserva eliminada exitosamente.");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/eliminarReserva/{id}")
    public String eliminarReserva(@PathVariable Long id ) {

        // Buscar el alquiler por su ID
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Eliminar el alquiler
        alquilerRepository.delete(alquiler);


        return "redirect:/user/mis-reservas" + "?success=true";
    }


    @GetMapping("/user/mi-perfil")
    public String mostrarPerfil(Model model, String success) {

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre de usuario del usuario autenticado

        // Buscar al usuario en el repositorio
        Usuario usuario = UsuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener el ID del usuario autenticado
        Integer idUsuario = usuario.getId_Usuario();

        // Si es cliente, buscar información adicional
        Cliente cliente = clienteRepository.findByUsuarioId(Long.valueOf(idUsuario));

        // Agregar datos del usuario y del cliente (si aplica) al modelo
        model.addAttribute("usuario", usuario);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
        }
        if ("true".equals(success)) {
            model.addAttribute("mensajeExito", "Se han actualizado tus datos!");
        }

        // Retornar la vista del perfil
        return "user/miPerfil";
    }


    @PostMapping("/user/mi-perfil")
    public String actualizarPerfil(
            @ModelAttribute Usuario usuarioActualizado,
            @ModelAttribute Cliente clienteActualizado,
            Model model) {

        // Actualizar datos de usuario
        Usuario usuarioExistente = UsuarioRepository.findById(usuarioActualizado.getId_Usuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        UsuarioRepository.save(usuarioExistente);

        // Actualizar datos de cliente
        if (clienteActualizado != null) {
            Cliente clienteExistente = clienteRepository.findById(clienteActualizado.getId_Cliente().longValue())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            clienteExistente.setNombre(clienteActualizado.getNombre());
            clienteExistente.setApellido(clienteActualizado.getApellido());
            clienteExistente.setDni(clienteActualizado.getDni());
            clienteExistente.setDireccion(clienteActualizado.getDireccion());
            clienteExistente.setTelefono(clienteActualizado.getTelefono());
            clienteRepository.save(clienteExistente);
        }


        // Redirigir con un mensaje de éxito
        model.addAttribute("mensajeExito", "Datos actualizados correctamente.");
        return "redirect:/user/mi-perfil" + "?success=true";
    }

    @GetMapping("/user/cambiar-password")
    public String mostrarFormularioCambioContrasena(Model model, String error, String error2, String error3, String success) {

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre de usuario del usuario autenticado

        // Buscar al usuario en el repositorio
        Usuario usuario = UsuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);

        if ("true".equals(error)) {
            model.addAttribute("mensajeError", "No has proporcionado la contraseña correcta ");
        }
        if ("true".equals(error2)) {
            model.addAttribute("mensajeError2", "Las contraseñas no son iguales");
        }
        if ("true".equals(error3)) {
            model.addAttribute("mensajeError3", "La contraseña nueva no puede ser igual a la actual");
        }
        if ("true".equals(success)) {
            model.addAttribute("mensajeExito", "Contraseña cambiada y registrada correctamente!");
        }
        return "user/cambiarPassword";
    }

    @PostMapping("/user/cambiar-password")
    public String cambiarContrasena(@RequestParam("currentPassword") String currentPassword,
                                    @RequestParam("newPassword") String newPassword,
                                    @RequestParam("confirmPassword") String confirmPassword,
                                    @ModelAttribute Usuario usuarioActualizado,
                                    Model model) {



        Usuario usuarioExistente = UsuarioRepository.findById(usuarioActualizado.getId_Usuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        // Verifica que la contraseña actual coincida
        if (!passwordEncoder.matches(currentPassword, usuarioExistente.getPasswordEncriptada())) {

            return "redirect:/user/cambiar-password" + "?error=true";
        }

        // Verifica que la nueva contraseña y la confirmación coincidan
        if (!newPassword.equals(confirmPassword)) {

            return "redirect:/user/cambiar-password" + "?error2=true";
        }

        // Verifica que la nueva contraseña sea diferente de la actual
        if (passwordEncoder.matches(newPassword, usuarioExistente.getPassword())) {

            return "redirect:/user/cambiar-password" + "?error3=true";
        }

        // Codifica la nueva contraseña
        String nuevaContrasenaCodificada = passwordEncoder.encode(newPassword);

        // Actualiza la contraseña del usuario en la base de datos
        usuarioExistente.setPassword(newPassword);
        usuarioExistente.setPasswordEncriptada(nuevaContrasenaCodificada);
        UsuarioRepository.save(usuarioExistente);

        // Agrega un mensaje de éxito


        return "redirect:/user/cambiar-password" + "?success=true";
    }


    @RestController
    @RequestMapping("/api/token/") // Ruta base para la API
    public class UsuarioController {

        private final UsuarioRepository usuarioRepository;
        private final ClienteRepository clienteRepository;

        public UsuarioController(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
            this.usuarioRepository = usuarioRepository;
            this.clienteRepository = clienteRepository;
        }

        @GetMapping("/mis-reservas")
        public ResponseEntity<?> mostrarReservas(@RequestParam(required = false) String success) {
            // Obtener usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Nombre del usuario autenticado

            // Buscar usuario por nombre
            Usuario usuario = UsuarioRepository.findByNombreUsuario(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

            // Obtener ID de cliente
            Integer idUsuario = usuario.getId_Usuario();
            Cliente cliente = clienteRepository.findByUsuarioId(Long.valueOf(idUsuario));

            // Si el cliente no existe, lo tratamos igual que un cliente no registrado (pero no lanzamos 403)
            boolean esCliente = cliente != null && usuario.getEs_Cliente();

            // Obtener precios totales (aunque esté vacío si no es cliente)
            List<Double> preciosTotales = esCliente ? alquilerRepository.calcularPrecioTotalPorReserva(Long.valueOf(cliente.getId_Cliente())) : new ArrayList<>();

            // Buscar alquileres asociados al cliente (si es cliente, sino, retornamos lista vacía)
            List<Alquiler> alquileres = esCliente ? alquilerRepository.findByClienteId(cliente.getId_Cliente().longValue()) : new ArrayList<>();

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", usuario);
            response.put("cliente", cliente);
            response.put("alquileres", alquileres);
            response.put("preciosTotales", preciosTotales);

            // Si el usuario no es cliente, incluir un mensaje indicando que no hay reservas
            if (!esCliente) {
                response.put("mensajeNoCliente", "Este usuario no tiene reservas ya que no es cliente.");
            }

            // Mensaje de éxito si existe
            if ("true".equals(success)) {
                response.put("mensajeExito", "Reserva eliminada exitosamente.");
            }

            return ResponseEntity.ok(response);
        }

        @DeleteMapping("/reservas/{id}")
        public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
            try {
                // Buscar el alquiler por su ID
                Alquiler alquiler = alquilerRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

                // Eliminar el alquiler
                alquilerRepository.delete(alquiler);

                // Retornar una respuesta de éxito
                return ResponseEntity.ok().body(Map.of("message", "Reserva eliminada exitosamente"));
            } catch (ResponseStatusException ex) {
                // Lanza el error directamente para casos de not found
                throw ex;
            } catch (Exception ex) {
                // Retornar un error genérico en caso de otros problemas
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error al eliminar la reserva", "details", ex.getMessage()));
            }
        }

        @GetMapping("/mi-perfil")
        public ResponseEntity<Map<String, Object>> mostrarPerfil(@RequestParam(required = false) String success) {

            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Nombre de usuario del usuario autenticado

            // Buscar al usuario en el repositorio
            Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Obtener el ID del usuario autenticado
            Integer idUsuario = usuario.getId_Usuario();

            // Si es cliente, buscar información adicional
            Cliente cliente = clienteRepository.findByUsuarioId(Long.valueOf(idUsuario));

            // Construir la respuesta como un Map para incluir múltiples datos
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", usuario);
            if (cliente != null) {
                response.put("cliente", cliente);
            }
            if ("true".equals(success)) {
                response.put("mensajeExito", "Se han actualizado tus datos!");
            }

            // Retornar la respuesta con código 200
            return ResponseEntity.ok(response);
        }
    }



}

