package model.card.expression;

import model.id.ActorId;
import model.id.Id;
import model.id.TileId;
import model.id.WorldCardId;

public enum CardTargetType {

	CHARACTER,
	SELF_HAND_CARD,
	SELF_QUEUE_CARD,
	SELF_DECK_CARD,
	SELF_DISCARD_CARD,
	ENEMY_QUEUE_CARD,
	TILE;

	public static Id typify(long id, CardTargetType targetType) {
		switch (targetType) {
			case CHARACTER:
				return new ActorId(id);
			case TILE:
				return new TileId(id);
			case SELF_HAND_CARD:
			case SELF_QUEUE_CARD:
			case SELF_DECK_CARD:
			case SELF_DISCARD_CARD:
			case ENEMY_QUEUE_CARD:
				return new WorldCardId(id);
			default:
				throw new RuntimeException("Unhandled target type: " + targetType + " ID: " + id);
		}
	}

}
