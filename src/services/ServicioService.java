package services;

import model.Servicio;
import exceptions.ElementoNoEncontradoException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ServicioService {

    private final Map<String, Servicio> serviciosPorId = new HashMap<>();
    private final AtomicInteger contadorId = new AtomicInteger(1);

    // devuelve servicio con id asignado
    public Servicio agregarServicio(Servicio servicio) {
        String id = servicio.getId() != null ? servicio.getId() : "S" + contadorId.getAndIncrement();
        Servicio s = new Servicio(id, servicio.getNombre(), servicio.getPrecioBase(), servicio.getDuracionMinutos());
        serviciosPorId.put(s.getId(), s);
        return s;
    }

    // Listar orden por nombre
    public List<Servicio> listarServicios() {
        return serviciosPorId.values()
                .stream()
                .sorted(Comparator.comparing(Servicio::getNombre, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());
    }


    public Servicio buscarPorId(String id) {
        Servicio s = serviciosPorId.get(id);
        if (s == null) throw new ElementoNoEncontradoException("Servicio con id " + id + " no encontrado");
        return s;
    }


    public Servicio modificarServicio(String id, String nuevoNombre, Double nuevoPrecio, Integer nuevaDuracion) {
        Servicio s = serviciosPorId.get(id);
        if (s == null) throw new ElementoNoEncontradoException("Servicio con id " + id + " no encontrado");
        if (nuevoNombre != null) s.setNombre(nuevoNombre);
        if (nuevoPrecio != null) s.setPrecioBase(nuevoPrecio);
        if (nuevaDuracion != null) s.setDuracionMinutos(nuevaDuracion);
        return s;
    }


    public boolean eliminarServicio(String id) {
        return serviciosPorId.remove(id) != null;
    }

    // Buscar por nombre (fuzzy)
    public List<Servicio> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return Collections.emptyList();
        String q = nombre.trim().toLowerCase();
        return serviciosPorId.values().stream()
                .filter(s -> s.getNombre().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public boolean existeServicio(String id) {
        return serviciosPorId.containsKey(id);
    }

    public int cantidadServicios() {
        return serviciosPorId.size();
    }
}
