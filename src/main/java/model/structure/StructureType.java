package model.structure;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.asList;
import static model.card.CardType.ACTION;

import java.util.Collection;
import java.util.function.BiFunction;

import event.logicprocessing.CardPlayedEvent;
import event.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.actor.Structure;
import model.card.WorldCard;
import model.chain.event.BuildDeckEvent;
import model.chain.event.ChainEvent;
import model.chain.event.DrawCardEvent;
import model.chain.event.InteractEvent;
import model.state.GameState;

public enum StructureType {

	HOUSE("house_full", 10, 4, DrawCardEvent.class, (dce, structure, state) -> {
//		return asList(new RestoreHealthEvent(structure.id(), dce.targetID(), 1));
		return null;
	}),
	OVERCLOCKED_MACHINERY("overclocked_machinery", 10, 4, CardPlayedEvent.class, (cpe, structure, state) -> {
		WorldCard card = cpe.cardID().getFrom(state);
		if (card.type() == ACTION) {
			card.setCostModifier(card.costModifier() - 1);
		}
		return null;
	}),
	PLANNING_TABLE("planning_table", 10, MAX_VALUE, InteractEvent.class, (event, structure, state) -> {
		if (event.targetID().equals(structure.id())) {
			return asList(new BuildDeckEvent(event.playerID()));
		}
		return null;
	});

	private String imageName;
	public final int health;
	public final int range;
	public final BiFunction<Structure, GameState, Collection<ChainEvent>> onSummon;
	public final Class<? extends NomadRealmsLogicProcessingEvent> triggerType;
	public final StructureTrigger<? extends NomadRealmsLogicProcessingEvent> trigger;

	private <T extends NomadRealmsLogicProcessingEvent> StructureType(String imageName, int health, int range, Class<T> triggerType,
	                                                                  StructureTrigger<T> trigger) {
		this(imageName, health, range, null, triggerType, trigger);
	}

	private StructureType(String imageName, int health, BiFunction<Structure, GameState, Collection<ChainEvent>> onSummon) {
		this(imageName, health, 0, onSummon, null, null);
	}

	private <T extends NomadRealmsLogicProcessingEvent> StructureType(String imageName, int health, int range,
	                                                                  BiFunction<Structure, GameState, Collection<ChainEvent>> onSummon,
	                                                                  Class<T> triggerType, StructureTrigger<T> trigger) {
		this.imageName = imageName;
		this.health = health;
		this.range = range;
		this.onSummon = onSummon;
		this.triggerType = triggerType;
		this.trigger = trigger;
	}

	public String imageName() {
		return imageName;
	}

}
