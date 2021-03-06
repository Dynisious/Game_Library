/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLibrary.Serialisation;

import java.lang.reflect.Array; //Used to interact with Arrays during serialisation.
import java.nio.ByteBuffer; //Used to serialise and deserialise the Object into/from arrays of bytes.
import java.lang.reflect.Field; //Used to interact with the fields of Objects during serialisation.
import java.lang.reflect.Modifier;
/**
 * <p>
 * Used to Serialise and Deserialise Objects into/from arrays of bytes with
 * minimal overhead.</p>
 * <p>
 * Recommended for use when sending messages across a network or saving bytes to
 * a file to save on bandwidth or disk space.</p>
 *
 * @author Dynisious 10/05/2015
 * @version 0.0.1
 */
public class Serialiser {

    /**
     * <p>
     * Returns the size in bytes of a String of length 'length', after it has
     * been serialised using StringToBytes.</p>
     *
     * @param length The length of the String in question.
     *
     * @return An int representing the size of a String of length 'length'.
     */
    public static int StringSize(int length) {
        return Integer.BYTES + (length * Character.BYTES); //Return the size of the serialised String of length, length.
    }

    /**
     * <p>
     * Serialises a String into an Array of bytes starting at a specific index
     * which is then returned.</p>
     *
     * @param buff The array of bytes to serialise into.
     * @param pos  The starting position in buff.
     * @param str  The String to be Serialised.
     *
     * @return The array of bytes.
     */
    public static ByteBuffer StringToBytes(byte[] buff, int pos, String str) { //Strings are Serialised in the form of 'String-length, String chars'.
        ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Create a ByteBuffer that will store the bytes starting at the given position.
        b.putInt(str.length()); //Serialise an int representing the length of the String to the start of b.
        for (char c : str.toCharArray()) { //Loop through each char in the String.
            b.putChar(c); //Serialise the char to the next index.
        }
        return b; //Return the array of bytes.
    }

    /**
     * <p>
     * Deserialises a String from an Array of bytes starting at a specific index
     * before returning the result.</p>
     *
     * @param buff The array of bytes to deserialise from.
     * @param pos  The starting position in buff.
     *
     * @return The String and the ByteBuffr used to deserialise it.
     */
    public static DeserialiseResult<String> StringFromBytes(byte[] buff, int pos) { //Strings are Serialised in the form of 'String-length, String chars'.
        ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Create a ByteBuffer that around the bytes of the String.
        char[] chrs = new char[b.getInt()]; //Create a char[] the length of the String to deserialise to hold the characters of the String.
        for (int i = 0; i < chrs.length; i++) { //Loop through each char in the String.
            chrs[i] = b.getChar(); //Deserialise a char from b into chrs.
        }
        return new DeserialiseResult<>(String.valueOf(chrs), b); //Return the result.
    }

    /**
     * <p>
     * Serialises a String into an Array of bytes which is then returned.</p>
     *
     * @param str The String to be Serialised.
     *
     * @return The array of bytes representing the String.
     */
    public static ByteBuffer StringToBytes(String str) { //Strings are Serialised in the form of 'String-length, String chars'.
        return StringToBytes(new byte[StringSize(str.length())], 0, str); //Return the bytes representing the String.
    }

    /**
     * <p>
     * Returns the size in bytes of the passed String[] after it has been
     * serialised using ArrayToBytes.</p>
     *
     * @param str The String[] to get the size of.
     *
     * @return An int representing the size of 'str' after serialisation.
     */
    public static int StringArraySize(String[] str) {
        int size = Integer.BYTES; //The total size of the String[] after serialisation; initially the size of one int representing the Array's length.
        for (String s : str) { //Loop through each String in str.
            size += StringSize(s.length()); //Add the size of the serialised String of length, length.
        }
        return size; //Return the total size.
    }

    /**
     * <p>
     * Returns the size of an Array of type int and length 'length', after it
     * has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the int[].
     *
     * @return An int representing the size of an int[] of length 'length' in
     *         bytes.
     */
    public static int IntArraySize(int length) { //int[]'s are serialised in the form 'int[] length, ints'
        return Integer.BYTES + (Integer.BYTES * length); //Return the size of an int[] of length 'length'.
    }

    /**
     * <p>
     * Returns the size of an Array of type long and length 'length', after it
     * has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the long[].
     *
     * @return An int representing the size of an long[] of length 'length' in
     *         bytes.
     */
    public static int LongArraySize(int length) { //long[]'s are serialised in the form 'long[] length, longs'
        return Integer.BYTES + (Long.BYTES * length); //Return the size of an long[] of length 'length'.
    }

    /**
     * <p>
     * Returns the size of an Array of type double and length 'length', after it
     * has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the double[].
     *
     * @return An int representing the size of an double[] of length 'length' in
     *         bytes.
     */
    public static int DoubleArraySize(int length) { //double[]'s are serialised in the form 'double[] length, doubles'
        return Integer.BYTES + (Double.BYTES * length); //Return the size of an double[] of length 'length'.
    }

    /**
     * <p>
     * Returns the size of an Array of type float and length 'length', after it
     * has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the float[].
     *
     * @return An int representing the size of an float[] of length 'length' in
     *         bytes.
     */
    public static int FloatArraySize(int length) { //float[]'s are serialised in the form 'float[] length, floats'
        return Integer.BYTES + (Float.BYTES * length); //Return the size of an float[] of length 'length'.
    }

    /**
     * <p>
     * Returns the size of an Array of type boolean and length 'length', after
     * it has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the boolean[].
     *
     * @return An int representing the size of a boolean[] of length 'length'
     *         in
     *         bytes.
     */
    public static int BooleanArraySize(int length) { //boolean[]'s are serialised in the form 'boolean[] length, bits'
        return Integer.BYTES + (int) Math.ceil(((double) length) / Byte.SIZE); //Return the size of an boolean[] of length 'length'.
    }

    /**
     * <p>
     * Returns a ByteBuffer storing the bytes representing the passed
     * boolean[].</p>
     *
     * @param buff       The byte[] to serialise 'bool' into.
     * @param buffPos    The position inside 'buff' to start serialisation at.
     * @param boolPos    The position inside of 'bool' to start serialisation
     *                   at.
     * @param boolLength The number of elements to serialise from 'bool'.
     * @param bool       The boolean[] to serialise from.
     *
     * @return The ByteBuffer containing the bytes representing the booleans
     *         serialised.
     *
     * @throws java.lang.IndexOutOfBoundsException Thrown if there isn't enough
     *                                             space in buff to serialise 'bool'.
     */
    public static ByteBuffer BooleanArrayToBytes(
            byte[] buff, int buffPos, int boolPos, int boolLength,
            boolean[] bool) throws IndexOutOfBoundsException { //Booleans are stored as single 1's or 0's in bytes with 8 booleans being stored in a single byte.
        if (boolLength <= bool.length - boolPos) { //The number of iterations to be made does not exceed the number that can be made.
            ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(buffPos); //Create a ByteBuffer to serialise to.
            b.putInt(boolLength); //Serialise an int representing the the number of booleans to be serialised into b.
            for (int i = boolPos; i < (boolPos + boolLength) - 8; i += 8) { //Loop through as many groups of 8 as evenly bit into bool.
                byte bit = 1; //A byte which starts at 1 but will shift left every boolean.
                byte val = 0; //A byte which stores the boolean values for serialisation.
                for (int e = i; e < (i + 8); e++) { //Iterate through the next 8 booleans.
                    if (bool[e]) { //This boolean needs to be set in val.
                        val |= bit; //Set a bit to true in val.
                    }
                    bit <<= 1; //Left shift bit for the next bool.
                }
                b.put(val); //Serialise val into b.
            }
            int remainder = (int) Math.IEEEremainder(boolLength, 8); //Get an int representing the number of booleans left to iterate through.
            byte bit = 1; //A byte which starts at 1 but will shift left every boolean.
            byte val = 0; //A byte which stores the boolean values for serialisation.
            for (int i = boolPos + boolLength - remainder; i < boolPos + boolLength; i++) { //Loop through the last booleans.
                if (bool[i]) { //This boolean needs to be set in val.
                    val |= bit; //Set a bit to true in val.
                }
                bit <<= 1; //Left shift bit for the next bool.
            }
            return b.put(val); //Serialise val into b and then return b.
        } else { //The number of iterations to be made exceeds the maximum possible.
            throw new IndexOutOfBoundsException(
                    "The number of booleans asked to be read from the Array exceeded the maximum that could be read.");
        }
    }

    /**
     * <p>
     * Deserialises a boolean[] from a passed byte[].</p>
     *
     * @param buff The byte[] to deserialise the boolean[] from.
     * @param pos  The position inside 'buff' to start deserialisation at.
     *
     * @return The ByteBuffer containing the bytes representing the booleans
     *         serialised.
     */
    public static DeserialiseResult<boolean[]> BooleanArrayFromBytes(
            byte[] buff, int pos) { //Booleans are stored as single 1's or 0's in bytes with 8 booleans being stored in a single byte.
        ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Wrap a ByteBuffer around buff to deserialise from.
        boolean[] bool = new boolean[b.getInt()]; //Create a boolean[] to store all the booleans that need to be deserialised.
        for (int i = 0; i < bool.length - 8; i += 8) { //Loop through as many groups of 8 as evenly bit into bool.
            byte bit = 1; //A byte which starts at 1 but will shift left every boolean.
            byte val = b.get(); //Deserialise the next byte of values from b.
            for (int e = i; e < (i + 8); e++) { //Iterate through the next 8 booleans.
                bool[e] = (bit & val) != 0; //Assign true or false to this boolean based on the next bit in val.
                bit <<= 1; //Right shift bit for the next bool.
            }
        }
        int remainder = (int) Math.IEEEremainder(bool.length, 8); //Get an int representing the number of booleans left to iterate through.
        byte bit = 1; //A byte which starts at 1 but will shift left every boolean.
        byte val = b.get(); //Deserialise the next byte of values from b.
        for (int i = bool.length - remainder; i < bool.length; i++) { //Loop through the last booleans.
            bool[i] = (bit & val) != 0; //Assign true or false to this boolean based on the next bit in val.
            bit <<= 1; //Left shift bit for the next bool.
        }
        return new DeserialiseResult(bool, b); //Return the result of this deserialise.
    }

    /**
     * <p>
     * Returns the size of an Array of type byte and length 'length', after it
     * has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the byte[].
     *
     * @return An int representing the size of a byte[] of length 'length' in
     *         bytes.
     */
    public static int ByteArraySize(int length) { //byte[]'s are serialised in the form 'byte[] length, bytes'
        return Integer.BYTES + length; //Return the size of an byte[] of length 'length'.
    }

    /**
     * <p>
     * Returns the size of an Array of type short and length 'length', after
     * it has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the short[].
     *
     * @return An int representing the size of a short[] of length 'length'
     *         in
     *         bytes.
     */
    public static int ShortArraySize(int length) { //short[]'s are serialised in the form 'short[] length, bits'
        return Integer.BYTES + (length * Short.BYTES); //Return the size of an short[] of length 'length'.
    }

    /**
     * <p>
     * Returns the size of an Array of type char and length 'length', after
     * it has been serialised using ArrayToBytes.</p>
     *
     * @param length The length of the char[].
     *
     * @return An int representing the size of a char[] of length 'length'
     *         in
     *         bytes.
     */
    public static int CharacterArraySize(int length) { //char[]'s are serialised in the form 'char[] length, bits'
        return Integer.BYTES + (length * Character.BYTES); //Return the size of an char[] of length 'length'.
    }

    /**
     * <p>
     * Serialises 'obj' into 'buff' starting at position 'pos' as an instance of
     * 'cls'.</p>
     *
     * @param cls  The Class type of 'obj'.
     * @param buff The Array of bytes to serialise to.
     * @param pos  The position in buff to start at.
     * @param obj  The Array to be serialised.
     *
     * @return The bytes representing the Array with 'obj' serialised to it.
     *
     * @throws java.lang.IllegalAccessException Thrown if 'obj' is not an
     *                                          instance of 'cls'
     */
    public static ByteBuffer ArrayToBytes(
            Class cls, byte[] buff, int pos, Object obj) throws IllegalAccessException { //Arrays are serialised in the form 'Array length, value bytes'.
        if (cls.isInstance(obj)) { //obj is an intance of cls.
            if (cls.isArray()) { //cls is an Array class.
                ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Create a ByteBuffer around buff to serialise to.
                if (cls == String[].class) { //It's a String[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (String s : (String[]) obj) { //Loop through each String.
                        b = StringToBytes(buff, b.position(), s); //Serialise the String and get the ByteBuffer.
                    }
                } else if (cls == int[].class) { //It's an int[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (int i : (int[]) obj) { //Loop through each int.
                        b.putInt(i); //Serialise i into b.
                    }
                } else if (cls == long[].class) { //It's a long[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (long l : (long[]) obj) { //Loop through each long.
                        b.putLong(l); //Serialise i into b.
                    }
                } else if (cls == double[].class) { //It's a double[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (double d : (double[]) obj) { //Loop through each double.
                        b.putDouble(d); //Serialise d into b.
                    }
                } else if (cls == float[].class) { //It's a float[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (float f : (float[]) obj) { //Loop through each float.
                        b.putFloat(f); //Serialise f into b.
                    }
                } else if (cls == boolean[].class) { //It's a boolean[].
                    b = BooleanArrayToBytes(buff, b.position(), 0,
                            Array.getLength(obj), (boolean[]) obj); //Serialise the boolean array into b.
                } else if (cls == byte[].class) { //It's a byte[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    b.put((byte[]) obj); //Serialise obj into b.
                } else if (cls == short[].class) { //It's a short[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (short s : (short[]) obj) { //Loop through each short.
                        b.putShort(s); //Serialise s into b.
                    }
                } else { //It's an Object[].
                    b.putInt(Array.getLength(obj)); //Serialise an int representing the length of the Array to b.
                    for (Object o : (Object[]) obj) { //Loop through each Object in obj
                        b = ObjectToBytes(o.getClass(), buff, b.position(), o,
                                true); //Serialise o into b and get the new ByteBuffer.
                    }
                }
                return b;
            } else { //This is not an Array type
                throw new IllegalAccessException(
                        "The passed Object is not an Array.");
            }
        } else {
            throw new IllegalAccessException(
                    "The passed Object is not an instance of the passed Class.");

        }
    }

    /**
     * <p>
     * Deserialises an Array of type T from an Array of bytes before returning
     * the result.</p>
     *
     * @param cls  The type of Array to deserialise.
     * @param buff The Array of bytes to deserialise from.
     * @param pos  The position in buff to start at.
     *
     * @return The deserialised Array and the ByteBuffer used to deserialise it.
     *
     * @throws java.lang.InstantiationException Thrown if the Class defined by
     *                                          'cls' does not have a nullary constructor.
     * @throws java.lang.IllegalAccessException Thrown if the Class 'cls' or
     *                                          it's nullary constructor cannot
     *                                          be accessed.
     */
    public static DeserialiseResult<?> ArrayFromBytes(
            Class cls, byte[] buff, int pos) throws InstantiationException, IllegalAccessException { //Arrays are serialised in the form 'Array length, value bytes'.
        ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Create a ByteBuffer around buff to deserialise from.
        if (cls == String[].class) { //It's a String[].
            String[] val = new String[b.getInt()]; //Create a String[] to hold each String.
            for (int i = 0; i < val.length; i++) { //Loop through each String.
                DeserialiseResult<String> res = StringFromBytes(buff, b.
                        position()); //Deserialise a String from buff and get the result.
                b = res.buff; //Get the new ByteBuffer.
                val[i] = res.val; //Place the String into val.
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        } else if (cls == int[].class) { //It's an int[].
            int[] val = new int[b.getInt()]; //Create an int[] to hold each int.
            for (int i = 0; i < val.length; i++) { //Loop through each int.
                val[i] = b.getInt(); //Deserialise an int from b.
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        } else if (cls == long[].class) { //It's a long[].
            long[] val = new long[b.getInt()]; //Create a long[] to store each long.
            for (int i = 0; i < val.length; i++) { //Loop through each long.
                val[i] = b.getLong(); //Deserialise a long from b.
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        } else if (cls == double[].class) { //It's a double[].
            double[] val = new double[b.getInt()]; //Create a double[] to store each double.
            for (int i = 0; i < val.length; i++) { //Loop through each double.
                val[i] = b.getDouble(); //Deserialise a double from b.
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        } else if (cls == float[].class) { //It's a float[].
            float[] val = new float[b.getInt()]; //Create a float[] to store each float.
            for (int i = 0; i < val.length; i++) { //Loop through each float.
                val[i] = b.getFloat(); //Deserialise a float from b.
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        } else if (cls == boolean[].class) { //It's a boolean[].
            DeserialiseResult<boolean[]> res = BooleanArrayFromBytes(buff, pos); //Deserialise a boolean[] from buff.
            return new DeserialiseResult<>(res.val, res.buff); //Return the result.
        } else if (cls == byte[].class) { //It's a byte[].
            byte[] val = new byte[b.getInt()]; //Create a byte[] to store each byte.
            b.get(val, 0, val.length); //Deserialise the bytes from b into val.
            return new DeserialiseResult<>(val, b); //Return the result.
        } else if (cls == short[].class) { //It's a short[].
            short[] val = new short[b.getInt()]; //Create a short[] to store each short.
            for (int i = 0; i < val.length; i++) { //Loop through each short.
                val[i] = b.getShort(); //Deserialise a short from b.
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        } else { //It's an Object[].
            Object[] val = new Object[b.getInt()]; //Create a Object[] to store each Object.
            for (int i = 0; i < val.length; i++) { //Loop through each Object.
                if (b.get() == 1) { //This is not a null value.
                    DeserialiseResult res = ObjectFromBytes(cls, buff, b.
                            position(), null, true); //Deserialise a Object from b.
                    b = res.buff; //Get the returned byte buffer.
                    val[i] = res.val; //Get the Object from res.
                }
            }
            return new DeserialiseResult<>(val, b); //Return the result.
        }
    }

    /**
     * <p>
     * Returns the size of 'obj' of type 'cls' after it has been serialised
     * using ObjectToBytes.</p>
     *
     * @param cls   The class type of 'obj'.
     * @param obj   The object to get the size of.
     * @param first A boolean used to indicate whether this is the first call on
     *              'obj' or whether the size of a superClass or interface is
     *              being added. Calls made by user should always be true.
     *
     * @return An int representing the size of 'obj' in bytes.
     *
     * @throws java.lang.IllegalAccessException Thrown if 'obj' is not an
     *                                          instance of 'cls'.
     */
    public static int ObjectSize(Class cls, Object obj, boolean first) throws IllegalAccessException {
        if (obj == null) { //obj is null.
            return Byte.BYTES; //Return one byte size to indicate this is a null value.
        } else { //obj is not null 
            if (cls.isInstance(obj)) { //obj is an instance of the given Class.
                int size = first ? Byte.BYTES : 0; //This int will keep a count of the size of this Object in bytes; initialy one byte to store whether obj is null if this is the first iteration.
                Field[] fields = cls.getDeclaredFields(); //Get the fields for this Object type.
                for (Field f : fields) { //Loop through every field.
                    f.setAccessible(true);
                    Class fCls = f.getType(); //Get the class type for this field.

                    if (fCls.isArray()) { //This field contains an Array.
                        if (fCls == String[].class) { //It's a String[].
                            size += Integer.BYTES; //Add an int to represent the length of this Array.
                            for (String s : (String[]) f.get(obj)) { //Loop through every String in this Field.
                                size += StringSize(s.length()); //Add the size of this String to size.
                            }
                        } else if (fCls == int[].class) { //It's an int[].
                            size += IntArraySize(Array.getLength(f.get(obj))); //Add the size of this Array to size.
                        } else if (fCls == long[].class) { //It's a long[].
                            size += LongArraySize(Array.getLength(obj)); //Adds the size of this Array to size.
                        } else if (fCls == double[].class) { //It's a double[].
                            size += DoubleArraySize(Array.getLength(obj)); //Adds the size of this Array to size.
                        } else if (fCls == float[].class) { //It's a float[].
                            size += FloatArraySize(Array.getLength(obj)); //Adds the size of this Array to size.
                        } else if (fCls == boolean[].class) { //It's a boolean[].
                            size += BooleanArraySize(Array.getLength(obj)); //Adds the size of this Array to size.
                        } else if (fCls == byte[].class) { //It's a byte[].
                            size += ByteArraySize(Array.getLength(obj)); //Adds the size of this Array to size.
                        } else if (fCls == short[].class) { //It's a short[].
                            size += ShortArraySize(Array.getLength(obj)); //Adds the size of this Array to size.
                        } else if (fCls == char[].class) { //It's a char[].
                            size += CharacterArraySize(Array.getLength(obj)); //Adds the size of this array to size.
                        } else { //It's an Object[].
                            size += Integer.BYTES; //Add an int to represent the length of this Array.
                            for (Object o : (Object[]) f.get(obj)) { //Loop through each Object in this Array.
                                size += ObjectSize(fCls.getComponentType(), o,
                                        true); //Add the size of this Object to size.
                            }
                        }
                    } else if (fCls == String.class) { //It's a String.
                        size += StringSize(((String) f.get(obj)).length()); //Add the size of this String to size.
                    } else if (fCls == int.class) { //It's an int.
                        size += Integer.BYTES; //Add the size of an int to size.
                    } else if (fCls == long.class) { //It's a long.
                        size += Long.BYTES; //Add the size of a long to size.
                    } else if (fCls == double.class) { //It's a double.
                        size += Double.BYTES; //Add the size of a double to size.
                    } else if (fCls == float.class) { //It's a float.
                        size += Float.BYTES; //Add the size of a float to size.
                    } else if (fCls == boolean.class || fCls == byte.class) { //It's a boolean or a byte.
                        size += Byte.BYTES; //Add the size of a byte to size.
                    } else if (fCls == short.class) { //It's a short.
                        size += Short.BYTES; //Add the size of a Short to size.
                    } else if (fCls == char.class) { //It's a char.
                        size += Character.BYTES; //Add the size of a char to size.
                    } else { //It's an Object.
                        size += ObjectSize(fCls.getComponentType(), f.get(obj),
                                true); //Add the size of this Object to size.
                    }
                }

                if (cls.getSuperclass() != null) {
                    size += ObjectSize(cls.getSuperclass(), obj, false); //Add the size of obj as an instance of it's superClass
                }

                for (Class c : cls.getInterfaces()) { //Loop through all interfaces used by 'cls'
                    size += ObjectSize(c, obj, false); //Add the size of obj as an instance of 'c'
                }

                return size;
            } else {
                throw new IllegalAccessException(
                        "The passed object is not an instance of the passed Class.");
            }
        }
    }

    /**
     * <p>
     * Serialises 'obj' into buff starting at position 'pos' as an instance of
     * 'cls'.</p>
     *
     * @param cls   The class type of 'obj'.
     * @param buff  The byte[] to serialise into.
     * @param pos   The starting position in buff.
     * @param obj   The object to serialise.
     * @param first A boolean used to indicate whether this is the first call on
     *              'obj' or whether the size of a superClass or interface is
     *              being added. Calls made by user should always be true.
     *
     * @return buff after serialising 'obj' into it.
     *
     * @throws java.lang.IllegalAccessException Thrown if 'obj' is not an
     *                                          instance of 'cls'.
     */
    public static ByteBuffer ObjectToBytes(
            Class cls, byte[] buff, int pos, Object obj, boolean first) throws IllegalAccessException {
        ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Create a ByteBuffer around buff and set the position to pos.
        if (obj == null) { //This is a null value.
            b.put((byte) 0); //Serialise 0 into b to indicate that this is a null value.
            return b; //Return b to the caller.
        } else { //This is not a null value.
            if (cls.isInstance(obj)) { //obj is an instance of the given Class.
                if (cls.isArray()) { //This is an Array type.
                    return ArrayToBytes(cls.getComponentType(), buff, pos, obj); //Return the serialise Array.
                } else { //This is an Object type.
                    if (first) { //Serialise 1 into b to indicate that this is not a null value if it's the first iteration.
                        b.put((byte) 1); //Serialise a byte into b.
                    }
                    Field[] fields = cls.getDeclaredFields(); //Get the fields for this Object type.
                    for (Field f : fields) { //Loop through every field.
                        f.setAccessible(true);
                        Class fCls = f.getType(); //Get the class type for this field.
                        if (fCls.isArray()) { //This field contains an Array.
                            b = ArrayToBytes(fCls, buff, b.position(), f.
                                    get(obj)); //Serialise the Array into buff.

                        } else if (fCls == String.class) { //It's a String[].
                            b = StringToBytes(
                                    buff, b.position(), (String) f.get(obj)); //Serialise the String and get the ByteBuffer.
                        } else if (fCls == int.class) { //It's an int.
                            b.putInt(f.getInt(obj)); //Serialise the int into b.
                        } else if (fCls == long.class) { //It's a long.
                            b.putLong(f.getLong(obj)); //Serialise the long into b.
                        } else if (fCls == double.class) { //It's a double.
                            b.putDouble(f.getDouble(obj)); //Serialise the double into b.
                        } else if (fCls == float.class) { //It's a float.
                            b.putFloat(f.getFloat(obj)); //Serialise the float into b.
                        } else if (fCls == boolean.class) { //It's a boolean.
                            b.put((byte) (f.getBoolean(obj) ? 1 : 0)); //Serialise the boolean into b.
                        } else if (fCls == byte.class) { //It's a byte.
                            b.put(f.getByte(obj)); //Serialise the byte into b.
                        } else if (fCls == short.class) { //It's a short.
                            b.putShort(f.getShort(obj)); //Serialise the short into b.
                        } else { //It's an Object.
                            b.put(f.getByte(obj)); //Serialise the Object into b.
                        }
                    }

                    if (cls.getSuperclass() != null) { //There is a superClass
                        b = ObjectToBytes(cls.getSuperclass(), buff, b.
                                position(), obj, false); //Serialise the object as an instance of it's superClass.
                    }

                    for (Class c : cls.getInterfaces()) { //Loop through all interfaces used by 'cls'
                        b = ObjectToBytes(c, buff, b.position(), obj, false); //Serialise the object as an instance of it's interface.
                    }

                    return b;
                }
            } else {
                throw new IllegalAccessException(
                        "The passed Object is not an instance of the passed Class.");
            }
        }
    }

    /**
     * <p>
     * Deserialises an instance of 'cls' from 'buff' starting at position
     * 'pos'.</p>
     *
     * @param cls   The class type to deserialise.
     * @param buff  The byte[] to deserialise from.
     * @param pos   The starting position in buff.
     * @param obj   The Object to deserialise values into, if null a new
     *              instance
     *              of 'cls' will be created.
     * @param first A boolean used to indicate whether this is the first call on
     *              'obj' or whether the size of a superClass or interface is
     *              being added. Calls made by user should always be true.
     *
     * @return The deserialised Object and the ByteBuffer used to deserialise
     *         it.
     *
     * @throws java.lang.InstantiationException Thrown if the Class defined by
     *                                          'cls' does not have a nullary constructor.
     * @throws java.lang.IllegalAccessException Thrown if the Class 'cls' or
     *                                          it's nullary constructor cannot
     *                                          be accessed.
     * @throws java.lang.ClassCastException     Thrown if 'obj' is not an
     *                                          instance of 'cls'.
     */
    public static DeserialiseResult<?> ObjectFromBytes(
            Class cls, byte[] buff, int pos, Object obj, boolean first) throws InstantiationException, IllegalAccessException, ClassCastException {
        ByteBuffer b = (ByteBuffer) ByteBuffer.wrap(buff).position(pos); //Create a ByteBuffer around buff and set the position to pos.
        if (cls.isArray()) { //This is an Array type.
            return ArrayFromBytes(cls, buff, pos); //Return the deserialised Array.
        } else { //This is an Object type.
            if (first) { //This is the first iteration so check for null obj.
                if (b.get() == 1) { //This instance is is a non null value.
                    if (obj == null) { //obj needs to have a value to be deserialised to.
                        try {
                            obj = cls.newInstance(); //Create a new Instance of cls to deserialise values to.
                        } catch (InstantiationException ex) {
                            throw new InstantiationException(
                                    "The passed class does not have a contructor with no parameters.");
                        }
                    }
                } else { //This is a null value.
                    return new DeserialiseResult<>(null, b); //Return a null result.
                }
            }

            if (!cls.isInstance(obj)) { //obj is not an instance of cls.
                throw new ClassCastException(
                        "The passed Object is not an instance of the passed Class."); //Throw an exception to the calling code.
            } else {
                Field[] fields = cls.getDeclaredFields(); //Get the fields for this Object type.
                for (Field f : fields) { //Loop through every field.
                    if (!Modifier.isFinal(f.getModifiers())
                            || !Modifier.isStatic(f.getModifiers())) { //This field is not 'static final'.
                        f.setAccessible(true);
                        Class fCls = f.getType(); //Get the class type for this field.
                        if (fCls.isArray()) { //This field contains an Array.
                            DeserialiseResult res = ArrayFromBytes(fCls, buff,
                                    b.
                                    position()); //Deserialise the Array from buff and get the result.
                            b = res.buff; //Set b to the updated ByteBuffer.
                            f.set(obj, res.val); //Set the value in obj.
                        } else if (fCls == String.class) { //It's a String[].
                            DeserialiseResult res = StringFromBytes(buff, b.
                                    position()); //Deserialise the String from buff and get the result.
                            b = res.buff; //Set b to the updated ByteBuffer.
                            f.set(obj, res.val); //Set the value in obj.
                        } else if (fCls == int.class) { //It's an int.
                            f.set(obj, b.getInt()); //Deserialise an int from b into obj.
                        } else if (fCls == long.class) { //It's a long.
                            f.set(obj, b.getLong()); //Deserialise a long from b into obj.
                        } else if (fCls == double.class) { //It's a double.
                            f.set(obj, b.getDouble()); //Deserialise an double from b into obj.
                        } else if (fCls == float.class) { //It's a float.
                            f.set(obj, b.getFloat()); //Deserialise an float from b into obj.
                        } else if (fCls == boolean.class) { //It's a boolean.
                            f.set(obj, (b.get() == 1)); //Deserialise an boolean from b into obj.
                        } else if (fCls == byte.class) { //It's a byte.
                            f.set(obj, b.get()); //Deserialise an byte from b into obj.
                        } else if (fCls == short.class) { //It's a short.
                            f.set(obj, b.getShort()); //Deserialise an short from b into obj.
                        } else { //It's an Object.
                            DeserialiseResult res = ObjectFromBytes(fCls, buff,
                                    b.
                                    position(), null, true); //Deserialise an Object from b and get the result.
                            b = res.buff; //Set b to the updated ByteBuffer.
                            f.set(obj, res.val); //Set the deserialised Object in obj.
                        }
                    }
                }

                if (cls.getSuperclass() != null) { //There is a superClass
                    DeserialiseResult res = ObjectFromBytes(cls.
                            getSuperclass(),
                            buff, b.position(), obj, false); //Deserialise the values for obj's superClass into obj.
                    b = res.buff; //Set b to the new updated ByteBuffer.
                    obj = res.val; //Set obj to the updated Object.
                }

                for (Class c : cls.getInterfaces()) { //Loop through all interfaces used by 'cls'
                    DeserialiseResult res = ObjectFromBytes(c, buff, b.
                            position(), obj, false); //Deserialise the values for obj's interface into obj.
                    b = res.buff; //Set b to the new updated ByteBuffer.
                    obj = res.val; //Set obj to the updated Object.
                }

                return new DeserialiseResult<>(obj, b); //Return the result.
            }
        }
    }

    /**
     * This Class is returned as the result for a deserialisation. It
     * contains
     * both the deserialised value and the ByteBuffer that was used to
     * deserialise it.
     *
     * @author dynisious
     * @param <T> The type of value that has been deserialised.
     *
     * @since 13/05/2015 00:26
     * @version 0.0.0.1
     */
    public static class DeserialiseResult<T> {
        public T val; //The value that has been deserialised.
        public ByteBuffer buff; //The byte buffer that was used to deserialise the contained value.

        public DeserialiseResult(T nVal, ByteBuffer nBuff) {
            val = nVal;
            buff = nBuff;
        }
    }
}
