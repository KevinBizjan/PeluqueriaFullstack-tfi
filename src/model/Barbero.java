package model;

public class Barbero extends Empleado {

    public Barbero(String id, String nombre) {
        super(id, nombre, "Barbero");
    }

    @Override
    public double calcularTarifa() {
        // Tarifa fija para barberos (podés ajustarla después)
        return 1500.0;
    }

    @Override
    public String toString() {
        return "Barbero{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                '}';
    }
}
