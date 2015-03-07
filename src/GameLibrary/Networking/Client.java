package GameLibrary.Networking;

import java.io.IOException;
import java.net.Socket;
/**
 * An {@code Object} used to send/receive {@code Array}s of {@code Byte}s using
 * an {@code Socket}
 *
 * @author Dynisious
 */
public class Client {
    /**
     * This int is used to name an unnamed AdvancedClient and it's Thread.
     */
    private static int idCount;
    /**
     * The {@code Scoket} object that this client utilises to communicate.
     */
    private Socket socket;
    public Socket getSocket() {
        return socket;
    }
    /**
     * A {@code String} value representing the name of the {@code Client}
     */
    private String name;
    /**
     * Gets the name of this {@code Client} object.
     *
     * @return The name of this {@code Client}.
     */
    public String getName() {
        return name;
    }
    /**
     * A {@code InputStream} {@code Object} used to receive {@code Byte}s.
     */
    protected java.io.InputStream in; //An InputStream used to receive data
    /**
     * @return Returns the {@code InputStream} object for this {@code Client}
     */
    protected java.io.InputStream getIn() {
        return in;
    }
    /**
     * A {@code OutputStream} {@code Object} used to send {@code Byte}s
     */
    protected java.io.OutputStream out;
    /**
     * @return Returns the {@code OutputStream} object for this {@code Client}
     */
    protected java.io.OutputStream getOut() {
        return out;
    }

    /**
     * Creates an unnamed, unbound {@code Client} object
     *
     * @param s The socket object the client will communicate through.
     * @throws java.io.IOException Thrown if getting the input and output
     * streams of s fails
     */
    public Client(java.net.Socket s) throws IOException {
        name = "Unnamed Client" + String.valueOf(idCount++);
        socket = s;
        in = s.getInputStream();
        out = s.getOutputStream();
    }

    /**
     * Creates an unnamed, bound {@code Client} {@code Object}
     *
     * @param ip The IP address to connect to
     * @param port The port number to connect to
     * @throws java.net.UnknownHostException
     * @throws java.io.IOException
     */
    public Client(String ip, int port) throws java.net.UnknownHostException,
                                              java.io.IOException {
        socket = new Socket(ip, port);
        name = "Unnamed Client" + String.valueOf(idCount++);
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    /**
     * Creates an named, bound {@code Client} {@code Object}
     *
     * NOTE: nName should not be null or empty.
     *
     * @param ip The IP address to connect to
     * @param port The port number to connect to
     * @param nName The name of the {@code Client}
     * @throws java.net.UnknownHostException
     * @throws java.io.IOException
     */
    public Client(String ip, int port, String nName) throws java.net.UnknownHostException,
                                                            java.io.IOException {
        socket = new Socket(ip, port);
        name = nName;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    /**
     * Receives an {@code Integer} and passes to the calling code.
     *
     * NOTE: This method is thread safe.
     *
     * @return Returns an {@code Integer} received by the {@code Client}
     * @throws GameLibrary.Networking.ReceiveException
     */
    public synchronized int receiveHeader() throws GameLibrary.Networking.ReceiveException {
        byte[] buff = new byte[4]; //A Buffer of 4 Bytes to be converted into an Integer
        byte receivedBytes = 0; //A Byte representing how many Bytes have been received so far
        do {
            try {
                synchronized (in) {
                    receivedBytes += in.read(buff, receivedBytes,
                            4 - receivedBytes);
                }
            } catch (java.io.IOException ex) {
                throw new GameLibrary.Networking.ReceiveException(ex,
                        receivedBytes, 4); //Throw the new error
            }
        } while (receivedBytes < 4); //Loop until an Integer has been received
        return java.nio.ByteBuffer.wrap(buff).getInt();
    }

    /**
     * Returns an {@code Array} of {@code Byte}s after receiving an
     * {@code Integer} specifying how many {@code Bytes} need to be received
     *
     * NOTE: This method is thread safe.
     *
     * @return Returns an {@code Array} of {@code Byte}s received by the
     * {@code Client}
     * @throws GameLibrary.Networking.ReceiveException
     */
    public synchronized byte[] receiveByteArray() throws GameLibrary.Networking.ReceiveException {
        int bytesToReceive = receiveHeader(); //Receive how many Bytes are going to be received
        byte[] buff = new byte[bytesToReceive]; //A buffer for the Bytes received
        int bytesReceived = 0; //How many bytes have been received
        try {
            synchronized (in) {
                do {
                    bytesReceived += in.read(buff, bytesReceived,
                            bytesToReceive - bytesReceived); //Count how many Bytes are received
                } while (bytesReceived < bytesToReceive); //Loop until the Socket has the full message
            }
        } catch (java.io.IOException ex) {
            throw new GameLibrary.Networking.ReceiveException(ex, bytesReceived,
                    bytesToReceive);
        }
        return buff;
    }

    /**
     * Returns an {@code Array} of {@code Byte}s of length
     * {@code bytesToReceive} to the calling code
     *
     * NOTE: This method is thread safe.
     *
     * @param bytesToReceive The count of how many {@code Byte}s to receive
     * @return Returns an {@code Array} of {@code Byte}s received by the
     * {@code Client}
     * @throws GameLibrary.Networking.ReceiveException
     */
    public synchronized byte[] receiveByteArray(int bytesToReceive) throws GameLibrary.Networking.ReceiveException {
        byte[] buff = new byte[bytesToReceive]; //A buffer for the Bytes received
        int bytesReceived = 0; //How many bytes have been received
        try {
            synchronized (in) {
                do {
                    bytesReceived += in.read(buff, bytesReceived,
                            bytesToReceive - bytesReceived); //Count how many Bytes are received
                } while (bytesReceived < bytesToReceive); //Loop until the Socket has the full message
            }
        } catch (java.io.IOException ex) {
            throw new GameLibrary.Networking.ReceiveException(ex, bytesReceived,
                    bytesToReceive);
        }
        return buff;
    }
    
}
