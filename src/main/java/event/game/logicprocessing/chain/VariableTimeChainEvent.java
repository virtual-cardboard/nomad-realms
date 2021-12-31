package event.game.logicprocessing.chain;

public abstract class VariableTimeChainEvent extends ChainEvent {

	public VariableTimeChainEvent(long playerID) {
		super(playerID);
	}

	@Override
	public boolean shouldDisplay() {
		return true;
	}

}
