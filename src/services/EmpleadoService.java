package services;

import exceptions.ElementoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import model.Barbero;
import model.Empleado;
import model.Estilista;

public class EmpleadoService {

    private List<Empleado> empleados = new ArrayList<>();

    public void agregarEmpleado(Empleado e) {
        empleados.add(e);
    }

    public List<Empleado> listar() {
        return empleados;
    }

    public List<Empleado> listarEmpleados() {
        return empleados;
    }

    public Empleado buscarPorId(String id) {
        return empleados.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean eliminar(String id) {
        return empleados.removeIf(e -> e.getId().equals(id));
    }

    public void modificar(String id, String nuevoNombre, String nuevaEspecialidad) {
        Empleado emp = buscarPorId(id);
        if (emp == null)
            throw new ElementoNoEncontradoException("Empleado no encontrado");

        emp.setNombre(nuevoNombre);
        emp.setEspecialidad(nuevaEspecialidad);
    }

    public Empleado crearEmpleado(String id, String nombre, String tipo) {

        if (tipo.equalsIgnoreCase("Barbero")) {
            Barbero b = new Barbero(id, nombre);
            empleados.add(b);
            return b;
        }

        if (tipo.equalsIgnoreCase("Estilista")) {
            Estilista e = new Estilista(id, nombre);
            empleados.add(e);
            return e;
        }

        throw new IllegalArgumentException("Tipo de empleado inválido: " + tipo);
    }

    public Empleado crearBarbero(String id, String nombre) {
        Barbero b = new Barbero(id, nombre);
        empleados.add(b);
        return b;
    }

    public Empleado crearEstilista(String id, String nombre) {
        Estilista e = new Estilista(id, nombre);
        empleados.add(e);
        return e;
    }
}
