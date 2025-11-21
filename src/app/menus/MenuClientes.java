package app.menus;

import exceptions.ElementoNoEncontradoException;
import model.Cliente;
import services.ClienteService;

import java.util.List;
import java.util.Scanner;

public class MenuClientes {

    private final Scanner sc;
    private final ClienteService clienteService;

    public MenuClientes(Scanner sc, ClienteService clienteService) {
        this.sc = sc;
        this.clienteService = clienteService;
    }

    public void mostrar() {
        int op;
        do {
            System.out.println("\n--- Gestión de Clientes ---");
            System.out.println("1. Agregar cliente");
            System.out.println("2. Buscar cliente por DNI");
            System.out.println("3. Buscar clientes por nombre");
            System.out.println("4. Modificar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Listar todos los clientes");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");

            try {
                switch (op) {
                    case 1 -> agregar();
                    case 2 -> buscarPorDni();
                    case 3 -> buscarPorNombre();
                    case 4 -> modificar();
                    case 5 -> eliminar();
                    case 6 -> listar();
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

        } while (op != 0);
    }

    private void agregar() {
        String nombre = leerString("Nombre: ");
        String apellido = leerString("Apellido: ");
        String dni = leerString("DNI: ");
        String telefono = leerString("Teléfono: ");

        Cliente c = new Cliente(null, nombre, apellido, dni, telefono);
        clienteService.agregarCliente(c);

        System.out.println("Cliente agregado: " + c);
    }

    private void buscarPorDni() {
        String dni = leerString("DNI: ");
        System.out.println(clienteService.buscarPorDni(dni));
    }

    private void buscarPorNombre() {
        String q = leerString("Nombre o parte del nombre: ");
        List<Cliente> lista = clienteService.buscarPorNombre(q);

        if (lista.isEmpty()) System.out.println("No se encontraron clientes.");
        else lista.forEach(System.out::println);
    }

    private void modificar() {
        String dni = leerString("DNI del cliente a modificar: ");
        String nuevoNombre = leerString("Nuevo nombre (enter = mantener): ");
        String nuevoApellido = leerString("Nuevo apellido (enter = mantener): ");
        String nuevoTel = leerString("Nuevo teléfono (enter = mantener): ");

        clienteService.modificarCliente(
                dni,
                nuevoNombre.isBlank() ? null : nuevoNombre,
                nuevoApellido.isBlank() ? null : nuevoApellido,
                nuevoTel.isBlank() ? null : nuevoTel
        );

        System.out.println("Cliente modificado.");
    }

    private void eliminar() {
        String dni = leerString("DNI del cliente a eliminar: ");
        boolean ok = clienteService.eliminarPorDni(dni);
        System.out.println(ok ? "Cliente eliminado." : "No se encontró cliente.");
    }

    private void listar() {
        clienteService.listarClientes().forEach(System.out::println);
    }

    private String leerString(String txt) {
        System.out.print(txt);
        return sc.nextLine().trim();
    }

    private int leerInt(String txt) {
        while (true) {
            System.out.print(txt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }
}
