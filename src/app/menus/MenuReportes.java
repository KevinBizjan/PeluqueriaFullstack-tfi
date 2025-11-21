package app.menus;

import services.TurnoService;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuReportes {

    private final Scanner sc;
    private final TurnoService turnoService;

    public MenuReportes(Scanner sc, TurnoService turnoService) {
        this.sc = sc;
        this.turnoService = turnoService;
    }

    public void mostrar() {
        int op;
        do {
            System.out.println("\n--- Reportes ---");
            System.out.println("1. Ingresos del día");
            System.out.println("2. Turnos pendientes");
            System.out.println("3. Turnos realizados hoy");
            System.out.println("0. Volver");

            op = leerInt("Opción: ");

            switch (op) {
                case 1 -> mostrarIngresos();
                case 2 -> listarPendientes();
                case 3 -> listarRealizadosHoy();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }

    private void mostrarIngresos() {
        double total = turnoService.calcularIngresosDiarios();
        System.out.println("Ingresos del día: $" + total);
    }

    private void listarPendientes() {
        turnoService.listarTurnos().stream()
                .filter(t -> t.getEstado() == model.Turno.Estado.PENDIENTE)
                .forEach(System.out::println);
    }

    private void listarRealizadosHoy() {
        LocalDate hoy = LocalDate.now();

        turnoService.listarPorFecha(hoy).stream()
                .filter(t -> t.getEstado() == model.Turno.Estado.REALIZADO)
                .forEach(System.out::println);
    }

    // Helpers
    private int leerInt(String prompt) {
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
}
