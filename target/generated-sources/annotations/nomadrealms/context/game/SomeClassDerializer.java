package nomadrealms.context.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;

public class SomeClassDerializer implements Derializer<nomadrealms.context.game.SomeClass> {

    public static byte[] serialize(nomadrealms.context.game.SomeClass o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            write(o.getName(), dos);
            write(o.getValue(), dos);
            write(o.getTimestamp(), dos);
            write(o.isActive(), dos);
            write(o.getNested() == null ? null : nomadrealms.context.game.FieldClassDerializer.serialize(o.getNested()), dos);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static nomadrealms.context.game.SomeClass deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            nomadrealms.context.game.SomeClass o = new nomadrealms.context.game.SomeClass();
            o.setName(readString(dis));
            o.setValue(readInt(dis));
            o.setTimestamp(readLong(dis));
            o.setActive(readBoolean(dis));
            byte[] nestedBytes = readBytes(dis);
            o.setNested((nestedBytes == null) ? null : nomadrealms.context.game.FieldClassDerializer.deserialize(nestedBytes));
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
