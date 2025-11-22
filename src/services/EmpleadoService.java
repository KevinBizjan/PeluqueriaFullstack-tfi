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
    private int contadorEmpleados = 1;
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
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (!tipo.equalsIgnoreCase("barbero") &&
            !tipo.equalsIgnoreCase("estilista")) {
            throw new IllegalArgumentException("Tipo de empleado inválido. Use Barbero o Estilista.");
        }
        if (id != null && id.startsWith("E")) {
            try {
                int nro = Integer.parseInt(id.substring(1));
                if (nro >= contadorEmpleados) contadorEmpleados = nro + 1;
            } catch (Exception ignore) {}
        } else {
            id = generarIdEmpleado();
        }
        Empleado nuevo;
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

    public String generarIdEmpleado() {
        return "E" + (contadorEmpleados++);
    }
}