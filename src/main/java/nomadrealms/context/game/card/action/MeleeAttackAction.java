package nomadrealms.context.game.card.action;

import static java.lang.Math.max;

import engine.common.math.Vector2f;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public class MeleeAttackAction implements Action {

	private final CardPlayer source;
	private final Actor target;
	private final int damage;
	private boolean isComplete;

	private int preDelay = 7;
	private int postDelay = 5;

	/**
	 * The start timestamp of the attack
	 */
	private transient long actionStart = 0;

	public MeleeAttackAction(CardPlayer source, Actor target, int damage) {
		this.source = source;
		this.target = target;
		this.damage = damage;
		this.isComplete = false;
	}

	@Override
	public void update(World world) {
		target.damage(damage);
		isComplete = true;
	}

	@Override
	public boolean isComplete() {
		return isComplete;
	}

	@Override
	public void init(World world) {
		actionStart = System.currentTimeMillis();
	}

	@Override
	public int preDelay() {
		return preDelay;
	}

	@Override
	public int postDelay() {
		return postDelay;
	}

	public Vector2f getScreenOffset(RenderingEnvironment re, long currentTimeMillis) {
		Vector2f dir = target.tile().coord().sub(source.tile().coord()).toVector2f().scale(0.6f);
		long time = System.currentTimeMillis();
		if (time - actionStart < (long) preDelay() * re.config.getMillisPerTick()) { // windup
			float progress = (time - actionStart) / (float) (preDelay() * re.config.getMillisPerTick());
			float fx = 7 * progress * progress * progress - 6 * progress * progress; // 7x^{3}-6x^{2}
			return dir.scale(fx);
		} else {
			float progress =
					(time - actionStart - preDelay() * re.config.getMillisPerTick()) / (float) (postDelay() * re.config.getMillisPerTick());
			return dir.scale(max(1 - progress, 0));
		}
	}

}
