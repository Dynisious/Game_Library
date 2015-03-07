package GameLibrary.Networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
/**
 * <p>
 * Extends {@code Client} and provides a sophisticated send loop.</p>
 *
 * @author Dynisious
 */
public class AdvancedClient extends Client {
    /**
     * <p>
     * This ArrayList is used to store the messages to be sent to the remote end
     * point.</p>
     */
    private final ArrayList<byte[]> sendBuffer = new ArrayList<>();
    /**
     * <p>
     * This ArrayList is used to store the error messages to be thrown if
     * sending this message fails.</p>
     */
    private final ArrayList<String> sendErrors = new ArrayList<>();
    /**
     * <p>
     * This Semaphore is used to block the send thread from sending unless there
     * are message(s) to send.</p>
     */
    private final Semaphore sendSemaphore = new Semaphore(0, true);
    /**
     * <p>
     * This boolean is used to determine whether Thread running sendLoop should
     * continue looping until all messages are sent before checking whether the
     * AdvancedClient is closing or whether it should check whether the
     * AdvancedClient is closing after every message sent.</p>
     */
    private boolean closeCheck = false;
    /**
     * <p>
     * This boolean is used to control whether the client should begin closing
     * or continue sending and receiving.</p>
     */
    private boolean runClient = true;
    /**
     * <p>
     * This boolean is used to indicate whether the client is alive and
     * sending/receiving.</p>
     */
    private boolean clientAlive = true;
    /**
     * <p>
     * Get the {@code boolean} representing whether the client is alive and
     * sending/receiving.</p>
     *
     * @return The {@code boolean} representing whether the client is alive.
     */
    public boolean getClientAlive() {
        return clientAlive;
    }

    /**
     * <p>
     * Creates an unnamed {@code AdvancedClient} connected which talks through
     * the passed {@code Socket}.</p>
     *
     * @param s The {@code Socket} that the AdvancedClient will talk through.
     * @throws IOException Thrown if getting the input and output streams of s
     * fails.
     */
    public AdvancedClient(Socket s) throws IOException {
        super(s);
    }

    /**
     * <p>
     * Creates an unnamed {@code AdvancedClient} connected to the specified ip
     * and port.</p>
     *
     * @param ip The remote IP address to connect to.
     * @param port The remote Port to connect to.
     * @throws UnknownHostException Thrown if the AdvancedClient is unable to
     * connect to the specified address and port.
     * @throws IOException Thrown if getting the input and output streams of s
     * fails.
     */
    public AdvancedClient(String ip, int port) throws UnknownHostException, IOException {
        super(ip, port);
    }

    /**
     * <p>
     * Creates an {@code AdvancedClient} connected to the specified ip and
     * port.</p>
     * <p>
     * WARNING: Name should not be null or empty.</p>
     *
     * @param ip The remote IP address to connect to.
     * @param port The remote Port to connect to.
     * @param nName The name given to this {@code AdvancedClient}.
     * @throws UnknownHostException Thrown if the AdvancedClient is unable to
     * connect to the specified address and port.
     * @throws IOException Thrown if getting the input and output streams of s
     * fails.
     */
    public AdvancedClient(String ip, int port, String nName) throws UnknownHostException, IOException {
        super(ip, port, nName);
    }

    /**
     * <p>
     * Adds a message and an error message to the lists inside this
     * {@code AdvancedClient} so that they will be sent to the remote end
     * point.</p>
     * <p>
     * WARNING!!! Error cannot be null or empty.</p>
     * <p>
     * NOTE: This method is {@code Thread} safe.</p>
     *
     * @param message The {@code byte}s to be sent to the remote end point.
     * @param error The error message to be displayed if a send fails while this
     * message is being sent.
     * @throws java.lang.Exception Thrown if the {@code error} parameter was
     * null or empty.
     */
    public synchronized void sendMessage(byte[] message, String error) throws Exception {
        if ((!"".equals(error)) && (error != null)) { //error is not null
            synchronized (sendBuffer) { //Lock access to sendBuffer
                synchronized (sendErrors) { //Lock access to sendErrors
                    sendBuffer.add(message); //Add the message to the message buffer
                    sendErrors.add(error); //Add the error message to the error buffer
                    sendSemaphore.release(); //Release a permit to sendThread
                }
            }
        } else { //error is null or empty
            Exception ex = new Exception(
                    "ERROR : The error paramiter was null or empty."); //Create a new Exception
            ex.setStackTrace(Thread.currentThread().getStackTrace()); //Set the stack trace of the error
            throw ex; //Throw the exception
        }
    }

    /**
     * <p>
     * Adds a message and an error message to the lists inside this
     * {@code AdvancedClient} so that they will be sent to the remote end
     * point.</p>
     * <p>
     * NOTE: This method is {@code Thread} safe.
     *
     * @param message The {@code byte}s to be sent to the remote end point
     */
    public synchronized void sendMessage(byte[] message) {
        synchronized (sendBuffer) { //Lock access to sendBuffer
            synchronized (sendErrors) { //Lock access to sendErrors
                sendBuffer.add(message); //Add the message to the message buffer
                sendErrors.add(
                        "ERROR: There was an error while attempting to send a message on " + super.
                        getName()); //Add the error message to the error buffer
                sendSemaphore.release(); //Release a permit to sendThread
            }
        }
    }

    /**
     * <p>
     * Call this method to begin sending pending messages on this
     * {@code AdvancedClient}.</p>
     * <p>
     * NOTE: A call to this method captures the calling thread in a loop until
     * the {@code AdvancedClient} closes.</p>
     *
     * @throws java.io.IOException Thrown when there is an error while sending a
     * message.
     * @throws java.lang.InterruptedException Thrown when there is an error
     * while waiting for a send request.
     */
    public final void sendLoop() throws IOException, InterruptedException {
        do {
            do {
                try {
                    if (sendSemaphore.tryAcquire(2, TimeUnit.SECONDS)) { //There is a message pending sending
                        synchronized (sendBuffer) { //Lock access to sendBuffer
                            synchronized (sendErrors) { //Lock access to sendErrors
                                try {
                                    getOut().write(sendBuffer.get(0)); //Send this message
                                } catch (IOException ex) {
                                    throw new IOException(
                                            sendErrors.get(0), ex);
                                }
                                sendBuffer.remove(0); //Remove the message just sent
                                sendErrors.remove(0); //Remove the message just sent
                            }
                        }
                    }
                } catch (InterruptedException ex) {
                    StackTraceElement[] trace = ex.getStackTrace(); //Get the stack trace of this error
                    ex = new InterruptedException(
                            "ERROR : There was an error while waiting for a send request on " + super.
                            getName() + "." + System.getProperty(
                                    "line.separator") + ex.getMessage()); //Create a new InteruptedException with the specific message
                    ex.setStackTrace(trace); //Set the stack strace
                    throw ex; //Throw the exception
                }
            } while (!closeCheck && !sendBuffer.isEmpty()); /* Loop as
             long as there are messages waiting to send and the AdvancedClient
             should not check for closing after each send.*/

        } while (runClient); //Loop as long as the AdvancedClient is alive
    }
}
