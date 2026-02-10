package nomadrealms.event.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import engine.serialization.Derializer;
import static engine.serialization.DerializableHelper.*;
import nomadrealms.event.networking.SyncedEvent;

public class SyncedEventDerializer implements Derializer<SyncedEvent> {

    public static byte[] serialize(SyncedEvent o) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            throw new IllegalArgumentException("No known subclasses for SyncedEvent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SyncedEvent deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             DataInputStream dis = new DataInputStream(bis)) {
            throw new IllegalArgumentException("No known subclasses for SyncedEvent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
