package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.particle.spawner.ParticleSpawner;
import engine.serialization.Derializable;

@Derializable
public class SpawnParticlesExpression implements CardExpression {

	/**
	 * Transient: the spawner holds runtime lambda callbacks, so a restored expression spawns nothing.
	 */
	private transient final ParticleSpawner spawner;

	public SpawnParticlesExpression(ParticleSpawner spawner) {
		this.spawner = spawner;
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new SpawnParticlesEffect(context.source(), spawner.copy(), context));
	}

	/**
	 * No-arg constructor for serialization.
	 */
	SpawnParticlesExpression() {
		this.spawner = null;
	}
}

