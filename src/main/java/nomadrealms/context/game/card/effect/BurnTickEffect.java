package nomadrealms.context.game.card.effect;

import static java.util.Arrays.asList;

import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class BurnTickEffect extends Effect {

	private final Target target;

	public BurnTickEffect(Target target) {
		this.target = target;
	}

	@Override
	public void resolve(World world) {
		world.addProcChain(new ProcChain(asList(
				new DamageEffect(target, target, 1),
				new ApplyStatusEffect(target, StatusEffect.BURNED, -1)
		)));
	}

}
