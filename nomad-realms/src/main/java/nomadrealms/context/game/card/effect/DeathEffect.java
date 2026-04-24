package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.World;

public class DeathEffect extends Effect {

	private final Actor target;

	public DeathEffect(Actor target) {
		super(target);
		this.target = target;
	}

	@Override
	public void resolve(World world) {
		world.spawnDeathParticles(target);
		target.health(0);
		target.tile().clearActor();
	}

}
