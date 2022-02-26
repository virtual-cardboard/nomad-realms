package model.card.expression;

import model.id.ActorID;
import model.id.ID;
import model.id.TileID;
import model.id.WorldCardID;

public enum CardTargetType {

	CHARACTER,
	SELF_HAND_CARD,
	SELF_QUEUE_CARD,
	SELF_DECK_CARD,
	SELF_DISCARD_CARD,
	ENEMY_QUEUE_CARD,
	TILE;

	public static ID<?> typify(long id, CardTargetType targetType) {
		switch (targetType) {
			case CHARACTER:
				return new ActorID(id);
			case TILE:
				return new TileID(id);
			case SELF_HAND_CARD:
			case SELF_QUEUE_CARD:
			case SELF_DECK_CARD:
			case SELF_DISCARD_CARD:
			case ENEMY_QUEUE_CARD:
				return new WorldCardID(id);
			default:
				throw new RuntimeException("Unhandled target type: " + targetType + " ID: " + id);
		}
	}

}
