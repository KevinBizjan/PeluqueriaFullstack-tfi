/**
 * Excepción lanzada cuando se intenta registrar un cliente
 * cuyo DNI ya existe en el sistema.
 *
 * @author Sebas
 */

package exceptions;

public class ClienteDuplicadoException extends RuntimeException {
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}