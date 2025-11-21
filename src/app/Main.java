package app;

import app.menus.MenuClientes;
import app.menus.MenuEmpleados;
import app.menus.MenuReportes;
import app.menus.MenuServicios;
import app.menus.MenuTurnos;
import java.util.Scanner;
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

        seedDatosIniciales(clienteService, servicioService, empleadoService);

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

        } while (opcion != 0);

        sc.close();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== PELUQUERÍA - MENÚ PRINCIPAL =====");
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

    // Seed
    private static void seedDatosIniciales(
            ClienteService cs,
            ServicioService ss,
            EmpleadoService es
    ) {
        try {
            cs.agregarCliente(new model.Cliente(null, "Juan", "Pérez", "12345678", "155512345"));
            cs.agregarCliente(new model.Cliente(null, "María", "Gómez", "87654321", "155598765"));
        } catch (Exception ignored) {}

        try {
            ss.agregarServicio(new model.Servicio(null, "Corte hombre", 3000.0, 30));
            ss.agregarServicio(new model.Servicio(null, "Corte mujer", 4000.0, 45));
            ss.agregarServicio(new model.Servicio(null, "Barba", 1000.0, 15));
        } catch (Exception ignored) {}

        try {
            es.crearEmpleado("E1", "Juan", "Barbero");
            es.crearEmpleado("E2", "Matías", "Barbero");
            es.crearEmpleado("E3", "Laura", "Estilista");
        } catch (Exception ignored) {}
    }
}
