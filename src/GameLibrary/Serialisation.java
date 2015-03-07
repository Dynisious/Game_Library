package GameLibrary;

/**
 * Used to serialise {@code Object}s to and deserialise {@code Object}s from
 * {@code Array}s of {@code byte}s.
 *
 * @author Dynisious
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
public class Serialisation {

    /**
     * Serialises an object into an array of bytes
     *
     * @param obj The object which is to be serialised
     * @return Returns an array of type byte which represents the inputed object
     * @throws IOException Any of the usual input/output exceptions
     */
    public static byte[] serialiseObj(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    /**
     * Serialises an array of objects into a single array of bytes
     *
     * @param obj The objects which is to be serialised
     * @return Returns an array of type byte which represents the inputed object
     * @throws IOException Any of the usual input/output exceptions
     */
    public static byte[] serialiseObjs(Object[] obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        for (Object i : obj) { //Loop through all objects
            o.writeObject(i);
        }
        return b.toByteArray();
    }

    /**
     * Deserialises an array of bytes into an object of type T
     *
     * @param <T> The return type of the method
     * @param bytes The array of bytes to deserialise
     * @return Returns an object of type T
     * @throws IOException Any of the usual input/output exceptions
     * @throws ClassNotFoundException The specified return class was not found
     */
    public static <T> T deserialiseObj(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (T) o.readObject();
    }

    /**
     * Deserialises an array of bytes into an object of type T.
     *
     * @param <T> The return type of the method.
     * @param bytes The array of bytes to deserialise.
     * @param offset The offset in the buffer of the first byte to read.
     * @param length The maximum number of bytes to read from the buffer.
     * @return Returns an object of type T.
     * @throws IOException Any of the usual input/output exceptions.
     * @throws ClassNotFoundException The specified return class was not found.
     */
    public static <T> T deserialiseObj(byte[] bytes, int offset, int length) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes, offset, length);
        ObjectInputStream o = new ObjectInputStream(b);
        return (T) o.readObject();
    }

    /**
     * Deserialises an array of bytes into an array of objects of type T.
     *
     * @param <T> The return type of the method.
     * @param bytes The array of bytes to deserialise.
     * @return Returns an array objects of type T.
     * @throws IOException Any of the usual input/output exceptions.
     * @throws ClassNotFoundException The specified return class was not found.
     */
    public static <T> T[] deserialiseObjs(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        java.util.ArrayList<T> obj = new java.util.ArrayList();
        for (T i = (T) o.readObject(); o.available() != 0;) {
            obj.add(i);
        }
        return (T[]) obj.toArray();
    }

    /**
     * Deserialises an array of bytes into an array of objects of type T.
     *
     * @param <T> The return type of the method.
     * @param bytes The array of bytes to deserialise.
     * @param offset The offset in the buffer of the first byte to read.
     * @param length The maximum number of bytes to read from the buffer.
     * @return Returns an array objects of type T.
     * @throws IOException Any of the usual input/output exceptions.
     * @throws ClassNotFoundException The specified return class was not found.
     */
    public static <T> T[] deserialiseObjs(byte[] bytes, int offset, int length) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes, offset, length);
        ObjectInputStream o = new ObjectInputStream(b);
        java.util.ArrayList<T> obj = new java.util.ArrayList();
        for (T i = (T) o.readObject(); o.available() != 0;) {
            obj.add(i);
        }
        return (T[]) obj.toArray();
    }

    /**
     * Concatenates two arrays of type T into a single array of type T.
     *
     * @param first The first array to be concatenated.
     * @param second The second array to be concatenated.
     * @return Returns an array of type T.
     */
    public static byte[] concatenateBytes(byte[] first, byte[] second) {
        byte[] a = new byte[first.length + second.length];
        System.arraycopy(first, 0, a, 0, first.length);
        System.arraycopy(second, 0, a, first.length, second.length);
        return a;
    }
}
