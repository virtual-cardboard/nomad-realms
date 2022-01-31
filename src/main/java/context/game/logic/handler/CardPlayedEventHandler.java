package context.game.logic.handler;

import static model.card.CardType.CANTRIP;
import static model.card.CardType.TASK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import common.math.Vector2i;
import context.game.NomadsGameData;
import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.CardResolvedEvent;
import model.actor.CardPlayer;
import model.actor.Structure;
import model.card.CardDashboard;
import model.card.WorldCard;
import model.card.chain.ChainEvent;
import model.card.chain.PlayCardChainEvent;
import model.chain.EffectChain;
import model.state.GameState;

public class CardPlayedEventHandler implements Consumer<CardPlayedEvent> {

	private NomadsGameData data;
	private CardResolvedEventHandler creHandler;

	public CardPlayedEventHandler(NomadsGameData data, CardResolvedEventHandler creHandler) {
		this.data = data;
		this.creHandler = creHandler;
	}

	/**
	 * If the card is a cantrip, then it resolves immediately and is handled by
	 * {@link CardResolvedEventHandler}.
	 */
	@Override
	public void accept(CardPlayedEvent event) {
		GameState state = data.nextState();
		CardPlayer player = state.cardPlayer(event.playerID());
		WorldCard card = state.card(event.cardID());
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

		EffectChain chain = new EffectChain(player.id());
		Vector2i chunkPos = player.worldPos().chunkPos();
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

		chain.addWheneverEvent(new PlayCardChainEvent(event.playerID(), event.cardID()));

		for (int i = 0; i < chain.size(); i++) {
			ChainEvent e = chain.get(i);
			for (Structure structure : structuresInRange) {
				if (structure.type().triggerType.isInstance(e)) {
					Collection<ChainEvent> structureEvents = structure.type().trigger.castAndTrigger(e, structure, state);
					if (structureEvents != null) {
//						chain.addAll(structureEvents);
					}
				}
			}

		}
	}

}
