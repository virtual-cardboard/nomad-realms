package context.game.logic.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import context.game.NomadsGameLogic;
import engine.common.event.async.AsyncEventPriorityQueue;
import engine.common.math.Vector2i;
import event.logicprocessing.CardPlayedEvent;
import event.logicprocessing.CardResolvedAsyncEvent;
import event.network.NomadRealmsP2PNetworkEvent;
import math.WorldPos;
import model.actor.health.cardplayer.CardPlayer;
import model.actor.health.Structure;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.chain.EffectChain;
import model.chain.event.ChainEvent;
import model.state.GameState;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	/** The delay, in ticks, before a cantrip or task card resolves */
	public static final int RESOLUTION_DELAY = 10;

	private final NomadsGameData data;
	private final NomadsGameLogic logic;
	private final AsyncEventPriorityQueue asyncEventQueue;
	private final Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkEvents;

	public CardPlayedEventHandler(NomadsGameData data, NomadsGameLogic logic, AsyncEventPriorityQueue asyncEventQueue,
	                              Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkEvents) {
		this.data = data;
		this.logic = logic;
		this.asyncEventQueue = asyncEventQueue;
		this.outgoingNetworkEvents = outgoingNetworkEvents;
	}

	/**
	 * If the card is a cantrip, then it resolves immediately and is handled by {@link CardResolvedEventHandler}.
	 */
	@Override
	public void accept(CardPlayedEvent event) {
		GameState nextState = data.nextState();
		CardPlayer player = event.playerID().getFrom(nextState);
		WorldCard card = event.cardID().getFrom(nextState);
		CardDashboard dashboard = player.cardDashboard();

		dashboard.hand().remove(card);
		if (card.type() == CANTRIP || card.type() == TASK) {
			int tick = logic.gameTick() + RESOLUTION_DELAY;
			asyncEventQueue.add(new CardResolvedAsyncEvent(tick, event.playerID(), event.cardID(), event.targetID()));
			dashboard.discard().addTop(card);
		} else {
			dashboard.queue().append(event);
		}

		if (card.effect().requiredItems != null) {
			player.inventory().sub(card.effect().requiredItems);
		}

		if (player.id().equals(data.playerID())) {
			outgoingNetworkEvents.add(event.toNetworkEvent());
		}

		EffectChain chain = new EffectChain(player.id());
		WorldPos playerPos = player.worldPos();
		Vector2i chunkPos = playerPos.chunkPos();
		List<Structure> structuresInRange = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				List<Structure> structures = nextState.structures(chunkPos.add(j, i));
				if (structures != null) {
					for (Structure s : structures) {
						if (s.worldPos().distanceTo(playerPos) <= s.type().range) {
							structuresInRange.add(s);
						}
					}
				}
			}
		}

//		chain.addWheneverEvent(new PlayCardEvent(event.playerID(), event.cardID()));

		for (Structure structure : structuresInRange) {
			if (structure.type().triggerType.isInstance(event)) {
				Collection<ChainEvent> structureEvents = structure.type().trigger.castAndTrigger(event, structure, nextState);
				if (structureEvents != null) {
					chain.addAllWheneverEvents(structureEvents);
				}
			}
		}
		if (!chain.isEmpty()) {
			nextState.chainHeap().add(chain);
		}

	}

}
