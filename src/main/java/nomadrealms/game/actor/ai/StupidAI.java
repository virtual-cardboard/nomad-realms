package nomadrealms.game.actor.ai;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;

public class StupidAI extends CardPlayerAI {

	public StupidAI(CardPlayer self) {
		super(self);
	}

	@Override
	public void update(GameState state) {
		WorldCard cardToPlay = self.deckCollection().deck1().peek();
		switch (cardToPlay.card().targetingInfo().targetType()) {
			case HEXAGON:
				// TODO figure out which chunk the next tile is on
				self.addNextPlay(new CardPlayedEvent(cardToPlay, self,
						self.tile().dr(state.world)));
				break;
			case NONE:
				self.addNextPlay(new CardPlayedEvent(cardToPlay, self, null));
				break;
			case CARD_PLAYER:
				self.addNextPlay(new CardPlayedEvent(cardToPlay, self, self));
				break;
		}
	}

	@Override
	protected int resetThinkingTime() {
		return (int) (Math.random() * 20) + 24;
	}

}
