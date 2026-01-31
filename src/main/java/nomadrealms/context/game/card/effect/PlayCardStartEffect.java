package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.context.game.world.World;

public class PlayCardStartEffect extends Effect {

	private final WorldCard card;
	private final TargetingInfo targetingInfo;
	private final List<Effect> effects;

	public PlayCardStartEffect(WorldCard card, TargetingInfo targetingInfo, List<Effect> effects) {
		this.card = card;
		this.targetingInfo = targetingInfo;
		this.effects = effects;
	}

	@Override
	public void resolve(World world) {

	}

	public WorldCard card() {
		return card;
	}

	public TargetingInfo targetingInfo() {
		return targetingInfo;
	}

	public List<Effect> effects() {
		return effects;
	}

}
