package nomadrealms.context.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;
import nomadrealms.context.game.GameState;

public class GameStateDerializer implements Derializer<GameState> {

    public static byte[] serialize(GameState o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            write(o.name(), dos);
            write(((Long) getField(o, "frameNumber", GameState.class)), dos);
            write(((Boolean) getField(o, "showMap", GameState.class)), dos);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameState deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            GameState o = new GameState();
            setField(o, "name", GameState.class, readString(dis));
            setField(o, "frameNumber", GameState.class, readLong(dis));
            setField(o, "showMap", GameState.class, readBoolean(dis));
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
