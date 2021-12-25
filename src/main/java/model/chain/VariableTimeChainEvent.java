package model.chain;

import model.actor.CardPlayer;

public abstract class VariableTimeChainEvent extends ChainEvent {

	public VariableTimeChainEvent(CardPlayer player) {
		super(player);
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
