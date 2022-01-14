package model.actor;

import java.util.ArrayList;
import java.util.List;

import model.card.CardDashboard;
import model.chain.EffectChain;
import model.state.GameState;
import model.task.Task;

public abstract class CardPlayer extends HealthActor {

	protected CardDashboard cardDashboard = new CardDashboard();
	protected List<EffectChain> chains = new ArrayList<>(1);

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	public CardPlayer(int maxHealth, long id) {
		super(id, maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	public <A extends CardPlayer> A copyTo(A copy) {
		copy.chains = chains;
		return super.copyTo(copy);
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
	public void update(GameState state) {
		super.update(state);
		Task task = cardDashboard.task();
		if (task != null) {
			if (task.cancelled()) {
				cardDashboard.setTask(null);
				return;
			}
			if (cardDashboard.queue().isEmpty() && chains.size() == 1) {
				if (task.paused()) {
					task.resume(id, state);
					task.setPaused(false);
				}
				task.execute(id, state);
				if (task.isDone()) {
					cardDashboard.setTask(null);
				}
			} else if (!task.paused()) {
				task.pause(id, state);
				task.setPaused(true);
			}
		}
	}

	public boolean addChain(EffectChain e) {
		return chains.add(e);
	}

	public boolean removeChain(EffectChain e) {
		return chains.remove(e);
	}

	public List<EffectChain> chains() {
		return chains;
	}

	public void setCardDashboard(CardDashboard cardDashboard) {
		this.cardDashboard = cardDashboard;
	}

}
