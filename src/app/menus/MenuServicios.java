package app.menus;

import exceptions.ElementoNoEncontradoException;
import model.Servicio;
import services.ServicioService;

import java.util.Scanner;

public class MenuServicios {

    private final Scanner sc;
    private final ServicioService servicioService;

    public MenuServicios(Scanner sc, ServicioService servicioService) {
        this.sc = sc;
        this.servicioService = servicioService;
    }

    public void mostrar() {
        int op;
        do {
            System.out.println("\n--- Gestión de Servicios ---");
            System.out.println("1. Agregar servicio");
            System.out.println("2. Buscar servicio por ID");
            System.out.println("3. Buscar servicio por nombre");
            System.out.println("4. Modificar servicio");
            System.out.println("5. Eliminar servicio");
            System.out.println("6. Listar servicios");
            System.out.println("0. Volver");

            op = leerInt("Opción: ");

            try {
                switch (op) {
                    case 1 -> agregar();
                    case 2 -> buscarPorId();
                    case 3 -> buscarPorNombre();
                    case 4 -> modificar();
                    case 5 -> eliminar();
                    case 6 -> listar();
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (ElementoNoEncontradoException | IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

        } while (op != 0);
    }

    private void agregar() {
        String nombre = leerString("Nombre: ");
        double precio = leerDouble("Precio: ");
        int duracion = leerInt("Duración en minutos: ");

        Servicio s = new Servicio(null, nombre, precio, duracion);
        s = servicioService.agregarServicio(s);

        System.out.println("Servicio creado: " + s);
    }

    private void buscarPorId() {
        String id = leerString("ID: ");
        System.out.println(servicioService.buscarPorId(id));
    }

    private void buscarPorNombre() {
        String q = leerString("Nombre: ");
        servicioService.buscarPorNombre(q).forEach(System.out::println);
    }

    private void modificar() {
        String id = leerString("ID servicio a modificar: ");

        String nuevoNombre = leerString("Nuevo nombre (enter=mantener): ");
        String precioTxt = leerString("Nuevo precio (enter=mantener): ");
        String durTxt = leerString("Nueva duración (enter=mantener): ");

        Double precio = precioTxt.isBlank() ? null : Double.parseDouble(precioTxt);
        Integer dur = durTxt.isBlank() ? null : Integer.parseInt(durTxt);

        servicioService.modificarServicio(id,
                nuevoNombre.isBlank() ? null : nuevoNombre,
                precio,
                dur);

        System.out.println("Servicio modificado.");
    }

    private void eliminar() {
        String id = leerString("ID: ");
        boolean ok = servicioService.eliminarServicio(id);

        System.out.println(ok ? "Servicio eliminado." : "No existe ese servicio.");
    }

    private void listar() {
        servicioService.listarServicios().forEach(System.out::println);
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

    private double leerDouble(String txt) {
        while (true) {
            System.out.print(txt);
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Número inválido");
            }
        }
    }
}
