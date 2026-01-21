package nomadrealms.context.game.card.effect;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.CosineConstraint.cos;
import static engine.visuals.constraint.posdim.SineConstraint.sin;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.misc.TimedConstraint;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.context.game.DirectionalFireParticle;

public class SpawnFireCircleEffect extends Effect {

	private final CardPlayer source;

	public SpawnFireCircleEffect(CardPlayer source) {
		this.source = source;
	}

	@Override
	public void resolve(World world) {
		ParticlePool pool = world.state().particlePool;
		Vector2f worldPos = source.tile().chunk().pos().add(source.tile().indexPosition()).vector();
		int count = 20;
		float speed = 0.207f;
		long lifetime = 1000;
		for (int i = 0; i < count; i++) {
			float angle = (float) (i * 2 * Math.PI / count);
			TimedConstraint time = TimedConstraint.time().activate();
			pool.addParticle(new DirectionalFireParticle(
					pool.glContext(),
					lifetime,
					new ConstraintBox(
							absolute(worldPos.x()).add(absolute((float) Math.cos(angle) * speed).multiply(time)),
							absolute(worldPos.y()).add(absolute((float) Math.sin(angle) * speed).multiply(time)),
							absolute(50), absolute(50)),
					absolute(angle)
			));
		}
	}

}
