package model;

public class Estilista extends Empleado {

    public Estilista(String id, String nombre) {
        super(id, nombre, "Estilista");
    }

    @Override
    public double calcularTarifa() {
        // Tarifa fija para estilistas (podés cambiarla si quieren)
        return 2000.0;
    }

    @Override
    public String toString() {
        return "Estilista{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                '}';
    }
}
