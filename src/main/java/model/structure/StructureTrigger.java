package model.structure;

import java.util.Collection;

import event.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.actor.health.Structure;
import model.chain.event.ChainEvent;
import model.state.GameState;

@FunctionalInterface
public interface StructureTrigger<T extends NomadRealmsLogicProcessingEvent> {

	Collection<ChainEvent> trigger(T event, Structure structure, GameState state);

	@SuppressWarnings("unchecked")
	default Collection<ChainEvent> castAndTrigger(NomadRealmsLogicProcessingEvent e, Structure structure, GameState state) {
		return trigger((T) e, structure, state);
	}

}
