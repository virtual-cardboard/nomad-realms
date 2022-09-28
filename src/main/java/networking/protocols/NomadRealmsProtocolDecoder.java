package networking.protocols;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.Function;

import context.input.event.PacketReceivedInputEvent;
import derealizer.SerializationClassGenerator;
import derealizer.SerializationReader;
import engine.common.event.GameEvent;
import event.network.NomadRealmsP2PNetworkEvent;

public class NomadRealmsProtocolDecoder implements Function<PacketReceivedInputEvent, GameEvent> {

	@SuppressWarnings("unchecked")
	private static final Constructor<? extends NomadRealmsP2PNetworkEvent>[] PROTOCOL_EVENTS = new Constructor[Short.MAX_VALUE];

	static {
		for (NomadRealmsP2PNetworkProtocol protocol : NomadRealmsP2PNetworkProtocol.values()) {
			short id = protocol.id();
			if (id == -1) {
				throw new RuntimeException("No id set for P2P Network Event " + protocol.name() + ". " +
						"Set its id in " + NomadRealmsP2PNetworkProtocol.class.getSimpleName() + "'s static block.\n" +
						"E.g. " + protocol.name() + ".id = 137;");
			}
			Class<? extends NomadRealmsP2PNetworkEvent> clazz = protocol.objClass();
			if (clazz == null) {
				throw new RuntimeException("No POJO class defined for P2P Network Event " + protocol.name() + ". " +
						"Try using " + SerializationClassGenerator.class.getSimpleName() + " to generate a POJO class for you.");
			}
			try {
				PROTOCOL_EVENTS[id] = clazz.getConstructor();
			} catch (NoSuchMethodException e) {
				System.err.println("Could not find no-arg constructor in class " + clazz.getSimpleName());
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public NomadRealmsP2PNetworkEvent apply(PacketReceivedInputEvent event) {
		SerializationReader reader = new SerializationReader(event.model().bytes());
		if (event.model().bytes().length < 2) {
			System.out.println("Invalid number of bytes in packet. bytes=" + Arrays.toString(event.model().bytes()));
			return null;
		}
		int protocolId = reader.readShort();

		Constructor<? extends NomadRealmsP2PNetworkEvent> constructor = PROTOCOL_EVENTS[protocolId];

		try {
			NomadRealmsP2PNetworkEvent networkEvent = constructor.newInstance();
			networkEvent.read(reader);
			networkEvent.setSource(event.source());
			return networkEvent;
		} catch (Exception e) {
			System.err.println("Could not create " + constructor.getDeclaringClass().getSimpleName()
					+ " from constructor.");
			e.printStackTrace();
		}

		System.out.println("Unknown protocol id " + protocolId);
		return null;
	}

}
