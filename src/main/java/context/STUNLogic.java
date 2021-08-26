package context;

import static context.input.networking.packet.address.STUNAddress.STUN_ADDRESS;
import static context.input.networking.packet.address.ServerAddress.SERVER_ADDRESS;
import static protocol.STUNProtocol.STUN_REQUEST;

import common.event.GameEvent;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.block.PacketBlock;
import context.logic.GameLogic;
import event.STUNResponseEvent;

public class STUNLogic extends GameLogic {

	@Override
	public void update() {
		STUNData data = (STUNData) getContext().getData();
		if (!data.sentPackets) {
			System.out.println("sending stun packets");
			PacketBlock stunBody = STUN_REQUEST.builder().consume(System.currentTimeMillis()).build();
			PacketModel packet1 = new PacketModel(SERVER_ADDRESS, stunBody);
			PacketModel packet2 = new PacketModel(STUN_ADDRESS, stunBody);
			getContext().sendPacket(packet1);
			getContext().sendPacket(packet2);
			data.sentPackets = true;
			System.out.println("done sending packets");
		}
		while (!getEventQueue().isEmpty()) {
			GameEvent event = getEventQueue().poll();
			if (event instanceof STUNResponseEvent) {
				STUNResponseEvent stunResponseEvent = (STUNResponseEvent) event;
				int server = getServerNumber(data, stunResponseEvent);
				System.out.println("Server " + server + ": " + stunResponseEvent.getAddress());
			} else {
				System.out.println("Unhandled event type: " + event.getClass());
			}
		}

	}

	private int getServerNumber(STUNData data, STUNResponseEvent stunResponseEvent) {
		int server = 0;
		if (data.receivedServer1) {
			if (data.receivedServer2) {
				System.out.println("Got an extra response");
				System.out.println(stunResponseEvent.getAddress());
			} else {
				server = 2;
				data.receivedServer2 = true;
			}
		} else {
			server = 1;
			data.receivedServer1 = true;
		}
		return server;
	}

}
