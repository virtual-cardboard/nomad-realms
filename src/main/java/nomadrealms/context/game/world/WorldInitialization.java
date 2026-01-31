package nomadrealms.context.game.world;

import java.io.Serializable;

@FunctionalInterface
public interface WorldInitialization extends Serializable {
	void apply(World world);
}
