package GameLibrary;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
/**
 * <p>
 * Used to serialise and deserialise classes.</p>
 * <p>
 * To be serialised or deserialised they must have a String[] named
 * 'serialisable' containing the names of all serialisable fields.</p>
 *
 * @author Dynisious
 */
public final class Serialisation {
    private static String str = "serialisable"; //The String representing the name of the String[] of serialisable fields.

    /**
     * <p>
     * Returns a {@code int} representing the length of this {@code Class} type
     * in {@code byte}s after serialisation.</p>
     * <p>
     * If obj is null 0 is returned.</p>
     *
     * @param obj The {@code Object} to get the serialised size of.
     *
     * @return Returns a {@code int} representing the length of this Class in
     *         {@code byte}s.
     *
     * @throws java.lang.IllegalAccessException Thrown if this Field object is
     *                                          enforcing Java language access
     *                                          control and the underlying field
     *                                          is inaccessible.
     * @throws java.lang.NoSuchFieldException   Thrown if a field with the
     *                                          specified name is not found.
     */
    public static int getSize(Object obj)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        if (obj == null) { //cls cannot be null.
            return 0;
        }
        Class cls = obj.getClass(); //Get the class of obj.
        String[] fields;
        int size = Integer.BYTES; /*An int representing the size of the array.
         The byte count of the object will always be an int at the head.*/

        if (cls.isArray()) { //This is an array.
            //<editor-fold defaultstate="collapsed" desc="Serialise Array">
            if (cls == boolean.class) { //It's a boolean.
                size += Array.getLength(obj); //Add the serialised length of the boolean[].
            } else if (cls == byte.class) { //It's a byte.
                size += Byte.BYTES * Array.getLength(obj); //Add the serialised length of the byte[].
            } else if (cls == char.class) { //It's a char.
                size += Character.BYTES * Array.getLength(obj); //Add the serialised length of the char[].
            } else if (cls == double.class) { //It's a double.
                size += Double.BYTES * Array.getLength(obj); //Add the serialised length of the double[].
            } else if (cls == float.class) { //It's a  float.
                size += Float.BYTES * Array.getLength(obj); //Add the serialised length of the float[].
            } else if (cls == int.class) { //It's an int.
                size += Integer.BYTES * Array.getLength(obj); //Add the serialised length of the int[]
            } else if (cls == long.class) { //It's a long.
                size += Long.BYTES * Array.getLength(obj); //Add the serialised length of the short[].
            } else if (cls == short.class) { //It's a short.
                size += Short.BYTES * Array.getLength(obj); //Add the serialised length of the short[].
            } else if (cls == String.class) { //It's a String.
                for (String i : (String[]) obj) { //Loop through each String in the String[].
                    size += Integer.BYTES + (i.length() * Character.BYTES); /*
                     Add an int to represent the length of the String and then
                     add the total byte count of all chars.*/

                }
            } else {
                for (Object i : (Object[]) obj) { //Loop through each String in the String[].
                    size += Integer.BYTES + getSize(i); /*
                     Add an int to represent the length of the Object and then
                     add the total byte count of all chars.*/

                }
            }
            //</editor-fold>
        } else { //This is an object.
            //<editor-fold defaultstate="collapsed" desc="Serialise Object">
            try {
                fields = (String[]) cls.getDeclaredField(str).get(obj);
            } catch (NoSuchFieldException | ClassCastException ex) {
                return 0;
            }
            for (String field : fields) { //Loop through every Field.
                Field f = cls.getDeclaredField(field); //The current Field being sized.
                Class type = f.getType(); //Get the type of this Field.
                if (type.isArray()) { //This field is an Array.
                    size += getSize(f.get(obj)); /*Add an int for the byte count
                     of the Array and the size of this Array.*/

                } else {
                    if (type == boolean.class) { //It's a boolean.
                        size += Byte.BYTES;
                    } else if (type == byte.class) { //It's a byte.
                        size += Byte.BYTES;
                    } else if (type == char.class) { //It's a char.
                        size += Character.BYTES;
                    } else if (type == double.class) { //It's a double.
                        size += Double.BYTES;
                    } else if (type == float.class) { //It's a  float.
                        size += Float.BYTES;
                    } else if (type == int.class) { //It's an int.
                        size += Integer.BYTES;
                    } else if (type == long.class) { //It's a long.
                        size += Long.BYTES;
                    } else if (type == short.class) { //It's a short.
                        size += Short.BYTES;
                    } else if (type == String.class) { //It's a String.
                        size += Integer.BYTES
                                + (((String) cls.getDeclaredField(field).
                                get(obj)).
                                length()
                                * Character.BYTES); /*Add the length of the String
                         in bytes after serialisation.*/

                    } else {
                        size += getSize(cls.getDeclaredField(field).get(obj)); //Add the size of this fields type to size.
                    }
                }
            }
            if (cls.getSuperclass() != null) { //This type has a super Class.
                size += getSize(cls.getSuperclass()); //Add on the size of the super Class.
            }
            for (Class i : cls.getInterfaces()) { //Loop through all interfaces for this Class.
                size += getSize(i); //Add on the size of the interface.
            }
//</editor-fold>
        }
        return size; //Return the size of obj in bytes post serialisation.
    }

    /**
     * <p>
     * Serialises an object into an array of bytes</p>
     * <p>
     * if 'obj' is null it will return an empty byte[].</p>
     *
     * @param obj The object which is to be serialised
     *
     * @return Returns an array of type byte which represents the inputed object
     *
     * @throws java.lang.IllegalAccessException Thrown if this Field object is
     *                                          enforcing Java language access
     *                                          control and the underlying field
     *                                          is inaccessible.
     * @throws java.lang.NoSuchFieldException   Thrown if a field with the
     *                                          specified name is not found.
     * @throws GameLibrary.NotSerialisableType  Thrown if 'obj' has not declared
     *                                          a String[] 'serialisable'.
     */
    public static byte[] serialiseObj(Object obj)
            throws IllegalArgumentException, IllegalAccessException,
                   NoSuchFieldException, NotSerialisableType {
        if (obj == null) { //obj is null.
            return new byte[0]; //Return an empty byte[].
        }
        ByteBuffer buff;
        {
            int size = getSize(obj); //Get the size of obj.
            buff = ByteBuffer.allocate(size); //Create a ByteBuffer to store the bytes in.
            buff.putInt(size - Integer.BYTES); //Put an int representing the number of bytes for this object.
        }
        Class cls = obj.getClass();
        if (cls.isArray()) { //obj is an Array.
            //<editor-fold defaultstate="collapsed" desc="Serialise Array">
            buff.putInt(Array.getLength(obj)); //Place the length of the Array in buff.
            if (cls == boolean.class) { //It's a boolean.
                for (boolean i : (boolean[]) obj) { //Loop through each boolean in the array.
                    buff.put((byte) (i ? 1 : 0)); //Put 1 or 0 for true or false.
                }
            } else if (cls == byte.class) { //It's a byte.
                for (byte i : (byte[]) obj) { //Loop through each byte in the array.
                    buff.put(i); //Put the byte into buff.
                }
            } else if (cls == char.class) { //It's a char.
                for (char i : (char[]) obj) { //Loop through each char in the array.
                    buff.putChar(i); //Put the char into buff.
                }
            } else if (cls == double.class) { //It's a double.
                for (double i : (double[]) obj) { //Loop through each double in the array.
                    buff.putDouble(i); //Put the double into buff.
                }
            } else if (cls == float.class) { //It's a  float.
                for (float i : (float[]) obj) { //Loop through each float in the array.
                    buff.putFloat(i); //Put the float into buff.
                }
            } else if (cls == int.class) { //It's an int.
                for (int i : (int[]) obj) { //Loop through each int in the array.
                    buff.putInt(i); //Put the int into buff.
                }
            } else if (cls == long.class) { //It's a long.
                for (long i : (long[]) obj) { //Loop through each long in the array.
                    buff.putLong(i); //Put the long into buff.
                }
            } else if (cls == short.class) { //It's a short.
                for (short i : (short[]) obj) { //Loop through each short in the array.
                    buff.putShort(i); //Put the short into buff.
                }
            } else if (cls == String.class) { //It's a String.
                for (String i : (String[]) obj) { //Loop through each String in the array.
                    buff.putInt(i.length()); //Put an int representing the length of the String.
                    for (char c : i.toCharArray()) { //Loop through each char in the String.
                        buff.putChar(c); //Put the char into the buff.
                    }
                }
            } else {
                for (Object i : (Object[]) obj) { //Loop through each Object in the array.
                    buff.put(serialiseObj(i)); //Put the Object into buff.
                }
            }
            //</editor-fold>
        } else { //obj is not an Array.
            //<editor-fold defaultstate="collapsed" desc="Serialise Object">
            String[] fields;
            try {
                fields = (String[]) cls.getDeclaredField(str).get(obj);
            } catch (NoSuchFieldException ex) {
                throw new NotSerialisableType(
                        "No String[] 'serialisable' was found in "
                        + cls.getName() + ".", ex);
            } catch (ClassCastException ex) {
                throw new ClassCastException(
                        "The field 'serialisable' in " + cls.
                        getName() + " is not type String[].");
            }
            for (String field : fields) { //Loop through every Field.
                Field i = cls.getDeclaredField(field); //Get the type of this Field.
                if (i.getType().isArray()) { //This Field is an Array.
                    byte[] array = serialiseObj(i.get(obj)); //Get the bytes of the Array.
                    buff.putInt(array.length); //Put an int representing the length of the serialised array.
                    buff.put(array); //Put the bytes of the Array into buff.
                } else { //This Field is not an Array.
                    if (i.getType() == boolean.class) { //It's a boolean.
                        buff.put((byte) (i.getBoolean(obj) ? 1 : 0)); //Put a byte representing the boolean into buff.
                    } else if (i.getType() == byte.class) { //It's a byte.
                        buff.put(i.getByte(obj)); //Put the byte into buff.
                    } else if (i.getType() == char.class) { //It's a char.
                        buff.putChar(i.getChar(obj)); //Put the char into buff.
                    } else if (i.getType() == double.class) { //It's a double.
                        buff.putDouble(i.getDouble(obj)); //Put the double into buff.
                    } else if (i.getType() == float.class) { //It's a  float.
                        buff.putFloat(i.getFloat(obj)); //Put the float into buff.
                    } else if (i.getType() == int.class) { //It's an int.
                        buff.putInt(i.getInt(obj)); //Put the int into buff.
                    } else if (i.getType() == long.class) { //It's a long.
                        buff.putLong(i.getLong(obj)); //Put the long into buff.
                    } else if (i.getType() == short.class) { //It's a short.
                        buff.putShort(i.getShort(obj)); //Put the short into buff.
                    } else if (i.getType() == String.class) {
                        char[] chrs = String.valueOf(i).toCharArray(); //Get the array of chars to put into the buff.
                        buff.putInt(chrs.length); //Put the length of chrs into buff.
                        for (char c : chrs) { //Loop through each char in chrs.
                            buff.putChar(c); //Put the char into buff.
                        }
                    } else { //It's an object.
                        buff.put(serialiseObj(i.get(obj))); //Add the bytes of
                    }
                }
            }

            if (cls.getSuperclass() != null) { //This Object has a super Class.
                buff.put(serialiseObj(cls.getSuperclass())); //Add on the buff of the super Class.
            }
            for (Class i : cls.getInterfaces()) { //Loop through all interfaces for this Class.
                buff.put(serialiseObj(i.cast(obj))); //Add on the buff of the interface.
            }
            //</editor-fold>
        }
        return buff.array(); //Return the array of bytes.
    }

    /**
     * <p>
     * Deserialises an array of bytes into an object of type Object.</p>
     *
     * @param bytes The array of bytes to deserialise.
     * @param obj   The Object to serialise the bytes into.
     *
     * @return Returns an Object.
     *
     * @throws java.lang.IllegalAccessException Thrown if this Field object is
     *                                          enforcing Java language access
     *                                          control and the underlying field
     *                                          is inaccessible.
     * @throws GameLibrary.NotSerialisableType  Thrown if 'obj' has not declared
     *                                          a String[] 'serialisable'.
     * @throws java.lang.NoSuchFieldException   Thrown if a field with the
     *                                          specified name is not found.
     */
    public static Object deserialiseObj(byte[] bytes, Object obj)
            throws IllegalArgumentException, IllegalAccessException,
                   NotSerialisableType, NoSuchFieldException {
        ByteBuffer buff = ByteBuffer.wrap(bytes); //Create a ByteBuffer around bytes.
        Class cls = obj.getClass(); //The class object of obj.
        if (cls.isArray()) { //It's an Array type.
            //<editor-fold defaultstate="collapsed" desc="Deserialise Array">
            if (cls == boolean.class) { //It's a boolean.
                boolean[] array = new boolean[buff.getInt()]; //Initialises the new boolean[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = (buff.get() == 0); //Put true or false into array.
                }
                return array; //Return array.
            } else if (cls == byte.class) { //It's a byte.
                byte[] array = new byte[buff.getInt()]; //Initialises the new byte[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.get(); //Put put the byte into array.
                }
                return array; //Return array.
            } else if (cls == char.class) { //It's a char.
                char[] array = new char[buff.getInt()]; //Initialises the new char[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getChar(); //Put the char into array.
                }
                return array; //Return array.
            } else if (cls == double.class) { //It's a double.
                double[] array = new double[buff.getInt()]; //Initialises the new double[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getDouble(); //Put put the double into array.
                }
                return array; //Return array.
            } else if (cls == float.class) { //It's a  float.
                float[] array = new float[buff.getInt()]; //Initialises the new float[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getFloat(); //Put put the float into array.
                }
                return array; //Return array.
            } else if (cls == int.class) { //It's an int.
                int[] array = new int[buff.getInt()]; //Initialises the new int[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getInt(); //Put put the int into array.
                }
                return array; //Return array.
            } else if (cls == long.class) { //It's a long.
                long[] array = new long[buff.getInt()]; //Initialises the new long[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getLong(); //Put put the long into array.
                }
                return array; //Return array.
            } else if (cls == short.class) { //It's a short.
                short[] array = new short[buff.getInt()]; //Initialises the new short[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getShort(); //Put put the short into array.
                }
                return array; //Return array.
            } else if (cls == String.class) { //It's a String.
                String[] array = new String[buff.getInt()]; //Initialises the new String[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    for (int e = 0; e < buff.getInt(); e++) { /*Get an int representing
                         the length of the String and loop and loop to that.*/

                        array[i] += Character.toString(buff.getChar()); //Adds the next char into the String.
                    }
                }
                return array; //Return array.
            } else { //It's an Object.
                Object[] array = new Object[buff.getInt()]; //Initialises the new Object[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    byte[] b = new byte[buff.getInt()]; //Create a byte[] the length of the Object in bytes.
                    buff.get(b); //Get the bytes of the Object
                    array[i] = deserialiseObj(b, cls.cast(new Object())); //Get the next Object.
                }
                return array; //Return array.
            }
            //</editor-fold>
        } else { //obj is not an Array.
            //<editor-fold defaultstate="collapsed" desc="Deserialise Object">
            String[] fields; //The different field names to deserialise.
            try {
                fields = (String[]) cls.getDeclaredField(str).get(obj); //Get the fields to deserialise.
            } catch (NoSuchFieldException ex) {
                throw new NotSerialisableType(
                        "No String[] 'serialisable' was found in "
                        + cls.getName() + ".", ex);
            } catch (ClassCastException ex) {
                throw new ClassCastException(
                        "The field 'serialisable' in " + cls.
                        getName() + " is not type String[].");
            }
            buff.getInt(); //Get the int that representing the length of the bytes of this Object
            for (String field : fields) { //Loop through every Field.
                Field i = cls.getDeclaredField(field); //Get this Field.
                if (i.getType().isArray()) { //This Field is an Array.
                    byte[] array = new byte[buff.getInt()]; //Create a byte[] the length of the bytes of the Array.
                    buff.get(array); //Get the bytes of the Array.
                    i.set(obj, deserialiseObj(array, i.getType().cast(
                            new Object())));
                } else { //This Field is not an Array.
                    if (i.getType() == boolean.class) { //It's a boolean.
                        i.set(obj, (buff.get() == 1)); //Get a byte from buff.
                    } else if (i.getType() == byte.class) { //It's a byte.
                        i.set(obj, buff.get()); //Get a byte from buff.
                    } else if (i.getType() == char.class) { //It's a char.
                        i.set(obj, buff.getChar()); //Get a char from buff.
                    } else if (i.getType() == double.class) { //It's a double.
                        i.set(obj, buff.getDouble()); //Get a double from buff.
                    } else if (i.getType() == float.class) { //It's a  float.
                        i.set(obj, buff.getFloat()); //Get a float from buff.
                    } else if (i.getType() == int.class) { //It's an int.
                        i.set(obj, buff.getInt()); //Get a int from buff.
                    } else if (i.getType() == long.class) { //It's a long.
                        i.set(obj, buff.getLong()); //Get a long from buff.
                    } else if (i.getType() == short.class) { //It's a short.
                        i.set(obj, buff.getShort()); //Get a short from buff.
                    } else if (i.getType() == String.class) {
                        String s = ""; //Create a String to deserialise the String into.
                        for (int e = 0; e < buff.getInt(); e++) { //Loop the length of the String.
                            s += Character.toString(buff.getChar()); //Get the next char.
                        }
                        i.set(obj, s); //Set the String.
                    } else { //It's an object.
                        byte[] b = new byte[buff.getInt()]; //Create a byte[] for the bytes of the Object.
                        buff.get(b); //Get the bytes that represent the Object.
                        i.set(obj, deserialiseObj(b, i.getType().cast(
                                new Object()))); //Get the Object.
                    }
                }
            }

            if (cls.getSuperclass() != null) { //This Object has a super Class.
                byte[] b = new byte[buff.getInt()]; //Create a byte[] the length of the superClass Object.
                buff.get(b); //Get the bytes of the superClass Object.
                deserialiseObj(b, obj); //Set the values of obj as a superclass.
            }
            for (Class i : cls.getInterfaces()) { //Loop through all interfaces for this Class.
                byte[] b = new byte[buff.getInt()]; //Create a byte[] the length of the interface Object.
                buff.get(b); //Get the bytes of the interface Object.
                deserialiseObj(b, obj); //Set the values of obj as a superclass.
            }
            return obj; //Return the object.
            //</editor-fold>
        }
    }

    /**
     * <p>
     * Deserialises an array of bytes into an object of type Object.</p>
     *
     * @param bytes  The array of bytes to deserialise.
     * @param offset The offset in the buffer of the first byte to read.
     * @param length The maximum number of bytes to read from the
     *               buffer.
     * @param obj    The object to deserialise.
     *
     * @return Returns an Object.
     *
     * @throws java.lang.IllegalAccessException Thrown if this Field object is
     *                                          enforcing Java language access
     *                                          control and the underlying field
     *                                          is inaccessible.
     * @throws GameLibrary.NotSerialisableType  Thrown if 'obj' has not declared
     *                                          a String[] 'serialisable'.
     * @throws java.lang.NoSuchFieldException   Thrown if a field with the
     *                                          specified name is not found.
     */
    public static Object deserialiseObj(byte[] bytes, int offset, int length,
                                        Object obj)
            throws IllegalArgumentException, IllegalAccessException,
                   NotSerialisableType, NoSuchFieldException {
        ByteBuffer buff = ByteBuffer.wrap(bytes, offset, length); //Create a ByteBuffer around bytes.
        Class cls = obj.getClass(); //The class object of obj.
        if (cls.isArray()) { //It's an Array type.
            //<editor-fold defaultstate="collapsed" desc="Deserialise Array">
            if (cls == boolean.class) { //It's a boolean.
                boolean[] array = new boolean[buff.getInt()]; //Initialises the new boolean[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = (buff.get() == 0); //Put true or false into array.
                }
                return array; //Return array.
            } else if (cls == byte.class) { //It's a byte.
                byte[] array = new byte[buff.getInt()]; //Initialises the new byte[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.get(); //Put put the byte into array.
                }
                return array; //Return array.
            } else if (cls == char.class) { //It's a char.
                char[] array = new char[buff.getInt()]; //Initialises the new char[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getChar(); //Put the char into array.
                }
                return array; //Return array.
            } else if (cls == double.class) { //It's a double.
                double[] array = new double[buff.getInt()]; //Initialises the new double[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getDouble(); //Put put the double into array.
                }
                return array; //Return array.
            } else if (cls == float.class) { //It's a  float.
                float[] array = new float[buff.getInt()]; //Initialises the new float[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getFloat(); //Put put the float into array.
                }
                return array; //Return array.
            } else if (cls == int.class) { //It's an int.
                int[] array = new int[buff.getInt()]; //Initialises the new int[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getInt(); //Put put the int into array.
                }
                return array; //Return array.
            } else if (cls == long.class) { //It's a long.
                long[] array = new long[buff.getInt()]; //Initialises the new long[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getLong(); //Put put the long into array.
                }
                return array; //Return array.
            } else if (cls == short.class) { //It's a short.
                short[] array = new short[buff.getInt()]; //Initialises the new short[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    array[i] = buff.getShort(); //Put put the short into array.
                }
                return array; //Return array.
            } else if (cls == String.class) { //It's a String.
                String[] array = new String[buff.getInt()]; //Initialises the new String[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    for (int e = 0; e < buff.getInt(); e++) { /*Get an int representing
                         the length of the String and loop and loop to that.*/

                        array[i] += Character.toString(buff.getChar()); //Adds the next char into the String.
                    }
                }
                return array; //Return array.
            } else { //It's an Object.
                Object[] array = new Object[buff.getInt()]; //Initialises the new Object[].
                for (int i = 0; i < array.length; i++) { //Loop until the end of array.
                    byte[] b = new byte[buff.getInt()]; //Create a byte[] the length of the Object in bytes.
                    buff.get(b); //Get the bytes of the Object
                    array[i] = deserialiseObj(b, cls.cast(new Object())); //Get the next Object.
                }
                return array; //Return array.
            }
            //</editor-fold>
        } else { //obj is not an Array.
            //<editor-fold defaultstate="collapsed" desc="Deserialise Object">
            String[] fields; //The different field names to deserialise.
            try {
                fields = (String[]) cls.getDeclaredField(str).get(obj); //Get the fields to deserialise.
            } catch (NoSuchFieldException ex) {
                throw new NotSerialisableType(
                        "No String[] 'serialisable' was found in "
                        + cls.getName() + ".", ex);
            } catch (ClassCastException ex) {
                throw new ClassCastException(
                        "The field 'serialisable' in " + cls.
                        getName() + " is not type String[].");
            }
            buff.getInt(); //Get the int that representing the length of the bytes of this Object
            for (String field : fields) { //Loop through every Field.
                Field i = cls.getDeclaredField(field); //Get this Field.
                if (i.getType().isArray()) { //This Field is an Array.
                    byte[] array = new byte[buff.getInt()]; //Create a byte[] the length of the bytes of the Array.
                    buff.get(array); //Get the bytes of the Array.
                    i.set(obj, deserialiseObj(array, i.getType().cast(
                            new Object())));
                } else { //This Field is not an Array.
                    if (i.getType() == boolean.class) { //It's a boolean.
                        i.set(obj, (buff.get() == 1)); //Get a byte from buff.
                    } else if (i.getType() == byte.class) { //It's a byte.
                        i.set(obj, buff.get()); //Get a byte from buff.
                    } else if (i.getType() == char.class) { //It's a char.
                        i.set(obj, buff.getChar()); //Get a char from buff.
                    } else if (i.getType() == double.class) { //It's a double.
                        i.set(obj, buff.getDouble()); //Get a double from buff.
                    } else if (i.getType() == float.class) { //It's a  float.
                        i.set(obj, buff.getFloat()); //Get a float from buff.
                    } else if (i.getType() == int.class) { //It's an int.
                        i.set(obj, buff.getInt()); //Get a int from buff.
                    } else if (i.getType() == long.class) { //It's a long.
                        i.set(obj, buff.getLong()); //Get a long from buff.
                    } else if (i.getType() == short.class) { //It's a short.
                        i.set(obj, buff.getShort()); //Get a short from buff.
                    } else if (i.getType() == String.class) {
                        String s = ""; //Create a String to deserialise the String into.
                        for (int e = 0; e < buff.getInt(); e++) { //Loop the length of the String.
                            s += Character.toString(buff.getChar()); //Get the next char.
                        }
                        i.set(obj, s); //Set the String.
                    } else { //It's an object.
                        byte[] b = new byte[buff.getInt()]; //Create a byte[] for the bytes of the Object.
                        buff.get(b); //Get the bytes that represent the Object.
                        i.set(obj, deserialiseObj(b, i.getType().cast(
                                new Object()))); //Get the Object.
                    }
                }
            }

            if (cls.getSuperclass() != null) { //This Object has a super Class.
                byte[] b = new byte[buff.getInt()]; //Create a byte[] the length of the superClass Object.
                buff.get(b); //Get the bytes of the superClass Object.
                deserialiseObj(b, obj); //Set the values of obj as a superclass.
            }
            for (Class i : cls.getInterfaces()) { //Loop through all interfaces for this Class.
                byte[] b = new byte[buff.getInt()]; //Create a byte[] the length of the interface Object.
                buff.get(b); //Get the bytes of the interface Object.
                deserialiseObj(b, obj); //Set the values of obj as a superclass.
            }
            return obj; //Return the object.
            //</editor-fold>
        }
    }

    /**
     * Concatenates two arrays of type Object into a single array of type
     * Object.
     *
     * @param first  The first array to be concatenated.
     * @param second The second array to be concatenated.
     *
     * @return Returns an array of type Object.
     */
    public static byte[] concatenateBytes(byte[] first, byte[] second) {
        byte[] a = new byte[first.length + second.length];
        System.arraycopy(first, 0, a, 0, first.length);
        System.arraycopy(second, 0, a, first.length, second.length);
        return a;

    }

}
