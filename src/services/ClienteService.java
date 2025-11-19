package services;

import exceptions.ElementoNoEncontradoException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import model.Cliente;

public class ClienteService {

    // HashMap para búsquedas rápidas por DNI
    private final Map<String, Cliente> clientesPorDni = new HashMap<>();
    private final AtomicInteger contadorId = new AtomicInteger(1); // para generar id tipo C1, C2...

    // Agregar cliente y verifica duplicado por DNI
    public Cliente agregarCliente(Cliente cliente) {
        if (cliente.getDni() == null || cliente.getDni().isBlank()) {
            throw new IllegalArgumentException("DNI no puede ser vacío");
        }
        if (clientesPorDni.containsKey(cliente.getDni())) {
            throw new IllegalArgumentException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        String id = cliente.getId() != null ? cliente.getId() : "C" + contadorId.getAndIncrement();
        Cliente nuevo = new Cliente(id, cliente.getNombre(), cliente.getApellido(), cliente.getDni(), cliente.getTelefono());
        clientesPorDni.put(nuevo.getDni(), nuevo);
        return nuevo;
    }

    // Listar todos (ordenados por apellido, nombre)
    public List<Cliente> listarClientes() {
        return clientesPorDni.values()
                .stream()
                .sorted(Comparator.comparing(Cliente::getApellido, Comparator.nullsLast(String::compareTo))
                                  .thenComparing(Cliente::getNombre, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }


    public Cliente buscarPorDni(String dni) {
        Cliente c = clientesPorDni.get(dni);
        if (c == null) throw new ElementoNoEncontradoException("Cliente con DNI " + dni + " no encontrado");
        return c;
    }


    public List<Cliente> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return Collections.emptyList();
        String q = nombre.trim().toLowerCase();
        return clientesPorDni.values().stream()
                .filter(c -> (c.getNombre() + " " + c.getApellido()).toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // Modificar por DNI
    public Cliente modificarCliente(String dni, String nuevoNombre, String nuevoApellido, String nuevoTelefono) {
        Cliente existente = clientesPorDni.get(dni);
        if (existente == null) throw new ElementoNoEncontradoException("Cliente con DNI " + dni + " no encontrado");
        existente.setNombre(nuevoNombre != null ? nuevoNombre : existente.getNombre());
        existente.setApellido(nuevoApellido != null ? nuevoApellido : existente.getApellido());
        existente.setTelefono(nuevoTelefono != null ? nuevoTelefono : existente.getTelefono());
        return existente;
    }

  
    public boolean eliminarPorDni(String dni) {
        return clientesPorDni.remove(dni) != null;
    }

    // Utilitarios
    public boolean existeDni(String dni) {
        return clientesPorDni.containsKey(dni);
    }

    public int cantidadClientes() {
        return clientesPorDni.size();
    }
}
