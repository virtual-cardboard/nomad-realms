package context.misc;

import static java.lang.System.currentTimeMillis;
import static protocol.STUNProtocol.STUN_REQUEST;
import static protocol.address.STUNAddress.STUN_ADDRESS;
import static protocol.address.ServerAddress.SERVER_ADDRESS;

import java.util.Random;

import context.input.networking.packet.PacketModel;
import context.logic.GameLogic;
import event.connect.STUNResponseEvent;

public class STUNLogic extends GameLogic {

	private long nonce1 = new Random().nextLong();
	private long nonce2 = new Random().nextLong();

	@Override
	protected void init() {
		addHandler(STUNResponseEvent.class, event -> {
			int server = -1;
			if (event.getNonce() == nonce1) {
				server = 1;
			}
			if (event.getNonce() == nonce2) {
				server = 2;
			}
			System.out.println("Server " + server + ": " + event.getAddress());
		});
	}

	@Override
	public void update() {
		STUNData data = (STUNData) context().data();
		if (!data.sentPackets) {
			System.out.println("Nonce1: " + nonce1);
			System.out.println("Nonce2: " + nonce2);
			System.out.println("Sending stun packets to " + SERVER_ADDRESS + " and " + STUN_ADDRESS);
			PacketModel packet1 = STUN_REQUEST.builder(SERVER_ADDRESS).consume(currentTimeMillis()).consume(nonce1).build();
			PacketModel packet2 = STUN_REQUEST.builder(STUN_ADDRESS).consume(currentTimeMillis()).consume(nonce2).build();
			context().sendPacket(packet1);
			context().sendPacket(packet2);
			data.sentPackets = true;
			System.out.println("Done sending packets");
		}
	}

}
