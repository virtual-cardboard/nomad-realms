package model.structure;

import java.util.Collection;

import event.game.NomadRealmsGameEvent;
import model.actor.Structure;
import model.state.GameState;

@FunctionalInterface
public interface StructureTrigger<T extends NomadRealmsGameEvent> {

	Collection<NomadRealmsGameEvent> trigger(T event, Structure structure, GameState state);

}
