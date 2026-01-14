package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.World;

public class AddCardToStackEffect extends Effect {

	private final CardPlayer target;
	private final GameCard card;

	public AddCardToStackEffect(CardPlayer target, GameCard card) {
		this.target = target;
		this.card = card;
	}

	@Override
	public void resolve(World world) {
		target.cardStack().add(new CardPlayedEvent(new WorldCard(card), target, null));
	}

}
