/**
 * Servicio principal de gestión de turnos.
 * Encargado de registrar turnos, validar disponibilidad,
 * cancelar y finalizar turnos.
 *
 * <p>También permite generar reportes por día o empleado.</p>
 *
 * @author Thomas
 */
package services;

import exceptions.TurnoNoDisponibleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Cliente;
import model.Empleado;
import model.Servicio;
import model.Turno;

public class TurnoService {

    private final List<Turno> turnos = new ArrayList<>();

    public void registrarTurno(String id,
                               Cliente cliente,
                               Empleado empleado,
                               Servicio servicio,
                               LocalDateTime fechaHora) {
        Turno t = new Turno(id, cliente, empleado, servicio, fechaHora);
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del turno no puede ser anterior a hoy.");
        }
        if (!estaDisponible(empleado.getId(), fechaHora)) {
            throw new TurnoNoDisponibleException(
                    "El empleado " + empleado.getNombre() + " no está disponible en ese horario."
            );
        }
        turnos.add(t);
    }

    public boolean cancelarTurno(String id) {
        for (Turno t : turnos) {
            if (t.getId().equals(id)) {
                t.cancelar();
                return true;
            }
        }
        return false;
    }

    public boolean finalizarTurno(String id) {
        for (Turno t : turnos) {
            if (t.getId().equals(id)) {
                t.realizar();
                return true;
            }
        }
        return false;
    }

    public List<Turno> listarTurnos() {
        return turnos;
    }

    public List<Turno> listarPorEmpleado(String empleadoId) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getEmpleado().getId().equals(empleadoId)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Turno> listarPorFecha(LocalDate fecha) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getFechaHora().toLocalDate().equals(fecha)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // Ingresos para una fecha
    public double calcularIngresos(LocalDate fecha) {
        double total = 0;
        for (Turno t : turnos) {
            if (t.getFechaHora().toLocalDate().equals(fecha)
                    && t.getEstado() == Turno.Estado.REALIZADO) {
                total += t.getServicio().getPrecioBase();
            }
        }
        return total;
    }

    // Ingresos del día
    public double calcularIngresosDiarios() {
        LocalDate hoy = LocalDate.now();
        return calcularIngresos(hoy);
    }

    //Seccion reportes
    // Verificar disponibilidad del empleado
    public boolean estaDisponible(String empleadoId, LocalDateTime fechaHora) {
        for (Turno t : turnos) {
            if (t.getEmpleado().getId().equals(empleadoId)
                    && t.getFechaHora().equals(fechaHora)
                    && t.getEstado() == Turno.Estado.PENDIENTE) {
                return false;
            }
        }
        return true;
    }

    //Ingresos por empleado en una fecha
    public Map<String, Double> ingresosPorEmpleado(LocalDate fecha) {
        Map<String, Double> mapa = new HashMap<>();

        for (Turno t : turnos) {
            if (t.getEstado() == Turno.Estado.REALIZADO
                    && t.getFechaHora().toLocalDate().equals(fecha)) {

                String key = t.getEmpleado().getId() + " - " + t.getEmpleado().getNombre();
                double importe = t.getServicio().getPrecioBase();

                mapa.merge(key, importe, Double::sum);
            }
        }
        return mapa;
    }

    //Ingresos por servicio en una fecha
    public Map<String, Double> ingresosPorServicio(LocalDate fecha) {
        Map<String, Double> mapa = new HashMap<>();

        for (Turno t : turnos) {
            if (t.getEstado() == Turno.Estado.REALIZADO
                    && t.getFechaHora().toLocalDate().equals(fecha)) {

                String key = t.getServicio().getNombre();
                double importe = t.getServicio().getPrecioBase();

                mapa.merge(key, importe, Double::sum);
            }
        }
        return mapa;
    }

    // Minutos trabajados por empleado en una fecha
    public Map<String, Integer> minutosTrabajadosPorEmpleado(LocalDate fecha) {
        Map<String, Integer> mapa = new HashMap<>();

        for (Turno t : turnos) {
            if (t.getEstado() == Turno.Estado.REALIZADO
                    && t.getFechaHora().toLocalDate().equals(fecha)) {

                String key = t.getEmpleado().getId() + " - " + t.getEmpleado().getNombre();
                int minutos = t.getServicio().getDuracionMinutos();

                mapa.merge(key, minutos, Integer::sum);
            }
        }
        return mapa;
    }

    // Ranking de servicios más vendidos en una fecha
    public Map<String, Long> rankingServicios(LocalDate fecha) {
        Map<String, Long> mapa = new HashMap<>();

        for (Turno t : turnos) {
            if (t.getEstado() == Turno.Estado.REALIZADO
                    && t.getFechaHora().toLocalDate().equals(fecha)) {

                String key = t.getServicio().getNombre();
                mapa.merge(key, 1L, Long::sum);
            }
        }
        return mapa;
    }

    public Turno buscarPorId(String id) {
        for (Turno t : turnos) {
            if (t.getId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }
}
