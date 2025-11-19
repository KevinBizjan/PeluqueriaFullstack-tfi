package model;

import util.Atendible;
import model.Servicio;

public abstract class Empleado implements util.Atendible {
    private String id;
    private String nombre;
    private String especialidad;

    public Empleado(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }

    // Método abstracto → cada tipo de empleado lo implementa diferente
    public abstract double calcularTarifa();
    
    @Override
    public double calcularTarifa(model.Servicio servicio) {
        return calcularTarifa();
    }
}
