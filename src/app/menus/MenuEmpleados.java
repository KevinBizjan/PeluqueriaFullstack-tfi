package app.menus;

import model.Empleado;
import services.EmpleadoService;

import java.util.Scanner;
import java.util.UUID;

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
        String id = "E" + UUID.randomUUID().toString().substring(0, 6);
        String nombre = leerString("Nombre: ");
        String tipo = leerString("Tipo (Barbero/Estilista): ");

        Empleado e = empleadoService.crearEmpleado(id, nombre, tipo);

        System.out.println("Empleado creado: " + e);
    }

    private void buscar() {
        String id = leerString("ID: ");
        System.out.println(empleadoService.buscarPorId(id));
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
            } catch (Exception e) {
                System.out.println("Número inválido");
            }
        }
    }
}
