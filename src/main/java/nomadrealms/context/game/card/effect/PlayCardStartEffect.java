package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;

public class PlayCardStartEffect extends Effect {

	private final WorldCard card;
	private final List<Effect> effects;

	public PlayCardStartEffect(WorldCard card, List<Effect> effects) {
		this.card = card;
		this.effects = effects;
	}

	@Override
	public void resolve(World world) {

	}

	public WorldCard card() {
		return card;
	}

	public List<Effect> effects() {
		return effects;
	}

}
