package nomadrealms.game.card.action;

import nomadrealms.game.world.World;

public interface Action {

	public void update(World world);

	public boolean isComplete();

}
