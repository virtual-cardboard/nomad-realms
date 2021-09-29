package event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import common.event.GameEvent;
import common.source.GameSource;

public abstract class SerializableEvent extends GameEvent {

	public SerializableEvent(long time, GameSource source) {
		super(time, source);
	}

	public SerializableEvent(GameSource source) {
		super(source);
	}

	public static byte[] convertToBytes(SerializableEvent event) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos)) {
			out.writeObject(event);
			return bos.toByteArray();
		}
	}

	public static SerializableEvent convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInputStream in = new ObjectInputStream(bis)) {
			return (SerializableEvent) in.readObject();
		}
	}

}
