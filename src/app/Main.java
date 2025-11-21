package app;

import app.menus.MenuClientes;
import app.menus.MenuEmpleados;
import app.menus.MenuReportes;
import app.menus.MenuServicios;
import app.menus.MenuTurnos;
import java.util.Scanner;
import repo.PersistenciaArchivo;
import services.ClienteService;
import services.EmpleadoService;
import services.ServicioService;
import services.TurnoService;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        ClienteService clienteService = new ClienteService();
        ServicioService servicioService = new ServicioService();
        EmpleadoService empleadoService = new EmpleadoService();
        TurnoService turnoService = new TurnoService();

        PersistenciaArchivo.cargarTodo(
                clienteService,
                servicioService,
                empleadoService,
                turnoService
        );

        // Crear menús
        MenuClientes menuClientes = new MenuClientes(sc, clienteService);
        MenuServicios menuServicios = new MenuServicios(sc, servicioService);
        MenuEmpleados menuEmpleados = new MenuEmpleados(sc, empleadoService);
        MenuTurnos menuTurnos = new MenuTurnos(sc, clienteService, servicioService, empleadoService, turnoService);
        MenuReportes menuReportes = new MenuReportes(sc, turnoService);

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerInt("Opción: ");

            switch (opcion) {
                case 1 -> menuClientes.mostrar();
                case 2 -> menuServicios.mostrar();
                case 3 -> menuEmpleados.mostrar();
                case 4 -> menuTurnos.mostrar();
                case 5 -> menuReportes.mostrar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

            PersistenciaArchivo.guardarTodo(
                    clienteService,
                    servicioService,
                    empleadoService,
                    turnoService
            );

        } while (opcion != 0);

        sc.close();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\nPELUQUERÍA FULLSTACK - MENÚ PRINCIPAL");
        System.out.println("1. Gestión de clientes");
        System.out.println("2. Gestión de servicios");
        System.out.println("3. Gestión de empleados");
        System.out.println("4. Gestión de turnos");
        System.out.println("5. Reportes");
        System.out.println("0. Salir");
    }

    private static int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Intente otra vez.");
            }
        }
    }
}
