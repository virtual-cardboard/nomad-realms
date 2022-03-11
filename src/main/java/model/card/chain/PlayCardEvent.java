package model.card.chain;

import common.QueueGroup;
import model.id.CardPlayerID;
import model.id.WorldCardID;
import model.state.GameState;

public final class PlayCardEvent extends ChainEvent {

	private WorldCardID cardID;

	public PlayCardEvent(CardPlayerID playerID, WorldCardID cardID) {
		super(playerID);
		this.cardID = cardID;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
	}

	@Override
	public int priority() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public boolean checkIsDone(GameState state) {
		return true;
	}

	@Override
	public boolean cancelled(GameState state) {
		return false;
	}

	@Override
	public boolean shouldDisplay() {
		return false;
	}

	public WorldCardID cardID() {
		return cardID;
	}

	@Override
	public String textureName() {
		return "play_card";
	}

}
