/**
 * Servicio encargado de gestionar clientes.
 * Permite agregar, buscar, modificar y eliminar clientes.
 *
 * <p>Incluye validaciones de DNI duplicado.</p>
 *
 * @author Thomas
 */

package services;

import exceptions.ClienteDuplicadoException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteService {

    private final List<Cliente> clientes = new ArrayList<>();
    private final String[] historial = new String[10];
    private int indiceHistorial = 0;

    private void agregarHistorial(String mensaje) {
        historial[indiceHistorial % historial.length] = mensaje;
        indiceHistorial++;
    }

    public void mostrarHistorial() {
        System.out.println("\n--- Historial de acciones (ClienteService) ---");
        for (String h : historial) {
            if (h != null) System.out.println("- " + h);
        }
    }

    public void agregarCliente(Cliente c) {
        // Validar duplicado por DNI
        boolean existe = clientes.stream()
                .anyMatch(x -> x.getDni().equals(c.getDni()));
        if (existe) {
            throw new ClienteDuplicadoException(
                    "Ya existe un cliente con DNI: " + c.getDni()
            );
        }
        if (!c.getDni().matches("\\d+")) {
            throw new IllegalArgumentException("El DNI debe ser numérico.");
        }
        if (!c.getTelefono().matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono debe ser numérico.");
        }
        if (c.getNombre().isBlank() || c.getApellido().isBlank()) {
            throw new IllegalArgumentException("El nombre y apellido son obligatorios.");
        }
        clientes.add(c);
        agregarHistorial("Cliente agregado: DNI " + c.getDni());
    }

    public Cliente buscarPorDni(String dni) {
        return clientes.stream()
                .filter(c -> c.getDni().equals(dni))
                .findFirst()
                .orElseThrow(() ->
                        new ClienteDuplicadoException("Cliente no encontrado"));
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public void modificarCliente(String dni, String nuevoNombre,
                                String nuevoApellido, String nuevoTelefono) {

        Cliente c = buscarPorDni(dni);

        if (nuevoNombre != null) c.setNombre(nuevoNombre);
        if (nuevoApellido != null) c.setApellido(nuevoApellido);
        if (nuevoTelefono != null) c.setTelefono(nuevoTelefono);
        agregarHistorial("Cliente modificado: DNI " + dni);
    }

    public boolean eliminarPorDni(String dni) {
        agregarHistorial("Cliente eliminado: DNI " + dni);
        return clientes.removeIf(c -> c.getDni().equals(dni));
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

}

