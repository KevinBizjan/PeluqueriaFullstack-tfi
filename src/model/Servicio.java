package model;

import java.util.Objects;

public class Servicio {
    private final String id;          
    private String nombre;
    private double precioBase;       
    private int duracionMinutos;

    public Servicio(String id, String nombre, double precioBase, int duracionMinutos) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.duracionMinutos = duracionMinutos;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getDuracionMinutos() { return duracionMinutos; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    @Override
    public String toString() {
        return String.format(
            "Servicio: %-8s | %-20s | $%-8.2f | %d min",
            (id == null ? "-" : id),
            nombre,
            precioBase,
            duracionMinutos
        );
    }


    // igualdad por id del servicio
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio)) return false;
        Servicio servicio = (Servicio) o;
        return Objects.equals(id, servicio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
