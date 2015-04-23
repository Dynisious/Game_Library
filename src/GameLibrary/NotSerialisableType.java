package GameLibrary;

/**
 * <p>
 * An {@code Exception} thrown when a {@code Serialisation} operation is
 * attempted on an object with no serialisable fields.</p>
 *
 * @author Dynisious
 * @versions 0.0.1
 */
public final class NotSerialisableType extends Exception {

    public NotSerialisableType(String message, Throwable cause) {
        super(message, cause);
    }

}
