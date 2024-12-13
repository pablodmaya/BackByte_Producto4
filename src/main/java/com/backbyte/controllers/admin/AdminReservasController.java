package com.backbyte.controllers.admin;

import com.backbyte.models.Alquiler;
import com.backbyte.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/reservas")
public class AdminReservasController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    // Listar reservas con filtrado
    @GetMapping
    public String listarReservas(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            Model model) {

        List<Alquiler> reservas;

        try {
            Date fechaInicioParsed = (fechaInicio != null && !fechaInicio.isEmpty()) ? Date.valueOf(fechaInicio) : null;
            Date fechaFinParsed = (fechaFin != null && !fechaFin.isEmpty()) ? Date.valueOf(fechaFin) : null;

            if (clienteId != null || fechaInicioParsed != null || fechaFinParsed != null) {
                reservas = alquilerRepository.findByCriteria(clienteId, fechaInicioParsed, fechaFinParsed);
            } else {
                reservas = alquilerRepository.findAll();
            }

            for (Alquiler alquiler : reservas) {
                long dias = (alquiler.getFecha_Fin().getTime() - alquiler.getFecha_Inicio().getTime()) / (1000 * 60 * 60 * 24);
                alquiler.setPrecioTotal(dias * alquiler.getVehiculo().getPrecioDia());
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar los filtros: " + e.getMessage());
            reservas = alquilerRepository.findAll();

            for (Alquiler alquiler : reservas) {
                long dias = (alquiler.getFecha_Fin().getTime() - alquiler.getFecha_Inicio().getTime()) / (1000 * 60 * 60 * 24);
                alquiler.setPrecioTotal(dias * alquiler.getVehiculo().getPrecioDia());
            }
        }

        model.addAttribute("reservas", reservas);
        return "admin/adminReservas";
    }

    // Editar una reserva (solo fechas)
    @PostMapping("/editar")
    public String editarReserva(@RequestParam Long id,
                                @RequestParam String fechaInicio,
                                @RequestParam String fechaFin) {

        try {
            Alquiler reservaExistente = alquilerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

            reservaExistente.setFecha_Inicio(Date.valueOf(fechaInicio));
            reservaExistente.setFecha_Fin(Date.valueOf(fechaFin));

            alquilerRepository.save(reservaExistente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la reserva: " + e.getMessage());
        }

        return "redirect:/admin/reservas";
    }

    // Eliminar una reserva
    @GetMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        try {
            alquilerRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la reserva: " + e.getMessage());
        }
        return "redirect:/admin/reservas";
    }
}
