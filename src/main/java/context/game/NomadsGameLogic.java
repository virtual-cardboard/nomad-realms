package context.game;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectLogic.PEER_ADDRESS;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;
import context.game.visuals.gui.CardDashboardGui;
import context.game.visuals.gui.CardGui;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.game.CardHoveredEvent;
import event.game.CardPlayedEvent;
import event.network.CardHoveredNetworkEvent;
import event.network.CardPlayedNetworkEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.CardType;
import model.card.GameCard;
import model.card.RandomAccessArrayDeque;
import model.card.effect.CardEffect;

public class NomadsGameLogic extends GameLogic {

	private NomadsGameData data;
	private NomadsGameVisuals visuals;
	private int tick;

	public NomadsGameLogic(PacketAddress peerAddress) {
	}

	@Override
	protected void init() {
		data = (NomadsGameData) context().data();
		visuals = (NomadsGameVisuals) context().visuals();
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
			} else if (event instanceof CardPlayedEvent) {
				CardPlayedEvent cardPlayedEvent = (CardPlayedEvent) event;
				CardDashboard dashboard = data.state().dashboard(data.player());
				GameCard card = cardPlayedEvent.card();

				// Remove from hand
				int index = dashboard.hand().indexOf(card.id());
				dashboard.hand().delete(index);

				CardDashboardGui dashboardGui = visuals.dashboardGui();
				CardGui cardGui = dashboardGui.hand().removeCardGui(index);
				if (card.type() == CardType.CANTRIP) {
					// Put into discard
					dashboard.discard().addTop(card);
					playCard(cardPlayedEvent);
					dashboardGui.discard().addCardGui(cardGui);
				} else {
					// Put into queue
					RandomAccessArrayDeque<CardPlayedEvent> queue = dashboard.queue();
					queue.offer(cardPlayedEvent);
					data.state().dashboard(data.player()).setQueueResolutionTimeStart(tick);
					dashboardGui.queue().addCardGui(cardGui);
				}
				cardGui.setLockPos(false);
				cardGui.setLockTargetPos(false);
				dashboardGui.resetTargetPositions(visuals.rootGui().dimensions());
			}
		}
		CardDashboard dashboard = data.state().dashboard(data.player());
		RandomAccessArrayDeque<CardPlayedEvent> queue = dashboard.queue();
		while (!queue.isEmpty() && dashboard.timeUntilResolution(tick) <= 0) {
			CardPlayedEvent cardPlayedEvent = queue.poll();
			playCard(cardPlayedEvent);
			CardGui cardGui = visuals.dashboardGui().queue().removeCardGui(0);
			visuals.dashboardGui().discard().addCardGui(cardGui);
			dashboard.setQueueResolutionTimeStart(tick);
			visuals.dashboardGui().discard().resetTargetPositions(visuals.rootGui().dimensions());
		}
		tick++;
	}

	/**
	 * Assumes card is no longer in hand.
	 * 
	 * @param CardPlayedEvent
	 */
	private void playCard(CardPlayedEvent cardPlayedEvent) {
		System.out.println("Played card " + cardPlayedEvent.card().name());
		CardEffect effect = cardPlayedEvent.card().effect();
		if (effect.expression != null) {
			effect.expression.process(cardPlayedEvent.player(), cardPlayedEvent.target(), data.state(), eventQueue());
			System.out.println("Triggered effect!");
		} else {
			System.out.println("Null effect");
		}
	}

}
