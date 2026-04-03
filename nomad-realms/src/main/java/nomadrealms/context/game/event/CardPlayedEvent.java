package nomadrealms.context.game.event;

import java.util.ArrayList;
import java.util.List;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.PlayCardEndEffect;
import nomadrealms.context.game.card.effect.PlayCardStartEffect;
import nomadrealms.context.game.world.World;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class CardPlayedEvent extends WorldCard implements InputEvent {

	private WorldCard originalCard;

	CardPlayer source;
	Target target;

	/**
	 * No-arg constructor for serialization.
	 */
	protected CardPlayedEvent() {
	}

	public CardPlayedEvent(WorldCard card, CardPlayer source, Target target) {
		this.originalCard = card;
		this.deck(card.deck());
		this.card(card.card());
		this.ephemeral(card.ephemeral());
		this.source = source;
		this.target = target;
	}

	public WorldCard originalCard() {
		return originalCard;
	}

	public CardPlayer source() {
		return source;
	}

	public Target target() {
		return target;
	}

	public ProcChain procChain(World world) {
		List<Effect> effects = new ArrayList<>();
		List<Effect> cardEffects = card().expression().effects(world, target(), source());
		effects.add(new PlayCardStartEffect(source(), this, cardEffects));
		effects.addAll(cardEffects);
		effects.add(new PlayCardEndEffect(source(), this));
		return new ProcChain(effects);
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
	public String toString() {
		return "CardPlayedEvent{" +
				"card=" + card() +
				", source=" + source +
				", target=" + target +
				'}';
	}

	@Override
	public void reindex(World world) {
	}

}
