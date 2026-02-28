package nomadrealms.context.game.event;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.world.World;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class InteractEvent implements InputEvent {

	private CardPlayer source;
	private Structure target;

	/**
	 * No-arg constructor for serialization.
	 */
	protected InteractEvent() {
	}

	public InteractEvent(CardPlayer source, Structure target) {
		this.source = source;
		this.target = target;
	}

	public CardPlayer source() {
		return source;
	}

	public Structure target() {
		return target;
	}

	@Override
	public void resolve(World world) {
		world.resolve(this);
	}

	@Override
	public void resolve(GameInterface ui) {
		ui.resolve(this);
	}

}
