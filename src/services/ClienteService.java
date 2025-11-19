package services;

import model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private List<Cliente> clientes = new ArrayList<>();

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public Cliente buscarPorDNI(String dni) {
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni)) return c;
        }
        return null;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}