package model.actor;

import static math.IDGenerator.genID;

import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import common.source.GameSource;
import model.card.GameCard;

public abstract class Actor implements GameSource {

	protected long id;

	public Actor() {
		id = genID();
	}

	public Actor(long id) {
		this.id = id;
	}

	public long id() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public abstract Actor copy();

	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, GameCard> cards, Map<Vector2i, List<PositionalActor>> chunkToActors) {
		actors.put(id, this);
	}

}
