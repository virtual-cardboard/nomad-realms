package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import common.source.GameSource;
import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;
import model.GameState;

public abstract class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(GameSource source) {
		super(source);
	}

	public ChainEvent(long time, GameSource source) {
		super(time, source);
	}

	public abstract void process(GameState state, Queue<GameEvent> sync);

	public abstract int priority();

	/**
	 * @return The time it takes to process this event, in ticks
	 */
	public abstract int processTime();

}
