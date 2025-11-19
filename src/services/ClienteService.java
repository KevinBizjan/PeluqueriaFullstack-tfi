package services;

import model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private List<Cliente> clientes = new ArrayList<>();

    // Registrar cliente
    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    // Buscar por nombre
    public Cliente buscarPorNombre(String nombre) {
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    // Listar todos
    public List<Cliente> listarClientes() {
        return clientes;
    }
}
