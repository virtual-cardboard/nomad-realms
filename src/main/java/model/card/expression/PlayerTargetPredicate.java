package model.card.expression;

import model.GameObject;
import model.actor.CardPlayer;
import model.state.GameState;

@FunctionalInterface
public interface PlayerTargetPredicate {

	boolean test(CardPlayer player, GameObject target, GameState state);

}
