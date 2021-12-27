package networking.protocols;

import static networking.NetworkUtils.PROTOCOL_ID;

import java.lang.reflect.Constructor;
import java.util.function.Function;

import common.event.GameEvent;
import common.source.NetworkSource;
import context.input.event.PacketReceivedInputEvent;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;

public class NomadRealmsProtocolDecoder implements Function<PacketReceivedInputEvent, GameEvent> {

	@SuppressWarnings("unchecked")
	private static final Constructor<? extends NomadRealmsNetworkEvent>[] PROTOCOL_EVENTS = new Constructor[Short.MAX_VALUE];

	static {
		NomadRealmsNetworkProtocols[] values = NomadRealmsNetworkProtocols.values();
		for (short i = 0; i < values.length; i++) {
			NomadRealmsNetworkProtocols value = values[i];
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
		}
	}

	@Override
	public GameEvent apply(PacketReceivedInputEvent event) {
		System.out.println("Received packet from: " + event.source().address());
		PacketReader protocolReader = PROTOCOL_ID.reader(event.model());
		int id = protocolReader.readShort() & 0xFFFF;
		try {
			return PROTOCOL_EVENTS[id].newInstance(event.source(), protocolReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Unknown protocol id " + id);
		return null;
	}
}
