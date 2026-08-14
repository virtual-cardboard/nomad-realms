package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.World;

public class AddCardToStackEffect extends Effect {

	private final CardPlayer target;
	private final GameCard card;
	private int count = 1;

	public AddCardToStackEffect(Actor source, CardPlayer target, GameCard card) {
		super(source);
		this.target = target;
		this.card = card;
	}

	public CardPlayer target() {
		return target;
	}

	public GameCard card() {
		return card;
	}

	public int count() {
		return count;
	}

	public void count(int count) {
		this.count = count;
	}

	@Override
	public void resolve(World world) {
		for (int i = 0; i < count; i++) {
			target.cardStack().add(new CardPlayedEvent(new WorldCard(null, card), target, null));
		}
	}

}
