package model.card.condition;

import java.util.function.BiPredicate;

import model.GameObject;
import model.actor.Actor;
import model.actor.CardPlayer;

public class RangeCondition implements BiPredicate<CardPlayer, GameObject> {

	private final int range;

	public RangeCondition(int range) {
		this.range = range;
	}

	@Override
	public boolean test(CardPlayer player, GameObject target) {
		if (target instanceof Actor) {
			Actor actor = (Actor) target;
			return player.worldPos().distanceTo(actor.worldPos()) <= range;
		}
		return false;
	}

}
