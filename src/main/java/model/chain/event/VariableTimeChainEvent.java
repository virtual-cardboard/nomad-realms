package model.chain.event;

import model.id.CardPlayerId;

public abstract class VariableTimeChainEvent extends ChainEvent {

	public VariableTimeChainEvent(CardPlayerId playerID) {
		super(playerID);
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
