package GameLibrary.Networking;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * <p>
 * An Object used to send/receive byte[]s using the network. All Sends and
 * receives through this NetworkClient are synchronous and require an
 * identifying message header, This allows multiple Threads to safely send and
 * receive on this NetworkClient.</p>
 *
 * @author Dynisious
 * @version 0.0.2
 */
public class NetworkClient {
    /**
     * <p>
     * The socket Object which this NetworkClient will use to send and receive
     * on the network.</p>
     */
    private Socket socket;
    public Socket getSocket() {
        return socket;
    }
    /**
     * <p>
     * Sets the Socket for this NetworkClient and returns the previous
     * Socket.</p>
     *
     * @param val The new Socket that this NetworkClient will send and receive
     *            across.
     *
     * @return The previously set Socket.
     */
    public Socket setSocket(Socket val) {
        Socket s = socket;
        socket = val;
        return s;
    }

    /**
     * <p>
     * Creates a new NetworkClient with no Socket
     */
    public NetworkClient() {
    }

    /**
     * <p>
     * Creates a new NetworkClient that will send a receive along the passed
     * socket.</p>
     *
     * @param initSocket The Socket Object that this NetworkClient will send and
     *                   receive on.
     */
    public NetworkClient(Socket initSocket) {
        socket = initSocket;
    }

    /**
     * <p>
     * Sends an int on the Socket for this NetworkClient.</p>
     *
     * @param header The int to send.
     *
     * @throws java.io.IOException Thrown if there was an error sending bytes on
     *                             the Socket.
     */
    public void send(int header) throws IOException {
        synchronized (socket) {
            socket.getOutputStream().write(
                    ByteBuffer.allocate(4).putInt(header).array()); //Send the int.
        }
    }

    /**
     * <p>
     * Sends bytes on the Socket for this NetworkClient.</p>
     *
     * @param message The bytes to send.
     *
     * @throws java.io.IOException Thrown if there was an error sending bytes on
     *                             the Socket.
     */
    public void send(byte[] message) throws IOException {
        synchronized (socket) {
            socket.getOutputStream().write(message); //Send the bytes.
        }
    }

    /**
     * <p>
     * Receives an int on the Socket for this NetworkClient.</p>
     *
     * @return An int received on the Socket.
     *
     * @throws IOException Thrown if there was an error receiving on the Socket.
     */
    public int receiveHeader() throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(4);
        synchronized (socket) {
            socket.getInputStream().read(buff.array());
        }
        return buff.getInt();
    }

    /**
     * <p>
     * Receives a byte[] on the Socket for this NetworkClient.</p>
     *
     * @return The received byte[].
     *
     * @throws IOException Thrown if there was an error receiving bytes on the
     *                     Socket.
     */
    public byte[] receive() throws IOException {
        byte[] buff = new byte[4];
        synchronized (socket) {
            socket.getInputStream().read(buff);
            buff = new byte[ByteBuffer.wrap(buff).getInt()];
            socket.getInputStream().read(buff);
        }
        return buff;
    }
}
