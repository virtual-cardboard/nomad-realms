package model.structure;

import java.util.Collection;

import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;
import event.game.logicprocessing.chain.ChainEvent;
import model.actor.Structure;
import model.state.GameState;

@FunctionalInterface
public interface StructureTrigger<T extends NomadRealmsLogicProcessingEvent> {

	Collection<ChainEvent> trigger(T event, Structure structure, GameState state);

	@SuppressWarnings("unchecked")
	default Collection<ChainEvent> castAndTrigger(NomadRealmsLogicProcessingEvent e, Structure structure, GameState state) {
		return trigger((T) e, structure, state);
	}

}
