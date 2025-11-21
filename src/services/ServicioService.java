/**
 * Servicio de gestión de servicios de peluquería.
 * Permite registrar, modificar, eliminar y buscar servicios.
 *
 * <p>Valida precios, duración y evita duplicados.</p>
 *
 * @author Thomas
 */

package services;

import exceptions.ServicioInvalidoException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Servicio;

public class ServicioService {

    private final List<Servicio> servicios = new ArrayList<>();

    public Servicio agregarServicio(Servicio servicio) {
    
    if (servicio.getPrecioBase() <= 0) {
            throw new ServicioInvalidoException("El precio no puede ser negativo ni cero.");
        }
        if (servicio.getDuracionMinutos() <= 0) {
            throw new ServicioInvalidoException("La duración debe ser mayor a 0 minutos.");
        }
        if (servicio.getNombre() == null || servicio.getNombre().isBlank()) {
            throw new ServicioInvalidoException("El nombre del servicio no puede estar vacío.");
        }

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
                        new ServicioInvalidoException("Servicio no encontrado" + id));
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
