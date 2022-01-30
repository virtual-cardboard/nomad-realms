package model.structure;

import static model.card.CardType.ACTION;

import java.util.Collection;
import java.util.function.BiFunction;

import event.game.logicprocessing.CardPlayedEvent;
import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.actor.Structure;
import model.card.WorldCard;
import model.card.chain.ChainEvent;
import model.state.GameState;

public enum StructureType {

	OVERCLOCKED_MACHINERY(10, 4, CardPlayedEvent.class, (cpe, structure, state) -> {
		WorldCard card = state.card(cpe.cardID());
		if (card.type() == ACTION) {
			card.setCostModifier(card.costModifier() - 1);
		}
		return null;
	});

	public final int health;
	public final int range;
	public final BiFunction<Structure, GameState, Collection<ChainEvent>> onSummon;
	public final Class<? extends NomadRealmsLogicProcessingEvent> triggerType;
	public final StructureTrigger<? extends NomadRealmsLogicProcessingEvent> trigger;

	private <T extends NomadRealmsLogicProcessingEvent> StructureType(int health, int range, Class<T> triggerType,
			StructureTrigger<T> trigger) {
		this(health, range, null, triggerType, trigger);
	}

	private StructureType(int health, BiFunction<Structure, GameState, Collection<ChainEvent>> onSummon) {
		this(health, 0, onSummon, null, null);
	}

	private <T extends NomadRealmsLogicProcessingEvent> StructureType(int health, int range,
			BiFunction<Structure, GameState, Collection<ChainEvent>> onSummon,
			Class<T> triggerType, StructureTrigger<T> trigger) {
		this.health = health;
		this.range = range;
		this.onSummon = onSummon;
		this.triggerType = triggerType;
		this.trigger = trigger;
	}

}
