package app;

import exceptions.ElementoNoEncontradoException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.IntStream;
import model.Cliente;
import model.Empleado;
import model.Servicio;
import services.ClienteService;
import services.EmpleadoService;
import services.ServicioService;
import services.TurnoService;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        // Services (asegurate de tener las implementaciones e inyectarlas si prefieres)
        ClienteService clienteService = new ClienteService();
        ServicioService servicioService = new ServicioService();
        EmpleadoService empleadoService = new EmpleadoService(); // lo hacemos en el siguiente paso
        TurnoService turnoService = new TurnoService();

        // Carga inicial mínima (opcional pero recomendable para demo)
        seedDatosIniciales(clienteService, servicioService, empleadoService);

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerInt("Opción: ");
            switch (opcion) {
                case 1 -> gestionClientes(clienteService);
                case 2 -> gestionServicios(servicioService);
                case 3 -> gestionEmpleados(empleadoService);
                case 4 -> gestionTurnos(clienteService, servicioService, empleadoService, turnoService);
                case 5 -> reportes(turnoService);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);

        sc.close();
        System.out.println("Programa finalizado. ¡Gracias!");
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

    private static void gestionClientes(ClienteService clienteService) {
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
                    case 1 -> {
                        String nombre = leerString("Nombre: ");
                        String apellido = leerString("Apellido: ");
                        String dni = leerString("DNI: ");
                        String telefono = leerString("Teléfono: ");
                        Cliente c = new Cliente(null, nombre, apellido, dni, telefono);
                        clienteService.agregarCliente(c);
                        System.out.println("Cliente agregado: " + c);
                    }
                    case 2 -> {
                        String dni = leerString("DNI: ");
                        Cliente c = clienteService.buscarPorDni(dni);
                        System.out.println(c);
                    }
                    case 3 -> {
                        String q = leerString("Nombre o parte del nombre: ");
                        List<Cliente> lista = clienteService.buscarPorNombre(q);
                        if (lista.isEmpty()) System.out.println("No se encontraron clientes.");
                        else lista.forEach(System.out::println);
                    }
                    case 4 -> {
                        String dni = leerString("DNI del cliente a modificar: ");
                        String nuevoNombre = leerString("Nuevo nombre (enter = mantener): ");
                        String nuevoApellido = leerString("Nuevo apellido (enter = mantener): ");
                        String nuevoTel = leerString("Nuevo teléfono (enter = mantener): ");
                        clienteService.modificarCliente(dni,
                                nuevoNombre.isBlank() ? null : nuevoNombre,
                                nuevoApellido.isBlank() ? null : nuevoApellido,
                                nuevoTel.isBlank() ? null : nuevoTel);
                        System.out.println("Cliente modificado.");
                    }
                    case 5 -> {
                        String dni = leerString("DNI del cliente a eliminar: ");
                        boolean ok = clienteService.eliminarPorDni(dni);
                        System.out.println(ok ? "Cliente eliminado." : "No se encontró cliente.");
                    }
                    case 6 -> clienteService.listarClientes().forEach(System.out::println);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } while (op != 0);
    }

    private static void gestionServicios(ServicioService servicioService) {
        int op;
        do {
            System.out.println("\n--- Gestión de Servicios ---");
            System.out.println("1. Agregar servicio");
            System.out.println("2. Buscar servicio por id");
            System.out.println("3. Buscar servicio por nombre");
            System.out.println("4. Modificar servicio");
            System.out.println("5. Eliminar servicio");
            System.out.println("6. Listar servicios");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1 -> {
                        String nombre = leerString("Nombre del servicio: ");
                        double precio = leerDouble("Precio base: ");
                        int duracion = leerInt("Duración en minutos: ");
                        Servicio s = new Servicio(null, nombre, precio, duracion);
                        s = servicioService.agregarServicio(s);
                        System.out.println("Servicio creado: " + s);
                    }
                    case 2 -> {
                        String id = leerString("ID servicio: ");
                        System.out.println(servicioService.buscarPorId(id));
                    }
                    case 3 -> {
                        String q = leerString("Buscar por nombre: ");
                        servicioService.buscarPorNombre(q).forEach(System.out::println);
                    }
                    case 4 -> {
                        String id = leerString("ID servicio a modificar: ");
                        String nuevoNombre = leerString("Nuevo nombre (enter=mantener): ");
                        String precioTxt = leerString("Nuevo precio (enter=mantener): ");
                        String durTxt = leerString("Nueva duración min (enter=mantener): ");
                        Double p = precioTxt.isBlank() ? null : Double.parseDouble(precioTxt);
                        Integer d = durTxt.isBlank() ? null : Integer.parseInt(durTxt);
                        servicioService.modificarServicio(id,
                                nuevoNombre.isBlank() ? null : nuevoNombre,
                                p, d);
                        System.out.println("Servicio modificado.");
                    }
                    case 5 -> {
                        String id = leerString("ID servicio a eliminar: ");
                        boolean ok = servicioService.eliminarServicio(id);
                        System.out.println(ok ? "Servicio eliminado." : "No se encontró servicio.");
                    }
                    case 6 -> servicioService.listarServicios().forEach(System.out::println);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } while (op != 0);
    }

    private static void gestionEmpleados(EmpleadoService empleadoService) {
        int op;
        do {
            System.out.println("\n--- Gestión de Empleados ---");
            System.out.println("1. Listar empleados");
            System.out.println("2. Agregar empleado (solo para pruebas)");
            System.out.println("3. Buscar por id");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1 -> empleadoService.listarEmpleados()
                         .forEach(e -> System.out.println(e.toString()));
                    case 2 -> {
                        String id = "E" + UUID.randomUUID().toString().substring(0, 6);
                        String nombre = leerString("Nombre: ");
                        String tipo = leerString("Tipo (Barbero/Estilista): ");
                        Empleado e = empleadoService.crearEmpleado(id, nombre, tipo);
                        System.out.println("Empleado creado: " + e);
                    }
                    case 3 -> {
                        String id = leerString("ID empleado: ");
                        System.out.println(empleadoService.buscarPorId(id));
                    }
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } while (op != 0);
    }

    private static void gestionTurnos(ClienteService clienteService,
                                      ServicioService servicioService,
                                      EmpleadoService empleadoService,
                                      TurnoService turnoService) {
        int op;
        do {
            System.out.println("\n--- Gestión de Turnos ---");
            System.out.println("1. Crear turno");
            System.out.println("2. Cancelar turno (por ID)");
            System.out.println("3. Finalizar turno (marcar realizado)");
            System.out.println("4. Listar turnos por día");
            System.out.println("5. Listar turnos por empleado");
            System.out.println("6. Listar todos los turnos");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");
            try {
                switch (op) {
                    case 1 -> crearTurnoFlow(clienteService, servicioService, empleadoService, turnoService);
                    case 2 -> {
                        String id = leerString("ID del turno a cancelar: ");
                        boolean ok = turnoService.cancelarTurno(id);
                        System.out.println(ok ? "Turno cancelado." : "No se encontró turno.");
                    }
                    case 3 -> {
                        String id = leerString("ID del turno a finalizar: ");
                        boolean ok = turnoService.finalizarTurno(id);
                        System.out.println(ok ? "Turno finalizado." : "No se encontró turno.");
                    }
                    case 4 -> {
                        LocalDate fecha = leerFechaSinHora("Fecha (dd/MM/yyyy): ");
                        if (fecha == null) return;

                        turnoService.listarPorFecha(fecha).forEach(System.out::println);
                    }
                    case 5 -> {
                        String empId = leerString("ID empleado: ");
                        turnoService.listarPorEmpleado(empId).forEach(System.out::println);
                    }
                    case 6 -> turnoService.listarTurnos().forEach(System.out::println);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } while (op != 0);
    }

    private static void reportes(TurnoService turnoService) {
        int op;
        do {
            System.out.println("\n--- Reportes ---");
            System.out.println("1. Ingresos del día");
            System.out.println("2. Turnos pendientes");
            System.out.println("3. Turnos realizados hoy");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");
            switch (op) {
                case 1 -> {
                    double total = turnoService.calcularIngresosDiarios();
                    System.out.println("Ingresos del día: $" + total);
                }
                case 2 -> turnoService.listarTurnos().stream()
                        .filter(t -> t.getEstado() == model.Turno.Estado.PENDIENTE)
                        .forEach(System.out::println);
                case 3 -> {
                    LocalDate hoy = LocalDate.now();
                    turnoService.listarPorFecha(hoy).stream()
                            .filter(t -> t.getEstado() == model.Turno.Estado.REALIZADO)
                            .forEach(System.out::println);
                }
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }
    //Flujo auxiliar
    private static void crearTurnoFlow(ClienteService clienteService,
                                       ServicioService servicioService,
                                       EmpleadoService empleadoService,
                                       TurnoService turnoService) {
        System.out.println("\n-- Crear Turno --");

        // Seleccionar cliente búsqueda por nombre
        String q = leerString("Buscar cliente por DNI o nombre: ");
        Cliente cliente = null;
        if (q.matches("\\d+")) {
            try {
                cliente = clienteService.buscarPorDni(q);
            } catch (ElementoNoEncontradoException ignored) {}
        }
        if (cliente == null) {
            List<Cliente> matches = clienteService.buscarPorNombre(q);
            if (matches.isEmpty()) {
                System.out.println("No hay clientes. Registre el cliente primero.");
                return;
            } else if (matches.size() == 1) {
                cliente = matches.get(0);
            } else {
                System.out.println("Elija cliente:");
                IntStream.range(0, matches.size()).forEach(i ->
                        System.out.println((i + 1) + ". " + matches.get(i)));
                int idx = leerInt("Opción: ");
                cliente = matches.get(idx - 1);
            }
        }

        // Seleccionar servicio
        List<Servicio> servicios = servicioService.listarServicios();
        if (servicios.isEmpty()) {
            System.out.println("No hay servicios cargados.");
            return;
        }
        System.out.println("Seleccione servicio:");
        IntStream.range(0, servicios.size()).forEach(i ->
                System.out.println((i + 1) + ". " + servicios.get(i)));
        int sIdx = leerInt("Opción servicio: ");
        Servicio servicio = servicios.get(sIdx - 1);

        // Seleccionar empleado
        List<Empleado> empleados = empleadoService.listarEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados cargados.");
            return;
        }
        System.out.println("Seleccione empleado:");
        IntStream.range(0, empleados.size()).forEach(i ->
                System.out.println((i + 1) + ". " + empleados.get(i)));
        int eIdx = leerInt("Opción empleado: ");
        Empleado empleado = empleados.get(eIdx - 1);

        // Fecha y hora
        String fechaTxt = leerString("Fecha y hora (dd/MM/yyyy HH:mm): ");
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(fechaTxt, FECHA_FMT);
        } catch (DateTimeParseException ex) {
            System.out.println("Formato de fecha inválido. Use dd/MM/yyyy HH:mm");
            return;
        }

        // Validar disponibilidad
        if (!turnoService.estaDisponible(empleado.getId(), fechaHora)) {
            System.out.println("Empleado no disponible en esa fecha/hora.");
            return;
        }

        // Crear turno con ID automático
        String idTurno = "T" + UUID.randomUUID().toString().substring(0, 6);
        turnoService.registrarTurno(idTurno, cliente, empleado, servicio, fechaHora);
        System.out.println("Turno creado con ID: " + idTurno);
    }

    //Helpers de lectura

    private static String leerString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Número inválido. Intente nuevamente.");
            }
        }
    }

    private static double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException ex) {
                System.out.println("Número inválido. Intente nuevamente.");
            }
        }
    }

    private static LocalDate leerFechaSinHora(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isBlank()) {
                return null;
            }
            try {
                return LocalDate.parse(line, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException ex) {
                System.out.println("Formato inválido. Use dd/MM/yyyy");
            }
        }
    }


    // Seed de datos
    private static void seedDatosIniciales(ClienteService cs, ServicioService ss, EmpleadoService es) {
        // Si existen datos no duplicar
        try {
            cs.agregarCliente(new Cliente(null, "Juan", "Pérez", "12345678", "155512345"));
            cs.agregarCliente(new Cliente(null, "María", "Gómez", "87654321", "155598765"));
        } catch (IllegalArgumentException ignore) {}

        try {
            ss.agregarServicio(new Servicio(null, "Corte hombre", 3000.0, 30));
            ss.agregarServicio(new Servicio(null, "Corte mujer", 4000.0, 45));
            ss.agregarServicio(new Servicio(null, "Barba", 1000.0, 15));
        } catch (IllegalArgumentException ignore) {}

        try {
            es.crearEmpleado("E1", "Juan", "Barbero");
            es.crearEmpleado("E2", "Matías", "Barbero");
            es.crearEmpleado("E3", "Laura", "Estilista");
        } catch (IllegalArgumentException ignore) {}
    }
}
