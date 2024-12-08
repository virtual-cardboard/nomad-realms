package nomadrealms.game.card.intent;

import nomadrealms.game.actor.HasPosition;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

public class MoveIntent implements Intent {

	private final HasPosition source;
	private final Tile target;

	public MoveIntent(HasPosition source, Tile target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public void resolve(World world) {
		if (world.tileToEntityMap.get(target) != null) {
			return;
		}
		source.move(target);
	}

}
