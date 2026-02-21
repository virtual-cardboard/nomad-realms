package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;

public class PlayCardEndEffect extends Effect {

	private final WorldCard card;

	public PlayCardEndEffect(Actor source, WorldCard card) {
		super(source);
		this.card = card;
	}

	@Override
	public void resolve(World world) {

	}


	public WorldCard card() {
		return card;
	}

}
