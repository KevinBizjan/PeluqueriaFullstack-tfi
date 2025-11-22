package app.menus;

import exceptions.ElementoNoEncontradoException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import model.Cliente;
import model.Empleado;
import model.Servicio;
import services.ClienteService;
import services.EmpleadoService;
import services.ServicioService;
import services.TurnoService;

public class MenuTurnos {

    private final Scanner sc;
    private final ClienteService clienteService;
    private final ServicioService servicioService;
    private final EmpleadoService empleadoService;
    private final TurnoService turnoService;

    private final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MenuTurnos(
            Scanner sc,
            ClienteService clienteService,
            ServicioService servicioService,
            EmpleadoService empleadoService,
            TurnoService turnoService
    ) {
        this.sc = sc;
        this.clienteService = clienteService;
        this.servicioService = servicioService;
        this.empleadoService = empleadoService;
        this.turnoService = turnoService;
    }

    public void mostrar() {
        int op;
        do {
            System.out.println("\n--- Gestión de Turnos ---");
            System.out.println("1. Crear turno");
            System.out.println("2. Cancelar turno");
            System.out.println("3. Finalizar turno");
            System.out.println("4. Listar turnos por día");
            System.out.println("5. Listar turnos por empleado");
            System.out.println("6. Listar todos los turnos");
            System.out.println("0. Volver");

            op = leerInt("Opción: ");

            try {
                switch (op) {
                    case 1 -> crearTurno();
                    case 2 -> cancelar();
                    case 3 -> finalizar();
                    case 4 -> listarPorDia();
                    case 5 -> listarPorEmpleado();
                    case 6 -> listarTodos();
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

        } while (op != 0);
    }

    private void crearTurno() {
        try {
            System.out.println("\n-- Crear Turno --");

            String q = leerString("Buscar cliente por DNI o nombre: ");
            Cliente cliente = buscarCliente(q);
            if (cliente == null) {
                System.out.println("No se encontró cliente.");
                return;
            }

            List<Servicio> servicios = servicioService.listarServicios();
            System.out.println("Seleccione servicio:");
            IntStream.range(0, servicios.size())
                    .forEach(i -> System.out.println((i + 1) + ". " + servicios.get(i)));
            int opServ = leerInt("Opción: ");
            if (opServ < 1 || opServ > servicios.size()) {
                System.out.println("Opción de servicio inválida.");
                return;
            }
            Servicio servicio = servicios.get(opServ - 1);

            List<Empleado> empleados = empleadoService.listarEmpleados();
            System.out.println("Seleccione empleado:");
            IntStream.range(0, empleados.size())
                    .forEach(i -> System.out.println((i + 1) + ". " + empleados.get(i)));
            int opEmp = leerInt("Opción: ");
            if (opEmp < 1 || opEmp > empleados.size()) {
                System.out.println("Opción de empleado inválida.");
                return;
            }
            Empleado empleado = empleados.get(opEmp - 1);

            LocalDateTime fechaHora = LocalDateTime.parse(
                    leerString("Fecha y hora (dd/MM/yyyy HH:mm): "),
                    FECHA_HORA
            );
            if (fechaHora.isBefore(LocalDateTime.now())) {
                System.out.println("No se pueden asignar turnos en el pasado.");
                return;
            }

            String id = turnoService.generarIdTurno();
            turnoService.registrarTurno(id, cliente, empleado, servicio, fechaHora);

            System.out.println("Turno creado: " + id);

        } catch (exceptions.TurnoNoDisponibleException ex) {
            System.out.println(" " + ex.getMessage());
        } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private Cliente buscarCliente(String q) {
        if (q.matches("\\d+")) {
            try {
                return clienteService.buscarPorDni(q);
            } catch (ElementoNoEncontradoException ignored) {}
        }

        List<Cliente> lista = clienteService.buscarPorNombre(q);

        if (lista.isEmpty()) return null;
        if (lista.size() == 1) return lista.get(0);

        System.out.println("Seleccione cliente:");
        IntStream.range(0, lista.size())
                .forEach(i -> System.out.println((i + 1) + ". " + lista.get(i)));

        return lista.get(leerInt("Opción: ") - 1);
    }

    private void cancelar() {
        String id = leerString("ID turno: ");
        System.out.println(turnoService.cancelarTurno(id) ? "Cancelado." : "No existe.");
    }

    private void finalizar() {
        String id = leerString("ID turno: ");
        System.out.println(turnoService.finalizarTurno(id) ? "Finalizado." : "No existe.");
    }

    private void listarPorDia() {
        LocalDate fecha = leerFecha("Fecha (dd/MM/yyyy): ");
        turnoService.listarPorFecha(fecha).forEach(System.out::println);
    }

    private void listarPorEmpleado() {
        String id = leerString("ID empleado: ");
        turnoService.listarPorEmpleado(id).forEach(System.out::println);
    }

    private void listarTodos() {
        turnoService.listarTurnos().forEach(System.out::println);
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
                System.out.println("Número inválido.");
            }
        }
    }

    private LocalDate leerFecha(String txt) {
        while (true) {
            System.out.print(txt);
            try {
                return LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                System.out.println("Formato inválido.");
            }
        }
    }
}
