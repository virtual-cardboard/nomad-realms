package nomadrealms.context.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;

public class GameStateDerializer implements Derializer<nomadrealms.context.game.GameState> {

    public static byte[] serialize(nomadrealms.context.game.GameState o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            write(o.name(), dos);
            write(((Long) getField(o, "frameNumber", nomadrealms.context.game.GameState.class)), dos);
            write(((Boolean) getField(o, "showMap", nomadrealms.context.game.GameState.class)), dos);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static nomadrealms.context.game.GameState deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            nomadrealms.context.game.GameState o = new nomadrealms.context.game.GameState();
            setField(o, "name", nomadrealms.context.game.GameState.class, readString(dis));
            setField(o, "frameNumber", nomadrealms.context.game.GameState.class, readLong(dis));
            setField(o, "showMap", nomadrealms.context.game.GameState.class, readBoolean(dis));
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
