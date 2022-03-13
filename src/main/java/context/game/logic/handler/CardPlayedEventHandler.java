package context.game.logic.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import event.network.NomadRealmsNetworkEvent;
import model.actor.CardPlayer;
import model.actor.Structure;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.card.chain.ChainEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private CardResolvedEventHandler creHandler;
	private Queue<NomadRealmsNetworkEvent> outgoingNetworkEvents;

	public CardPlayedEventHandler(NomadsGameData data, CardResolvedEventHandler creHandler, Queue<NomadRealmsNetworkEvent> outgoingNetworkEvents) {
		this.data = data;
		this.creHandler = creHandler;
		this.outgoingNetworkEvents = outgoingNetworkEvents;
	}

	/**
	 * If the card is a cantrip, then it resolves immediately and is handled by
	 * {@link CardResolvedEventHandler}.
	 */
	@Override
	public void accept(CardPlayedEvent event) {
		GameState currentState = data.currentState();
		CardPlayer player = event.playerID().getFrom(currentState);
		WorldCard card = event.cardID().getFrom(currentState);
		CardDashboard dashboard = player.cardDashboard();

		dashboard.hand().remove(card);
		if (card.type() == CANTRIP) {
			creHandler.accept(new CardResolvedEvent(event.playerID(), event.cardID(), event.targetID()));
		} else if (card.type() == TASK) {
			dashboard.cancelTask();
			creHandler.accept(new CardResolvedEvent(event.playerID(), event.cardID(), event.targetID()));
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
//		Vector2i chunkPos = player.worldPos().chunkPos();
		List<Structure> structuresInRange = new ArrayList<>();
//		for (int i = -1; i <= 1; i++) {
//			for (int j = -1; j <= 1; j++) {
//				List<Structure> structures = state.structures(chunkPos.add(j, i));
//				for (int k = 0; k < structures.size(); k++) {
//					Structure s = structures.get(k);
//					if (s.worldPos().distanceTo(playerPos) <= s.type().range) {
//						structuresInRange.add(s);
//					}
//				}
//			}
//		}

//		chain.addWheneverEvent(new PlayCardEvent(event.playerID(), event.cardID()));

		for (int i = 0; i < chain.size(); i++) {
			ChainEvent e = chain.get(i);
			for (Structure structure : structuresInRange) {
				if (structure.type().triggerType.isInstance(e)) {
					Collection<ChainEvent> structureEvents = structure.type().trigger.castAndTrigger(e, structure, currentState);
					if (structureEvents != null) {
//						chain.addAll(structureEvents);
					}
				}
			}

		}
	}

}
