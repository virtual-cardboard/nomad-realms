package nomadrealms.context.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;
import nomadrealms.context.game.SomeClass;
import nomadrealms.context.game.FieldClassDerializer;

public class SomeClassDerializer implements Derializer<SomeClass> {

    public static byte[] serialize(SomeClass o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            write(o.getName(), dos);
            write(o.getValue(), dos);
            write(o.getTimestamp(), dos);
            write(o.isActive(), dos);
            write(o.getNested() == null ? null : FieldClassDerializer.serialize(o.getNested()), dos);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SomeClass deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            SomeClass o = new SomeClass();
            o.setName(readString(dis));
            o.setValue(readInt(dis));
            o.setTimestamp(readLong(dis));
            o.setActive(readBoolean(dis));
            byte[] nestedBytes = readBytes(dis);
            o.setNested((nestedBytes == null) ? null : FieldClassDerializer.deserialize(nestedBytes));
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
