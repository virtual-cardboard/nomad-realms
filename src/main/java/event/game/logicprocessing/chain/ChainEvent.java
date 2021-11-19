package event.game.logicprocessing.chain;

import common.source.GameSource;
import event.game.logicprocessing.NomadRealmsLogicProcessingEvent;

public class ChainEvent extends NomadRealmsLogicProcessingEvent {

	public ChainEvent(GameSource source) {
		super(source);
	}

	public ChainEvent(long time, GameSource source) {
		super(time, source);
	}

}
