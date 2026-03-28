package nomadrealms.render.particle.spawner;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.event.Target;

@FunctionalInterface
public interface ParticlePropertyFunction<T> {
	T apply(int index, Actor source, Target target);
}
