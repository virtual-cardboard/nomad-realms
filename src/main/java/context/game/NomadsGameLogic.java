package context.game;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectLogic.PEER_ADDRESS;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.network.CardPlayedNetworkEvent;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.GameCard;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;

	public NomadsGameLogic(PacketAddress peerAddress) {
	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();
	}

	@Override
	public void update() {

		while (!eventQueue().isEmpty()) {
			GameEvent event = eventQueue().poll();
			if (event instanceof PeerConnectRequestEvent) {
				PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent();
				context().sendPacket(toPacket(connectResponse, PEER_ADDRESS));
				System.out.println(PEER_ADDRESS + " joined late");
			} else if (event instanceof CardPlayedNetworkEvent) {
				CardPlayedNetworkEvent cardPlayed = (CardPlayedNetworkEvent) event;
				Actor actor = data.state().actor(cardPlayed.playedBy());
				System.out.println("Received CardPlayedNetworkEvent");
				if (actor instanceof CardPlayer) {
					CardPlayer cardPlayer = (CardPlayer) actor;
					CardDashboard dashboard = data.state().dashboard(cardPlayer);
					int foundCard = -1;
					for (int i = 0, size = dashboard.hand().size(); i < size; i++) {
						if (dashboard.hand().card(i).id() == cardPlayed.card()) {
							foundCard = i;
						}
					}
					if (foundCard == -1) {
						System.out.println("Card with id is not in hand in CardPlayedNetworkEvent");
					}
					GameCard card = dashboard.hand().drawCard(foundCard);
					dashboard.queue().addBottom(card);
//					cardPlayed.target();
					System.out.println("Validated info in CardPlayedNetworkEvent");
				} else {
					System.out.println("Card playedBy is not a CardPlayer in CardPlayedNetworkEvent");
				}
			}
		}
	}

}
