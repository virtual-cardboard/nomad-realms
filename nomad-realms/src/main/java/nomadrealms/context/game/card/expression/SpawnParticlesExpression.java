package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.render.particle.ParticleParameters;
import nomadrealms.render.particle.spawner.ParticleSpawner;

public class SpawnParticlesExpression implements CardExpression {

	private final ParticleSpawner spawner;

	public SpawnParticlesExpression(ParticleSpawner spawner) {
		this.spawner = spawner;
	}

	@Override
	public List<Effect> effects(World world, Target target, Actor source) {
		ParticleParameters params = new ParticleParameters()
				.world(world)
				.source(source)
				.target(target);
		return singletonList(new SpawnParticlesEffect(source, spawner.copy(), params));
	}

}
