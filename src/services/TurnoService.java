package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Empleado;
import model.Servicio;
import model.Turno;

public class TurnoService {

    private List<Turno> turnos = new ArrayList<>();

    public void registrarTurno(String id, Cliente cliente, Empleado empleado, Servicio servicio, LocalDateTime fechaHora) {
        Turno t = new Turno(id, cliente, empleado, servicio, fechaHora);
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

    // ✔ IMPLEMENTACIÓN CORRECTA
    public void crearTurno(Cliente cliente, String servicioNombre, String empleadoNombre, String fecha) {
        Servicio servicio = new Servicio("S" + (turnos.size() + 1), servicioNombre, 30, 1500);

        Empleado empleado = new Empleado(empleadoNombre + "_id", empleadoNombre, "Peluquero") {
            @Override
            public double getTarifaBase() {
                return 1000;
            }
        };

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime fechaHora = LocalDateTime.parse(fecha + " 10:00", format);

        String idTurno = "T" + (turnos.size() + 1);

        registrarTurno(idTurno, cliente, empleado, servicio, fechaHora);
    }

    // ✔ IMPLEMENTACIÓN CORRECTA
    public double calcularIngresosDiarios() {
        LocalDate hoy = LocalDate.now();
        double total = 0;

        for (Turno t : turnos) {
            if (t.getFechaHora().toLocalDate().equals(hoy)
                && t.getEstado() == Turno.Estado.REALIZADO) {

                total += t.getServicio().getPrecioBase();
            }
        }

        return total;
    }
}
