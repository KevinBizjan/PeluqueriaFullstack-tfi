package model;

public class Barbero extends Empleado {

    public Barbero(String id, String nombre) {
        super(id, nombre, "Barbero");
    }

    @Override
    public double getTarifaBase() {
        return 1500.0;
    }
}
