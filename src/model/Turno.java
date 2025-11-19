package model;

import java.time.LocalDateTime;

public class Turno {
    public enum Estado {
        PENDIENTE,
        REALIZADO,
        CANCELADO
    }

    private String id;
    private Cliente cliente;
    private Empleado empleado;
    private Servicio servicio;
    private LocalDateTime fechaHora;
    private Estado estado;

    public Turno(String id, Cliente cliente, Empleado empleado, Servicio servicio, LocalDateTime fechaHora) {
        this.id = id;
        this.cliente = cliente;
        this.empleado = empleado;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = Estado.PENDIENTE;
    }

    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Empleado getEmpleado() { return empleado; }
    public Servicio getServicio() { return servicio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Estado getEstado() { return estado; }

    public void cancelar() { this.estado = Estado.CANCELADO; }
    public void realizar() { this.estado = Estado.REALIZADO; }

    @Override
    public String toString() {
        return "Turno{" +
                "id='" + id + ''' +
                ", cliente=" + cliente +
                ", empleado=" + empleado +
                ", servicio=" + servicio +
                ", fechaHora=" + fechaHora +
                ", estado=" + estado +
                '}';
    }
}