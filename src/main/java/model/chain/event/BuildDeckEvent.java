package model.chain.event;

import engine.common.ContextQueues;
import math.IdGenerators;
import model.id.CardPlayerId;
import model.state.GameState;

public class BuildDeckEvent extends FixedTimeChainEvent {

	public BuildDeckEvent(CardPlayerId playerID) {
		super(playerID);
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, ContextQueues contextQueues) {
	}

	@Override
	public int priority() {
		return 30;
	}

	@Override
	public int processTime() {
		return 20;
	}

	@Override
	public String textureName() {
		return "build_deck";
	}

}
