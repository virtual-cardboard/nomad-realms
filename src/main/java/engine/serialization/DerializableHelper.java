package engine.serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class DerializableHelper {

    public static void write(boolean v, DataOutputStream dos) throws IOException { dos.writeBoolean(v); }
    public static void write(byte v, DataOutputStream dos) throws IOException { dos.writeByte(v); }
    public static void write(short v, DataOutputStream dos) throws IOException { dos.writeShort(v); }
    public static void write(char v, DataOutputStream dos) throws IOException { dos.writeChar(v); }
    public static void write(int v, DataOutputStream dos) throws IOException { dos.writeInt(v); }
    public static void write(long v, DataOutputStream dos) throws IOException { dos.writeLong(v); }
    public static void write(float v, DataOutputStream dos) throws IOException { dos.writeFloat(v); }
    public static void write(double v, DataOutputStream dos) throws IOException { dos.writeDouble(v); }

    public static boolean readBoolean(DataInputStream dis) throws IOException { return dis.readBoolean(); }
    public static byte readByte(DataInputStream dis) throws IOException { return dis.readByte(); }
    public static short readShort(DataInputStream dis) throws IOException { return dis.readShort(); }
    public static char readChar(DataInputStream dis) throws IOException { return dis.readChar(); }
    public static int readInt(DataInputStream dis) throws IOException { return dis.readInt(); }
    public static long readLong(DataInputStream dis) throws IOException { return dis.readLong(); }
    public static float readFloat(DataInputStream dis) throws IOException { return dis.readFloat(); }
    public static double readDouble(DataInputStream dis) throws IOException { return dis.readDouble(); }

    public static void write(String s, DataOutputStream dos) throws IOException {
        if (s == null) {
            dos.writeBoolean(false);
        } else {
            dos.writeBoolean(true);
            dos.writeUTF(s);
        }
    }

    public static String readString(DataInputStream dis) throws IOException {
        return dis.readBoolean() ? dis.readUTF() : null;
    }

    public static void write(byte[] bytes, DataOutputStream dos) throws IOException {
        if (bytes == null) {
            dos.writeInt(-1);
        } else {
            dos.writeInt(bytes.length);
            dos.write(bytes);
        }
    }

    public static byte[] readBytes(DataInputStream dis) throws IOException {
        int len = dis.readInt();
        if (len == -1) return null;
        byte[] bytes = new byte[len];
        dis.readFully(bytes);
        return bytes;
    }

    public static void writeId(DataOutputStream dos, int id, int subclassCount) throws IOException {
        if (subclassCount <= 256) {
            dos.writeByte(id);
        } else if (subclassCount <= 65536) {
            dos.writeShort(id);
        } else {
            dos.writeInt(id);
        }
    }

    public static int readId(DataInputStream dis, int subclassCount) throws IOException {
        if (subclassCount <= 256) {
            return dis.readUnsignedByte();
        } else if (subclassCount <= 65536) {
            return dis.readUnsignedShort();
        } else {
            return dis.readInt();
        }
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new RuntimeException("Field not found: " + fieldName);
    }

    public static void setField(Object o, String fieldName, Object value) {
        try {
            Field field = findField(o.getClass(), fieldName);
            field.set(o, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(Object o, String fieldName) {
        try {
            Field field = findField(o.getClass(), fieldName);
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object o, String fieldName, Class<?> declaringClass, Object value) {
        try {
            Field field = declaringClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(Object o, String fieldName, Class<?> declaringClass) {
        try {
            Field field = declaringClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
