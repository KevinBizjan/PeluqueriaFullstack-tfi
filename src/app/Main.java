package app;

import services.ClienteService;
import services.TurnoService;
import model.Cliente;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ClienteService clienteService = new ClienteService();
        TurnoService turnoService = new TurnoService();

        int opcion;
        do {
            System.out.println("===== PELUQUERÍA =====");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Crear turno");
            System.out.println("4. Cancelar turno");
            System.out.println("5. Ver ingresos diarios");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String tel = sc.nextLine();
                    clienteService.agregarCliente(new Cliente(nombre, tel));
                    break;

                case 2:
                    System.out.println(clienteService.listarClientes());
                    break;

                case 3:
                    System.out.println("Crear turno → (a completar)");
                    break;

                case 4:
                    System.out.println("Cancelar turno → (a completar)");
                    break;

                case 5:
                    System.out.println("Ingresos diarios → (a completar)");
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;
            }

        } while (opcion != 0);
    }
}