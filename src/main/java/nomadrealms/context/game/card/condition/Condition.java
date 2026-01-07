package nomadrealms.context.game.card.condition;

import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public interface Condition {

	public boolean test(World world, Target target);

}
