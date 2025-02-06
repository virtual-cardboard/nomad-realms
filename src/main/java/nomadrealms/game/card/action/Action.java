package nomadrealms.game.card.action;

import common.math.Vector2f;
import nomadrealms.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public interface Action {

	public void update(World world);

	public boolean isComplete();

	public default void init(World world) {
	}

	public int preDelay();

	public int postDelay();

	public default Vector2f getScreenOffset(RenderingEnvironment re, long currentTimeMillis) {
		return new Vector2f(0, 0);
	}

}
