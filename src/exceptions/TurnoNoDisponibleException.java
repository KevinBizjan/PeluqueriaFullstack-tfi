/**
 * Excepción lanzada cuando un empleado no tiene disponibilidad
 * para un horario solicitado.
 *
 * @author Sebas
 */

package exceptions;

public class TurnoNoDisponibleException extends RuntimeException {
    public TurnoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}