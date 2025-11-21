/**
 * Excepción lanzada cuando el servicio tiene datos inválidos:
 * precio negativo, duración incorrecta, etc.
 *
 * @author Sebas
 */

package exceptions;

public class ServicioInvalidoException extends RuntimeException {
    public ServicioInvalidoException(String mensaje) {
        super(mensaje);
    }
}