package model.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.GameState;
import model.card.CardDashboard;
import model.card.GameCard;
import model.chain.EffectChain;
import model.task.Task;

public abstract class CardPlayer extends HealthActor {

	private CardDashboard cardDashboard = new CardDashboard();
	private List<EffectChain> chains = new ArrayList<>(1);

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	public CardDashboard cardDashboard() {
		return cardDashboard;
	}

	@Override
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards, Map<Vector2i, List<Actor>> chunkToActors) {
		super.addTo(actors, cardPlayers, cards, chunkToActors);
		cardPlayers.put(id, this);
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
					task.resume(this, task.target(), state);
					task.setPaused(false);
				}
				task.execute(this, task.target(), state);
				if (task.isDone()) {
					cardDashboard.setTask(null);
				}
			} else if (!task.paused()) {
				task.pause(this, task.target(), state);
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

}
