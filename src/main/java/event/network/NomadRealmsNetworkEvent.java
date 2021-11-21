package event.network;

import static context.input.networking.packet.PacketPrimitive.BYTE_ARRAY;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import common.source.GameSource;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import event.game.NomadRealmsGameEvent;

public abstract class NomadRealmsNetworkEvent extends NomadRealmsGameEvent implements Serializable {

	private static final long serialVersionUID = -962322129401076013L;
	private static final PacketFormat SERIALIZED_FORMAT = new PacketFormat().with(BYTE_ARRAY);

	public NomadRealmsNetworkEvent(GameSource source) {
		super(source);
	}

	public NomadRealmsNetworkEvent(long time, GameSource source) {
		super(time, source);
	}

	public static byte[] toBytes(NomadRealmsNetworkEvent event) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos)) {
			out.writeObject(event);
			return bos.toByteArray();
		}
	}

	public static NomadRealmsNetworkEvent fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInputStream in = new ObjectInputStream(bis)) {
			return (NomadRealmsNetworkEvent) in.readObject();
		}
	}

	public static PacketModel toPacket(NomadRealmsNetworkEvent event, PacketAddress dest) {
		byte[] bytes = null;
		try {
			bytes = toBytes(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SERIALIZED_FORMAT
				.builder(dest)
				.consume(bytes)
				.build();
	}

	public static NomadRealmsNetworkEvent fromPacket(PacketModel packet) {
		byte[] bytes = SERIALIZED_FORMAT.reader(packet).readByteArray();
		NomadRealmsNetworkEvent event = null;
		try {
			event = fromBytes(bytes);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Packet event data is not correctly formatted");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return event;
	}

}
