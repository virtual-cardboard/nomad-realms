package model.card.condition;

import static model.world.Tile.TILE_HEIGHT;

import java.util.function.BiPredicate;

import common.math.Vector2f;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.actor.HealthActor;

public class RangeCondition implements BiPredicate<CardPlayer, GameObject> {

	private final int range;

	public RangeCondition(int range) {
		this.range = range;
	}

	@Override
	public boolean test(CardPlayer player, GameObject target) {
		if (target instanceof HealthActor) {
			HealthActor actor = (HealthActor) target;
			Vector2f relativePos = player.relativePos(actor.chunkPos(), actor.pos());
			return relativePos.scale(1f / TILE_HEIGHT).lengthSquared() < range * range;
		}
		return false;
	}

}
