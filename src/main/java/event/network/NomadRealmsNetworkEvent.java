package event.network;

import static context.input.networking.packet.PacketPrimitive.BYTE_ARRAY;
import static networking.NetworkUtils.PROTOCOL_ID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import event.game.NomadRealmsGameEvent;
import networking.protocols.ProtocolID;

public abstract class NomadRealmsNetworkEvent extends NomadRealmsGameEvent implements Serializable {

	private static final long serialVersionUID = -962322129401076013L;
	private static final PacketFormat SERIALIZED_FORMAT = new PacketFormat().with(BYTE_ARRAY);

	public NomadRealmsNetworkEvent(NetworkSource source) {
		super(source);
	}

	public NomadRealmsNetworkEvent(long time, NetworkSource source) {
		super(time, source);
	}

	@Override
	public NetworkSource source() {
		return (NetworkSource) super.source();
	}

	public PacketModel toPacket(PacketAddress address) {
		PacketBuilder builder = PROTOCOL_ID.builder(address).consume(protocolID().id());
		return toPacketModel(builder);
	}

	protected abstract PacketModel toPacketModel(PacketBuilder builder);

	protected abstract ProtocolID protocolID();

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
