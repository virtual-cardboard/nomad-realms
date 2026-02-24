package nomadrealms.context.game.actor;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.actor.status.StatusEffect.INVINCIBLE;

import engine.visuals.constraint.box.ConstraintPair;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.status.Status;
import nomadrealms.context.game.actor.types.HasHealth;
import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.actor.types.HasPosition;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.card.query.actor.SelfQuery;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.Renderable;
import nomadrealms.render.particle.ParticleParameters;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

/**
 * An entity in the game world. Actors can have health, a position, an inventory, and can be targeted by actions. They
 * can also be rendered on the screen.
 *
 * @author Lunkle
 */
public interface Actor extends HasPosition, HasHealth, HasInventory, Target, Renderable {

	String name();

	default List<Action> actions() {
		return new ArrayList<>();
	}

	default void update(GameState state) {
	}

	default List<InputEvent> retrieveNextPlays() {
		return new ArrayList<>();
	}

	default boolean isDestroyed() {
		return health() <= 0;
	}

	Status status();

	@Override
	default void damage(int damage) {
		if (damage > 0 && status().count(INVINCIBLE) > 0) {
			status().remove(INVINCIBLE, 1);
			particlePool().addParticles(new SpawnParticlesEffect(
					this,
					new BasicParticleSpawner(new SelfQuery<>(), "text_blocked")
							.sizeOffset(new ConstraintPair(absolute(10), absolute(10)))
							.positionOffset(new ConstraintPair(absolute(0), time().neg().multiply(0.1f)))
							.lifetime(500),
					new ParticleParameters().source(this).target(this)
			));
			return;
		}
		HasHealth.super.damage(damage);
	}

	void particlePool(ParticlePool particlePool);

	ParticlePool particlePool();

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	default void reindex(World world) {
	}

	@Override
	default boolean move(Tile target) {
		if (target.actor() != null) {
			return false;
		}
		if (tile() != null) {
			tile().clearActor();
		}
		if (!HasPosition.super.move(target)) {
			return false;
		}
		target.actor(this);
		return true;
	}

}
