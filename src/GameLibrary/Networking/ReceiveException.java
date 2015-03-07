package GameLibrary.Networking;

/**
 * An {@code Exception} {@code Object} thrown when one of {@code Networking.Client}'s
 * receives fail
 * 
 * @author Dynisious
 */
public final class ReceiveException extends Exception {

    /**
     * Creates a new {@code ReceiveException} {@code Object}
     * 
     * @param ex The internal {@code Exception} that caused this {@code ReceiveException}
     * @param bytesReceived How many {@code Byte}s where received
     * @param bytesToReceive How many {@code Byte}s should've been received
     */
    public ReceiveException(Exception ex, int bytesReceived, int bytesToReceive) {
        super(String.format("Bytes Received: %d; Bytes To Receive: %d.", bytesReceived, bytesToReceive), ex);
    }

}
