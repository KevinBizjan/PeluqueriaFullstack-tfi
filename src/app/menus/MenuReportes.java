package app.menus;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;
import model.Turno;
import services.TurnoService;

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
            System.out.println("4. Ingresos por empleado (hoy)");
            System.out.println("5. Ingresos por servicio (hoy)");
            System.out.println("6. Horas trabajadas por empleado (hoy)");
            System.out.println("7. Ranking de servicios más vendidos (hoy)");
            System.out.println("0. Volver");
            op = leerInt("Opción: ");

            switch (op) {
                case 1 -> mostrarIngresosDelDia();
                case 2 -> mostrarTurnosPendientes();
                case 3 -> mostrarTurnosRealizadosHoy();
                case 4 -> mostrarIngresosPorEmpleadoHoy();
                case 5 -> mostrarIngresosPorServicioHoy();
                case 6 -> mostrarHorasTrabajadasPorEmpleadoHoy();
                case 7 -> mostrarRankingServiciosHoy();
                case 0 -> {
                }
                default -> System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }

    //Opción 1

    private void mostrarIngresosDelDia() {
        double total = turnoService.calcularIngresosDiarios();
        System.out.printf("Ingresos del día: $%.2f%n", total);
    }

    //Opción 2

    private void mostrarTurnosPendientes() {
        System.out.println("\nTurnos pendientes:");
        turnoService.listarTurnos().stream()
                .filter(t -> t.getEstado() == Turno.Estado.PENDIENTE)
                .forEach(System.out::println);
    }

    //Opción 3

    private void mostrarTurnosRealizadosHoy() {
        LocalDate hoy = LocalDate.now();
        System.out.println("\nTurnos realizados hoy (" + hoy + "):");
        turnoService.listarPorFecha(hoy).stream()
                .filter(t -> t.getEstado() == Turno.Estado.REALIZADO)
                .forEach(System.out::println);
    }

    //Opción 4

    private void mostrarIngresosPorEmpleadoHoy() {
        LocalDate hoy = LocalDate.now();
        Map<String, Double> mapa = turnoService.ingresosPorEmpleado(hoy);

        if (mapa.isEmpty()) {
            System.out.println("No hay turnos realizados hoy.");
            return;
        }

        System.out.println("\nIngresos por empleado (hoy " + hoy + "):");
        mapa.forEach((empleado, total) ->
                System.out.printf(" - %s: $%.2f%n", empleado, total)
        );
    }

    //Opción 5

    private void mostrarIngresosPorServicioHoy() {
        LocalDate hoy = LocalDate.now();
        Map<String, Double> mapa = turnoService.ingresosPorServicio(hoy);

        if (mapa.isEmpty()) {
            System.out.println("No hay turnos realizados hoy.");
            return;
        }

        System.out.println("\nIngresos por servicio (hoy " + hoy + "):");
        mapa.forEach((servicio, total) ->
                System.out.printf(" - %s: $%.2f%n", servicio, total)
        );
    }

    //Opción 6

    private void mostrarHorasTrabajadasPorEmpleadoHoy() {
        LocalDate hoy = LocalDate.now();
        Map<String, Integer> mapa = turnoService.minutosTrabajadosPorEmpleado(hoy);

        if (mapa.isEmpty()) {
            System.out.println("No hay turnos realizados hoy.");
            return;
        }

        System.out.println("\nHoras trabajadas por empleado (hoy " + hoy + "):");
        mapa.forEach((empleado, minutos) -> {
            double horas = minutos / 60.0;
            System.out.printf(" - %s: %.2f hs (%d min)%n", empleado, horas, minutos);
        });
    }

    //Opción 7

    private void mostrarRankingServiciosHoy() {
        LocalDate hoy = LocalDate.now();
        Map<String, Long> ranking = turnoService.rankingServicios(hoy);

        if (ranking.isEmpty()) {
            System.out.println("No hay turnos realizados hoy.");
            return;
        }

        System.out.println("\nRanking de servicios más vendidos (hoy " + hoy + "):");
        ranking.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e ->
                        System.out.printf(" - %s: %d turno(s)%n", e.getKey(), e.getValue())
                );
    }

    //Helper lectura int

    private int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Número inválido. Intente nuevamente.");
            } catch (Exception ex) {
                System.out.println(" " + ex.getMessage()); 
            }
        }
    }
}

