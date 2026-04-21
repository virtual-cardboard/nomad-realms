package nomadrealms.context.game.card.effect;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.actor.StaticTargetQuery;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

public class DeathEffect extends Effect {

	private final Actor target;

	public DeathEffect(Actor target) {
		super(target);
		this.target = target;
	}

	@Override
	public void resolve(World world) {
		spawnDeathParticles(world, target);
		target.health(0);
		if (target.tile() != null) {
			target.tile().clearActor();
		}
	}

	private void spawnDeathParticles(World world, Actor actor) {
		Tile deathTile = actor.tile();
		if (deathTile == null) {
			return;
		}
		world.particlePool().addParticles(new SpawnParticlesEffect(
				actor,
				new BasicParticleSpawner(new StaticTargetQuery<>(deathTile), "pill")
						.particleCount(8)
						.size((i, s, t) -> new ConstraintPair(absolute(5), absolute(11)))
						.rotation((i, s, t) -> absolute((float) (i * 2 * PI / 8)))
						.position((i, s, t) -> {
							float angle = (float) (i * 2 * PI / 8);
							return new ConstraintPair(t.tile().pos().vector())
									.add(time().multiply((float) sin(angle)).multiply(0.1f),
											time().multiply((float) cos(angle)).multiply(-0.1f));
						})
						.lifetime((i, s, t) -> 500L),
				new EffectContext().source(actor).target(actor)
		));
		world.particlePool().addParticles(new SpawnParticlesEffect(
				actor,
				new BasicParticleSpawner(new StaticTargetQuery<>(deathTile), "text_pop")
						.size((i, s, t) -> new ConstraintPair(absolute(10), absolute(10)))
						.position((i, s, t) -> {
							float v0x = 0.05f;
							float v0y = -0.1f;
							return new ConstraintPair(t.tile().pos().vector())
									.add(time().multiply(v0x).add(time().multiply(time()).multiply(-v0x / 2000f)),
											time().multiply(v0y).add(time().multiply(time()).multiply(-v0y / 2000f)));
						})
						.lifetime((i, s, t) -> 500L),
				new EffectContext().source(actor).target(actor)
		));
	}

}
