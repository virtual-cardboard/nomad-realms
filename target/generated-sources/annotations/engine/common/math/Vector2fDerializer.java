package engine.common.math;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;
import engine.common.math.Vector2f;

public class Vector2fDerializer implements Derializer<Vector2f> {

    public static byte[] serialize(Vector2f o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            write(o.x(), dos);
            write(o.y(), dos);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vector2f deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            Vector2f o = new Vector2f();
            setField(o, "x", Vector2f.class, readFloat(dis));
            setField(o, "y", Vector2f.class, readFloat(dis));
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
