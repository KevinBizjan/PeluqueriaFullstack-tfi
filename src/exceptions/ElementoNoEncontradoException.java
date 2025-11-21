/**
 * Excepción lanzada al intentar operar con un elemento inexistente.
 *
 * @author Sebas
 */

package exceptions;

public class ElementoNoEncontradoException extends RuntimeException {
    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
