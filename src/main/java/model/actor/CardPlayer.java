package model.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import context.game.visuals.displayer.CardPlayerDisplayer;
import model.GameState;
import model.card.CardDashboard;
import model.card.GameCard;
import model.card.Task;
import model.chain.EffectChain;

public abstract class CardPlayer extends HealthActor {

	private CardDashboard cardDashboard = new CardDashboard();
	private List<EffectChain> chains = new ArrayList<>(1);

	public CardPlayer(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public abstract CardPlayer copy();

	public abstract CardPlayerDisplayer<?> displayer();

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
		Task task = cardDashboard.task();
		if (task != null) {
			if (task.cancelled()) {
				cardDashboard.setTask(null);
				return;
			}
			task.execute(this, task.target(), state);
			if (task.isDone()) {
				cardDashboard.setTask(null);
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
