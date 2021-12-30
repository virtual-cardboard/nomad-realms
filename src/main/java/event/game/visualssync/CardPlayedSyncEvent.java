package event.game.visualssync;

import common.math.Vector2f;
import common.math.Vector2i;
import model.actor.CardPlayer;
import model.card.GameCard;

public class CardPlayedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private GameCard card;
	private Vector2i chunkPos;
	private Vector2f pos;

	public CardPlayedSyncEvent(CardPlayer player, GameCard card, Vector2i chunkPos, Vector2f pos) {
		super(player);
		this.card = card;
		this.chunkPos = chunkPos;
		this.pos = pos;
	}

	public GameCard card() {
		return card;
	}

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public Vector2f pos() {
		return pos;
	}

}
