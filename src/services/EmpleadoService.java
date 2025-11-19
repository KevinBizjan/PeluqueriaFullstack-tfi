package services;

import exceptions.ElementoNoEncontradoException;
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
                .orElse(null);
    }

    public Empleado crearEmpleado(String id, String nombre, String tipo) {
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

        if (e == null)
            throw new ElementoNoEncontradoException("Empleado no encontrado");

        if (nuevoNombre != null && !nuevoNombre.isBlank()) e.setNombre(nuevoNombre);
        if (nuevaEspecialidad != null && !nuevaEspecialidad.isBlank()) e.setEspecialidad(nuevaEspecialidad);
    }

    public boolean eliminar(String id) {
        return empleados.removeIf(e -> e.getId().equalsIgnoreCase(id));
    }
}
