package app.menus;

import java.util.List;
import java.util.Scanner;
import model.Cliente;
import services.ClienteService;

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
            System.out.println("7. Mostrar historial de acciones");
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
                    case 7 -> clienteService.mostrarHistorial();
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (exceptions.ClienteDuplicadoException ex) {
                System.out.println(" " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println(" " + ex.getMessage());
            }
        } while (op != 0);
    }

    private void agregar() {
        String nombre = leerString("Nombre: ");
         if (!esNombreValido(nombre)) {
            System.out.println("El nombre solo puede contener letras.");
            return;
        }
        if (nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");

        String apellido = leerString("Apellido: ");
        if (!esNombreValido(apellido)) {
            System.out.println("El apellido solo puede contener letras.");
            return;
        }
        if (apellido.isBlank())
            throw new IllegalArgumentException("El apellido no puede estar vacío.");

        String dni = leerString("DNI: ");
        if (!dni.matches("\\d+"))
            throw new IllegalArgumentException("El DNI debe ser numérico.");

        String telefono = leerString("Teléfono: ");
        if (!telefono.matches("\\d+"))
            throw new IllegalArgumentException("El teléfono debe ser numérico.");

        Cliente c = new Cliente(null, nombre, apellido, dni, telefono);
        clienteService.agregarCliente(c);

        System.out.println("Cliente agregado: " + c);
    }

    private void buscarPorDni() {
        String dni = leerString("DNI: ");
        if (!dni.matches("\\d+")) {
            System.out.println("El DNI debe contener solo números.");
            return;
        }
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

    private boolean esNombreValido(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
}
}
