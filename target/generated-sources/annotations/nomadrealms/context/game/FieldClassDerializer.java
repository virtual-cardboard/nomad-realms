package nomadrealms.context.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;
import nomadrealms.context.game.FieldClass;

public class FieldClassDerializer implements Derializer<FieldClass> {

    public static byte[] serialize(FieldClass o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            write(o.getId(), dos);
            write(o.getDescription(), dos);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FieldClass deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            FieldClass o = new FieldClass();
            o.setId(readInt(dis));
            o.setDescription(readString(dis));
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
