package model.card.chain;

import static math.IntegerRandom.randomInt;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.CardPlayer;
import model.card.CardDashboard;
import model.card.CardZone;
import model.card.WorldCard;
import model.id.ID;
import model.state.GameState;

public class DiscardCardEvent extends FixedTimeChainEvent {

	private ID<? extends CardPlayer> targetID;
	private int amount;

	public DiscardCardEvent(ID<? extends CardPlayer> playerID, ID<? extends CardPlayer> targetID, int amount) {
		super(playerID);
		this.targetID = targetID;
		this.amount = amount;
	}

	@Override
	public int priority() {
		return 8;
	}

	@Override
	public int processTime() {
		return 5;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer target = targetID.getFrom(state);
		CardDashboard dashboard = target.cardDashboard();
		CardZone hand = dashboard.hand();
		for (int i = 0; i < amount && !hand.empty(); i++) {
			WorldCard card = hand.remove(randomInt(hand.size()));
			dashboard.discard().addTop(card);
		}
	}

	@Override
	public boolean cancelled(GameState state) {
		return super.cancelled(state) || targetID.getFrom(state).shouldRemove();
	}

	@Override
	public String textureName() {
		return "discard_card";
	}

}
