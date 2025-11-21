package model;

import util.Atendible;

public abstract class Empleado implements Atendible {

    private String id;
    private String nombre;
    private String especialidad;

    public Empleado(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return String.format(
            "Empleado: %-6s | %-12s | Especialidad: %s",
            id,
            nombre,
            especialidad
        );
    }

    
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    // Cada empleado define su tarifa base SIN servicio
    public abstract double getTarifaBase();

    @Override
    public double calcularTarifa(model.Servicio servicio) {
        return getTarifaBase() + servicio.getPrecioBase();
    }

}
