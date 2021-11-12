package context.game;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectLogic.PEER_ADDRESS;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.CardHoveredEvent;
import event.game.CardPlayedEvent;
import event.network.CardHoveredNetworkEvent;
import event.network.CardPlayedNetworkEvent;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.GameCard;
import model.card.RandomAccessArrayDeque;

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
			} else if (event instanceof CardHoveredEvent) {
				CardHoveredEvent cardHoveredEvent = (CardHoveredEvent) event;
				context().sendPacket(toPacket(cardHoveredEvent.toNetworkEvent(), PEER_ADDRESS));
			} else if (event instanceof CardHoveredNetworkEvent) {
				System.out.println("Opponent hovered");
			} else if (event instanceof CardPlayedNetworkEvent) {
				CardPlayedNetworkEvent cardPlayed = (CardPlayedNetworkEvent) event;
				CardPlayer actor = data.state().cardPlayer(cardPlayed.player());
				System.out.println("Received CardPlayedNetworkEvent");
				if (actor != null) {
					CardPlayer cardPlayer = actor;
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
//					GameCard card = dashboard.hand().drawCard(foundCard);
//					cardPlayed.target();
					System.out.println("Validated info in CardPlayedNetworkEvent");
				} else {
					System.out.println("Card playedBy is not a CardPlayer in CardPlayedNetworkEvent");
				}
			}
		}
		CardDashboard dashboard = data.state().dashboard(data.player());
		RandomAccessArrayDeque<CardPlayedEvent> queue = dashboard.queue();
		if (queue.isEmpty()) {
			return;
		}
		CardPlayedEvent first = queue.getFirst();
	}

	/**
	 * Assumes card is no longer in hand.
	 * 
	 * @param card
	 */
	public void playCard(GameCard card, Actor target) {
//		if(card.) {
//			
//		}
//		dashboard.queue().addBottom(card);
	}

}
