package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class DerializableHelperTest {

    @Test
    public void testIdSerializationByte() throws IOException {
        int count = 200;
        int id = 150;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DerializableHelper.writeId(new DataOutputStream(bos), id, count);

        byte[] bytes = bos.toByteArray();
        assertEquals(1, bytes.length); // Should use 1 byte

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        int readId = DerializableHelper.readId(dis, count);
        assertEquals(id, readId);
    }

    @Test
    public void testIdSerializationShort() throws IOException {
        int count = 300;
        int id = 290;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DerializableHelper.writeId(new DataOutputStream(bos), id, count);

        byte[] bytes = bos.toByteArray();
        assertEquals(2, bytes.length); // Should use 2 bytes

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        int readId = DerializableHelper.readId(dis, count);
        assertEquals(id, readId);
    }

    @Test
    public void testIdSerializationInt() throws IOException {
        int count = 70000;
        int id = 69000;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DerializableHelper.writeId(new DataOutputStream(bos), id, count);

        byte[] bytes = bos.toByteArray();
        assertEquals(4, bytes.length); // Should use 4 bytes

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        int readId = DerializableHelper.readId(dis, count);
        assertEquals(id, readId);
    }

    @Test
    public void testIdSerializationBoundary256() throws IOException {
        int count = 256;
        int id = 255;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DerializableHelper.writeId(new DataOutputStream(bos), id, count);

        byte[] bytes = bos.toByteArray();
        assertEquals(1, bytes.length); // Should use 1 byte

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        int readId = DerializableHelper.readId(dis, count);
        assertEquals(id, readId);
    }

    @Test
    public void testIdSerializationBoundary257() throws IOException {
        int count = 257;
        int id = 256;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DerializableHelper.writeId(new DataOutputStream(bos), id, count);

        byte[] bytes = bos.toByteArray();
        assertEquals(2, bytes.length); // Should use 2 bytes

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        int readId = DerializableHelper.readId(dis, count);
        assertEquals(id, readId);
    }
}
