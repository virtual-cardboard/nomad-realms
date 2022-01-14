package model.structure;

import java.util.Collection;
import java.util.function.BiFunction;

import common.event.GameEvent;
import event.game.NomadRealmsGameEvent;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.Structure;
import model.state.GameState;

public enum StructureType {

	OVERCLOCKED_MACHINERY(10, 4, CardPlayedEvent.class, (cpe, structure, state) -> {
		// TODO https://trello.com/c/Aq5ECXMW/110-card-overclocked-machinery
		return null;
	});

	public final int health;
	public final int range;
	public final BiFunction<Structure, GameState, Collection<GameEvent>> onSummon;
	public final Class<? extends NomadRealmsGameEvent> triggerType;
	public final StructureTrigger<? extends NomadRealmsGameEvent> trigger;

	private <T extends NomadRealmsGameEvent> StructureType(int health, int range, Class<T> triggerType,
			StructureTrigger<T> trigger) {
		this(health, range, null, triggerType, trigger);
	}

	private StructureType(int health, BiFunction<Structure, GameState, Collection<GameEvent>> onSummon) {
		this(health, 0, onSummon, null, null);
	}

	private <T extends NomadRealmsGameEvent> StructureType(int health, int range,
			BiFunction<Structure, GameState, Collection<GameEvent>> onSummon,
			Class<T> triggerType, StructureTrigger<T> trigger) {
		this.health = health;
		this.range = range;
		this.onSummon = onSummon;
		this.triggerType = triggerType;
		this.trigger = trigger;
	}

}
