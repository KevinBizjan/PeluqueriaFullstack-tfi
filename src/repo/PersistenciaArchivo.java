package repo;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import model.Barbero;
import model.Cliente;
import model.Empleado;
import model.Estilista;
import model.Servicio;
import model.Turno;
import services.ClienteService;
import services.EmpleadoService;
import services.ServicioService;
import services.TurnoService;

/**
 * Cada línea del archivo es un objeto JSON chiquito, por ej:
 * {"id":"E1","nombre":"Juan","especialidad":"Barbero"}
 */
public class PersistenciaArchivo {

    private static final String DATA_DIR = "data";

    private static final String CLIENTES_FILE  = DATA_DIR + File.separator + "clientes.json";
    private static final String SERVICIOS_FILE = DATA_DIR + File.separator + "servicios.json";
    private static final String EMPLEADOS_FILE = DATA_DIR + File.separator + "empleados.json";
    private static final String TURNOS_FILE    = DATA_DIR + File.separator + "turnos.json";

    public static void guardarTodo(ClienteService cs,
                                   ServicioService ss,
                                   EmpleadoService es,
                                   TurnoService ts) {
        asegurarDirectorio();

        try {
            guardarClientes(cs, CLIENTES_FILE);
            guardarServicios(ss, SERVICIOS_FILE);
            guardarEmpleados(es, EMPLEADOS_FILE);
            guardarTurnos(ts, TURNOS_FILE);
        } catch (IOException e) {
            System.out.println("Error guardando datos en archivos: " + e.getMessage());
        }
    }

    public static void cargarTodo(ClienteService cs,
                                  ServicioService ss,
                                  EmpleadoService es,
                                  TurnoService ts) {
        asegurarDirectorio();

        try {
            cargarClientes(cs, CLIENTES_FILE);
            cargarServicios(ss, SERVICIOS_FILE);
            cargarEmpleados(es, EMPLEADOS_FILE);
            cargarTurnos(ts, cs, ss, es, TURNOS_FILE);
        } catch (IOException e) {
            System.out.println("Error cargando datos desde archivos: " + e.getMessage());
        }
    }

    private static void guardarClientes(ClienteService cs, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Cliente c : cs.listarClientes()) {
                String json = "{"
                        + "\"id\":" + quote(c.getId())
                        + ",\"nombre\":" + quote(c.getNombre())
                        + ",\"apellido\":" + quote(c.getApellido())
                        + ",\"dni\":" + quote(c.getDni())
                        + ",\"telefono\":" + quote(c.getTelefono())
                        + "}";
                pw.println(json);
            }
        }
    }

    private static void cargarClientes(ClienteService cs, String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Map<String, String> map = parseJsonLine(line);
                if (map == null) continue;

                String id       = nullIfLiteralNull(map.get("id"));
                String nombre   = map.get("nombre");
                String apellido = map.get("apellido");
                String dni      = map.get("dni");
                String tel      = map.get("telefono");

                Cliente c = new Cliente(id, nombre, apellido, dni, tel);
                try {
                    cs.agregarCliente(c);
                } catch (IllegalArgumentException ignore) {
                    // si está duplicado por DNI, lo ignoramos
                }
            }
        }
    }

    private static void guardarServicios(ServicioService ss, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Servicio s : ss.listarServicios()) {
                String json = "{"
                        + "\"id\":" + quote(s.getId())
                        + ",\"nombre\":" + quote(s.getNombre())
                        + ",\"precioBase\":" + s.getPrecioBase()
                        + ",\"duracionMinutos\":" + s.getDuracionMinutos()
                        + "}";
                pw.println(json);
            }
        }
    }

    private static void cargarServicios(ServicioService ss, String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Map<String, String> map = parseJsonLine(line);
                if (map == null) continue;

                String id     = nullIfLiteralNull(map.get("id"));
                String nombre = map.get("nombre");
                double precio = Double.parseDouble(map.get("precioBase"));
                int duracion  = Integer.parseInt(map.get("duracionMinutos"));

                Servicio s = new Servicio(id, nombre, precio, duracion);
                try {
                    ss.agregarServicio(s);
                } catch (IllegalArgumentException ignore) {
                    // si está duplicado, ignoramos
                }
            }
        }
    }

    private static void guardarEmpleados(EmpleadoService es, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Empleado e : es.listarEmpleados()) {
                // inferimos tipo desde la clase concreta
                String tipo;
                if (e instanceof Barbero) tipo = "Barbero";
                else if (e instanceof Estilista) tipo = "Estilista";
                else tipo = e.getEspecialidad();

                String json = "{"
                        + "\"id\":" + quote(e.getId())
                        + ",\"nombre\":" + quote(e.getNombre())
                        + ",\"tipo\":" + quote(tipo)
                        + "}";
                pw.println(json);
            }
        }
    }

    private static void cargarEmpleados(EmpleadoService es, String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Map<String, String> map = parseJsonLine(line);
                if (map == null) continue;

                String id    = map.get("id");
                String nombre = map.get("nombre");
                String tipo   = map.get("tipo");

                try {
                    es.crearEmpleado(id, nombre, tipo);
                } catch (IllegalArgumentException ignore) {
                    // duplicados -> ignoramos
                }
            }
        }
    }

    private static void guardarTurnos(TurnoService ts, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (Turno t : ts.listarTurnos()) {
                String json = "{"
                        + "\"id\":" + quote(t.getId())
                        + ",\"clienteDni\":" + quote(t.getCliente().getDni())
                        + ",\"empleadoId\":" + quote(t.getEmpleado().getId())
                        + ",\"servicioId\":" + quote(t.getServicio().getId())
                        + ",\"fechaHora\":" + quote(t.getFechaHora().toString())
                        + ",\"estado\":" + quote(t.getEstado().name())
                        + "}";
                pw.println(json);
            }
        }
    }

    private static void cargarTurnos(TurnoService ts,
                                     ClienteService cs,
                                     ServicioService ss,
                                     EmpleadoService es,
                                     String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Map<String, String> map = parseJsonLine(line);
                if (map == null) continue;

                String id          = map.get("id");
                String clienteDni  = map.get("clienteDni");
                String empleadoId  = map.get("empleadoId");
                String servicioId  = map.get("servicioId");
                String fechaStr    = map.get("fechaHora");
                String estadoStr   = map.get("estado");

                Cliente cliente;
                Servicio servicio;
                Empleado empleado;

                try {
                    cliente = cs.buscarPorDni(clienteDni);
                    servicio = ss.buscarPorId(servicioId);
                    empleado = es.buscarPorId(empleadoId);
                } catch (Exception ex) {
                    // si algo no existe, salteamos ese turno
                    continue;
                }

                LocalDateTime fechaHora = LocalDateTime.parse(fechaStr);

                ts.registrarTurno(id, cliente, empleado, servicio, fechaHora);

                // Ajustar estado
                Turno t = ts.buscarPorId(id);
                if (t != null) {
                    if ("REALIZADO".equalsIgnoreCase(estadoStr)) {
                        t.realizar();
                    } else if ("CANCELADO".equalsIgnoreCase(estadoStr)) {
                        t.cancelar();
                    }
                }
            }
        }
    }

    private static String quote(String s) {
        if (s == null) return "null";
        String esc = s.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + esc + "\"";
    }

    private static String unquote(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.equals("null")) return null;
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static Map<String, String> parseJsonLine(String line) {
        if (!line.startsWith("{") || !line.endsWith("}")) return null;
        String inner = line.substring(1, line.length() - 1); // sin { }
        // dividimos por comas principales (no tenemos arrays ni objetos anidados)
        String[] parts = inner.split(",");

        Map<String, String> map = new HashMap<>();
        for (String part : parts) {
            String[] kv = part.split(":", 2);
            if (kv.length != 2) continue;
            String key = unquote(kv[0].trim());
            String val = unquote(kv[1].trim());
            map.put(key, val);
        }
        return map;
    }

    private static String nullIfLiteralNull(String s) {
        if (s == null) return null;
        if ("null".equalsIgnoreCase(s)) return null;
        if (s.isBlank()) return null;
        return s;
    }

    private static void asegurarDirectorio() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
