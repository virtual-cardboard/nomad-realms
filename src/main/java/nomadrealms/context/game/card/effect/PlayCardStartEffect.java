package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;

public class PlayCardStartEffect extends Effect {

	private final WorldCard card;

	public PlayCardStartEffect(WorldCard card) {
		this.card = card;
	}

	@Override
	public void resolve(World world) {

	}

	public WorldCard getCard() {
		return card;
	}
}
