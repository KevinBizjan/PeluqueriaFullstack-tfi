/**
 * Servicio de administración de empleados.
 * Permite crear empleados, buscarlos y modificarlos.
 *
 * <p>Incluye validación de especialidades.</p>
 *
 * @author Thomas
 */

package services;

import exceptions.EmpleadoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import model.Barbero;
import model.Empleado;
import model.Estilista;

public class EmpleadoService {

    private final List<Empleado> empleados = new ArrayList<>();

    public List<Empleado> listarEmpleados() {
        return empleados;
    }

    public Empleado buscarPorId(String id) {
        return empleados.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() ->
                new EmpleadoNoEncontradoException("No existe empleado con ID: " + id));
    }

    public Empleado crearEmpleado(String id, String nombre, String tipo) {
        Empleado nuevo;
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del empleado no puede estar vacío.");
        }
        if (!tipo.equalsIgnoreCase("barbero") && 
            !tipo.equalsIgnoreCase("estilista")) {
            throw new IllegalArgumentException("Tipo de empleado inválido. Use Barbero o Estilista.");
        }

        switch (tipo.toLowerCase()) {
            case "barbero" -> nuevo = new Barbero(id, nombre);
            case "estilista" -> nuevo = new Estilista(id, nombre);
            default -> throw new IllegalArgumentException("Tipo de empleado inválido");
        }

        empleados.add(nuevo);
        return nuevo;
    }

    public void modificar(String id, String nuevoNombre, String nuevaEspecialidad) {
        Empleado e = buscarPorId(id);
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            e.setNombre(nuevoNombre);
        }
        if (nuevaEspecialidad != null && !nuevaEspecialidad.isBlank()) {
            e.setEspecialidad(nuevaEspecialidad);
        }
    }

    public boolean eliminar(String id) {
        buscarPorId(id);
        return empleados.removeIf(e -> e.getId().equalsIgnoreCase(id));
    }
}