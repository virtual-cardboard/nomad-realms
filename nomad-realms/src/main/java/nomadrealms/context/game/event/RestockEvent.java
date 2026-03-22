package nomadrealms.context.game.event;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class RestockEvent implements InputEvent {

	private CardPlayer source;
	private Deck deck;

	/**
	 * No-arg constructor for serialization.
	 */
	protected RestockEvent() {
	}

	public RestockEvent(CardPlayer source, Deck deck) {
		this.source = source;
		this.deck = deck;
	}

	public CardPlayer source() {
		return source;
	}

	public Deck deck() {
		return deck;
	}

	@Override
	public void resolve(World world) {
		world.resolve(this);
	}

	@Override
	public void resolve(GameInterface ui) {
		ui.resolve(this);
	}

	@Override
	public void reindex(World world) {
	}

}
