/**
 * Excepción lanzada al intentar operar con un empleado inexistente.
 *
 * @author Sebas
 */

package exceptions;

public class EmpleadoNoEncontradoException extends RuntimeException {
    public EmpleadoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}