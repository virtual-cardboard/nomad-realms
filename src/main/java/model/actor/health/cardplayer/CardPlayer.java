package model.actor.health.cardplayer;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import model.actor.health.EventEmitter;
import model.card.CardDashboard;
import model.id.CardPlayerId;
import model.item.ItemCollection;
import model.state.GameState;
import model.task.Task;

public abstract class CardPlayer extends EventEmitter {

	protected CardDashboard cardDashboard = new CardDashboard();
	protected ItemCollection inventory = new ItemCollection();

	public CardPlayer() {
	}

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	public CardPlayer(int maxHealth, long id) {
		super(id, maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	public <A extends CardPlayer> A copyTo(A copy) {
		copy.inventory = inventory.copy();
		return super.copyTo(copy);
	}

	@Override
	public abstract CardPlayerId id();

	public ItemCollection inventory() {
		return inventory;
	}

	public CardDashboard cardDashboard() {
		return cardDashboard;
	}

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.cardPlayers().add(this);
	}

	@Override
	public void removeFrom(GameState state) {
		super.removeFrom(state);
		state.cardPlayers().remove(this);
	}

	@Override
	public void update(long tick, GameState state) {
		Task task = cardDashboard.task();
		if (task != null) {
			if (task.cancelled()) {
				cardDashboard.setTask(null);
				return;
			}
			// TODO only resume task if 1 chain
			if (cardDashboard.queue().isEmpty()) {
				if (task.paused()) {
					task.resume(id(), state);
					task.setPaused(false);
				}
				task.execute(id(), state);
				if (task.isDone()) {
					cardDashboard.setTask(null);
				}
			} else if (!task.paused()) {
				task.pause(id(), state);
				task.setPaused(true);
			}
		}
	}

	public void setCardDashboard(CardDashboard cardDashboard) {
		this.cardDashboard = cardDashboard;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
		System.err.println("Warning: you are trying to deserialize a " + getClass().getSimpleName() +
				", but the serialization for CardPlayer hasn't been implemented yet.");
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
		System.err.println("Warning: you are trying to serialize a " + getClass().getSimpleName() +
				", but the serialization for CardPlayer hasn't been implemented yet.");
	}

}
