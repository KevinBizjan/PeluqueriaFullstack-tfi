package app.menus;

import java.util.Scanner;
import java.util.UUID;
import model.Empleado;
import services.EmpleadoService;

public class MenuEmpleados {

    private final Scanner sc;
    private final EmpleadoService empleadoService;

    public MenuEmpleados(Scanner sc, EmpleadoService empleadoService) {
        this.sc = sc;
        this.empleadoService = empleadoService;
    }

    public void mostrar() {
        int op;
        do {
            System.out.println("\n--- Gestión de Empleados ---");
            System.out.println("1. Listar empleados");
            System.out.println("2. Agregar empleado");
            System.out.println("3. Buscar por ID");
            System.out.println("0. Volver");

            op = leerInt("Opción: ");

            switch (op) {
                case 1 -> listar();
                case 2 -> agregar();
                case 3 -> buscar();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }

    private void listar() {
        empleadoService.listarEmpleados().forEach(System.out::println);
    }

    private void agregar() {
        try {
            String id = "E" + UUID.randomUUID().toString().substring(0, 6);
            String nombre = leerString("Nombre: ");
            String tipo = leerString("Tipo (Barbero/Estilista): ");

            if (!esNombreValido(nombre)) {
                System.out.println("El nombre solo puede contener letras.");
                return;
            }

            if (nombre.isBlank()) {
                System.out.println("El nombre no puede estar vacío.");
                return;
            }

            if (!tipo.equalsIgnoreCase("barbero") && !tipo.equalsIgnoreCase("estilista")) {
                System.out.println("Tipo inválido. Debe ser Barbero o Estilista.");
                return;
            }

            Empleado e = empleadoService.crearEmpleado(id, nombre, tipo);

            System.out.println("Empleado creado: " + e);

        } catch (exceptions.EmpleadoNoEncontradoException ex) {
            System.out.println(" " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void buscar() {
        String id = leerString("ID: ");
        try {
            System.out.println(empleadoService.buscarPorId(id));
        } catch (exceptions.EmpleadoNoEncontradoException ex) {
            System.out.println(" " + ex.getMessage());
        }
    }

    private String leerString(String txt) {
        System.out.print(txt);
        return sc.nextLine().trim();
    }

    private int leerInt(String txt) {
        while (true) {
            System.out.print(txt);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido");
            }
        }
    }
    
    private boolean esNombreValido(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
}
