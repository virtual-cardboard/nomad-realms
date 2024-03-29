package nomadrealms.misc;

import nomadrealms.card.card.WorldCard;
import nomadrealms.world.actor.CardPlayer;

public class CardPlayedEvent {

	WorldCard worldCard;
	CardPlayer source;
	Target target;

	public CardPlayedEvent(WorldCard worldCard, CardPlayer source, Target target) {
		this.worldCard = worldCard;
		this.source = source;
		this.target = target;
	}

	public WorldCard card() {
		return worldCard;
	}

	public CardPlayer source() {
		return source;
	}

	public Target target() {
		return target;
	}

}
