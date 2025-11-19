package model;

public class Servicio {

    private String id;
    private String nombre;
    private double precioBase;
    private int duracionMinutos;

    public Servicio(String id, String nombre, double precioBase, int duracionMinutos) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.duracionMinutos = duracionMinutos;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precioBase=" + precioBase +
                ", duracionMinutos=" + duracionMinutos +
                '}';
    }
}
