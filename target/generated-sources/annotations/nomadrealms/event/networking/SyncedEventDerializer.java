package nomadrealms.event.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;

public class SyncedEventDerializer implements Derializer<nomadrealms.event.networking.SyncedEvent> {

    public static byte[] serialize(nomadrealms.event.networking.SyncedEvent o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            throw new IllegalArgumentException("No known subclasses for nomadrealms.event.networking.SyncedEvent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static nomadrealms.event.networking.SyncedEvent deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            throw new IllegalArgumentException("No known subclasses for nomadrealms.event.networking.SyncedEvent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
