package model;

public class Estilista extends Empleado {

    public Estilista(String id, String nombre) {
        super(id, nombre, "Estilista");
    }

    @Override
    public double getTarifaBase() {
        return 2000.0;
    }
}
