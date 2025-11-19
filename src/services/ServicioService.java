package services;

import exceptions.ElementoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Servicio;

public class ServicioService {

    private final List<Servicio> servicios = new ArrayList<>();

    public Servicio agregarServicio(Servicio servicio) {

    String nuevoId = (servicio.getId() == null || servicio.getId().isBlank())
            ? "S" + UUID.randomUUID().toString().substring(0, 6)
            : servicio.getId();

    boolean existe = servicios.stream()
            .anyMatch(s -> nuevoId.equalsIgnoreCase(s.getId()));

    if (existe) {
        throw new IllegalArgumentException("Ya existe un servicio con ID: " + nuevoId);
    }

    Servicio nuevo = new Servicio(
            nuevoId,
            servicio.getNombre(),
            servicio.getPrecioBase(),
            servicio.getDuracionMinutos()
    );

    servicios.add(nuevo);
    return nuevo;
    }


    public Servicio buscarPorId(String id) {
        return servicios.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() ->
                        new ElementoNoEncontradoException("Servicio no encontrado: " + id));
    }

    public List<Servicio> buscarPorNombre(String nombre) {
        return servicios.stream()
                .filter(s -> s.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public void modificarServicio(String id, String nuevoNombre, Double nuevoPrecio, Integer nuevaDuracion) {
        Servicio s = buscarPorId(id);

        if (nuevoNombre != null) s.setNombre(nuevoNombre);
        if (nuevoPrecio != null) s.setPrecioBase(nuevoPrecio);
        if (nuevaDuracion != null) s.setDuracionMinutos(nuevaDuracion);
    }

    public boolean eliminarServicio(String id) {
        return servicios.removeIf(s -> s.getId().equalsIgnoreCase(id));
    }

    public List<Servicio> listarServicios() {
        return servicios;
    }
}
