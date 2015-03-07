package GameLibrary.GameObjects;

/**
 * An {@code Exception} {@code Object} thrown when a GameLibrary.GameObjects Stat
 * is set to a value outside it's bounds and is left un-handled
 * 
 * @author Dynisious
 */
public final class OutOfRangeException extends java.lang.Exception {

    public OutOfRangeException(String errorMessage) {
        super(errorMessage);
    }

}
