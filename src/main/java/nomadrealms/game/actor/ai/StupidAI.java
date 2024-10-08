package nomadrealms.game.actor.ai;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;

public class StupidAI extends CardPlayerAI {

	public StupidAI(CardPlayer cardPlayer) {
		super(cardPlayer);
	}

	@Override
	public void update(CardPlayer cardPlayer, GameState state) {
		WorldCard cardToPlay = cardPlayer.deckCollection().deck1().peek();
		switch (cardToPlay.card().targetingInfo().targetType()) {
			case HEXAGON:
				// TODO figure out which chunk the next tile is on
				cardPlayer.addNextPlay(new CardPlayedEvent(cardToPlay, cardPlayer,
						cardPlayer.tile().dr(state.world)));
				break;
			case NONE:
				cardPlayer.addNextPlay(new CardPlayedEvent(cardToPlay, cardPlayer, null));
				break;
			case CARD_PLAYER:
				cardPlayer.addNextPlay(new CardPlayedEvent(cardToPlay, cardPlayer, cardPlayer));
				break;
		}
	}

	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 20) + 24;
	}

}
