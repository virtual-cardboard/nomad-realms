package model.chain.event;

import model.id.CardPlayerID;

public abstract class VariableTimeChainEvent extends ChainEvent {

	public VariableTimeChainEvent(CardPlayerID playerID) {
		super(playerID);
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
