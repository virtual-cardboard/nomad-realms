package model.card.expression;

import model.actor.CardPlayer;
import model.state.GameState;

@FunctionalInterface
public interface PlayerTargetPredicate {

	boolean test(CardPlayer player, long targetID, GameState state);

}
