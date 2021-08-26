package context;

import static context.input.networking.packet.address.STUNAddress.STUN_ADDRESS;
import static context.input.networking.packet.address.ServerAddress.SERVER_ADDRESS;
import static java.lang.System.currentTimeMillis;
import static protocol.STUNProtocol.STUN_REQUEST;

import java.util.Random;

import common.event.GameEvent;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.block.PacketBlock;
import context.logic.GameLogic;
import event.STUNResponseEvent;

public class STUNLogic extends GameLogic {

	private long nonce1 = new Random().nextLong();
	private long nonce2 = new Random().nextLong();

	@Override
	public void update() {
		STUNData data = (STUNData) getContext().getData();
		if (!data.sentPackets) {
			System.out.println("Nonce1: " + nonce1);
			System.out.println("Nonce2: " + nonce2);
			System.out.println("Sending stun packets to " + SERVER_ADDRESS + " and " + STUN_ADDRESS);
			PacketBlock stunBody1 = STUN_REQUEST.builder().consume(currentTimeMillis()).consume(nonce1).build();
			PacketBlock stunBody2 = STUN_REQUEST.builder().consume(currentTimeMillis()).consume(nonce2).build();
			PacketModel packet1 = new PacketModel(SERVER_ADDRESS, stunBody1);
			PacketModel packet2 = new PacketModel(STUN_ADDRESS, stunBody2);
			getContext().sendPacket(packet1);
			getContext().sendPacket(packet2);
			data.sentPackets = true;
			System.out.println("Done sending packets");
		}
		while (!getEventQueue().isEmpty()) {
			GameEvent event = getEventQueue().poll();
			if (event instanceof STUNResponseEvent) {
				STUNResponseEvent stunResponseEvent = (STUNResponseEvent) event;
				int server = -1;
				if (stunResponseEvent.getNonce() == nonce1) {
					server = 1;
				}
				if (stunResponseEvent.getNonce() == nonce2) {
					server = 2;
				}
				System.out.println("Server " + server + ": " + stunResponseEvent.getAddress());
			} else {
				System.out.println("Unhandled event type: " + event.getClass());
			}
		}

	}

}
