package app.menus;

import exceptions.ElementoNoEncontradoException;
import model.Cliente;
import model.Empleado;
import model.Servicio;
import services.ClienteService;
import services.EmpleadoService;
import services.ServicioService;
import services.TurnoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.IntStream;

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
        System.out.println("\n-- Crear Turno --");

        // Cliente
        String q = leerString("Buscar cliente por DNI o nombre: ");
        Cliente cliente = buscarCliente(q);

        if (cliente == null) {
            System.out.println("No se encontró cliente.");
            return;
        }

        // Servicio
        List<Servicio> servicios = servicioService.listarServicios();
        System.out.println("Seleccione servicio:");
        IntStream.range(0, servicios.size())
                .forEach(i -> System.out.println((i + 1) + ". " + servicios.get(i)));
        Servicio servicio = servicios.get(leerInt("Opción: ") - 1);

        // Empleado
        List<Empleado> empleados = empleadoService.listarEmpleados();
        System.out.println("Seleccione empleado:");
        IntStream.range(0, empleados.size())
                .forEach(i -> System.out.println((i + 1) + ". " + empleados.get(i)));
        Empleado empleado = empleados.get(leerInt("Opción: ") - 1);

        // Fecha y hora
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(
                    leerString("Fecha y hora (dd/MM/yyyy HH:mm): "),
                    FECHA_HORA
            );
        } catch (Exception e) {
            System.out.println("Formato inválido.");
            return;
        }

        // Disponibilidad
        if (!turnoService.estaDisponible(empleado.getId(), fechaHora)) {
            System.out.println("Empleado no disponible.");
            return;
        }

        String id = "T" + UUID.randomUUID().toString().substring(0, 6);
        turnoService.registrarTurno(id, cliente, empleado, servicio, fechaHora);

        System.out.println("Turno creado: " + id);
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
            } catch (Exception e) {
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
