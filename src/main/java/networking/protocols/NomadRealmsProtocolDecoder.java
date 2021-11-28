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
	private static final Class<? extends NomadRealmsNetworkEvent>[] PROTOCOL_EVENTS = new Class[Short.MAX_VALUE];

	static {
		ProtocolID[] values = ProtocolID.values();
		for (short i = 0; i < values.length; i++) {
			ProtocolID value = values[i];
			PROTOCOL_EVENTS[value.id()] = value.clazz();
		}
	}

	@Override
	public GameEvent apply(PacketReceivedInputEvent event) {
		System.out.println("Received packet from: " + event.source().address());
		PacketReader protocolReader = PROTOCOL_ID.reader(event.model());
		short id = protocolReader.readShort();
		try {
			Constructor<? extends NomadRealmsNetworkEvent> constructor = PROTOCOL_EVENTS[id].getConstructor(NetworkSource.class, PacketReader.class);
			return constructor.newInstance(event.source(), protocolReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Unknown protocol id " + id);
		return null;
	}

}
