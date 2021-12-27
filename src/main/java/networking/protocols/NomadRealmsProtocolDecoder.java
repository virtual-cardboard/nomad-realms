package networking.protocols;

import static networking.NetworkUtils.PROTOCOL_ID;

import java.lang.reflect.Constructor;
import java.util.function.Function;

import common.event.GameEvent;
import common.source.NetworkSource;
import context.input.event.PacketReceivedInputEvent;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;

public class NomadRealmsProtocolDecoder implements Function<PacketReceivedInputEvent, GameEvent> {

	@SuppressWarnings("unchecked")
	private static final Constructor<? extends NomadRealmsNetworkEvent>[] PROTOCOL_EVENTS = new Constructor[Short.MAX_VALUE];
	public static final PacketFormat[] PROTOCOL_FORMATS = new PacketFormat[Short.MAX_VALUE];

	static {
		NomadRealmsNetworkProtocol[] values = NomadRealmsNetworkProtocol.values();
		for (short i = 0; i < values.length; i++) {
			NomadRealmsNetworkProtocol value = values[i];
			Class<? extends NomadRealmsNetworkEvent> clazz = value.clazz();
			Constructor<? extends NomadRealmsNetworkEvent> constructor = null;
			try {
				constructor = clazz.getConstructor(NetworkSource.class, PacketReader.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			PROTOCOL_EVENTS[value.id()] = constructor;
			PROTOCOL_FORMATS[value.id()] = value.format();
		}
	}

	@Override
	public GameEvent apply(PacketReceivedInputEvent event) {
		System.out.println("Received packet from: " + event.source().address());

		PacketReader idReader = PROTOCOL_ID.reader(event.model());
		int id = idReader.readShort() & 0xFFFF;

		Constructor<? extends NomadRealmsNetworkEvent> constructor = PROTOCOL_EVENTS[id];
		PacketFormat format = PROTOCOL_FORMATS[id];
		PacketReader protocolReader = format.reader(idReader);

		try {
			return constructor.newInstance(event.source(), protocolReader);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Unknown protocol id " + id);
		return null;
	}

}
