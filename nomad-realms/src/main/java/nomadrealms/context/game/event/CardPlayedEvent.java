package nomadrealms.context.game.event;

import java.util.ArrayList;
import java.util.List;

import engine.serialization.Derializable;
import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.Card;
import nomadrealms.context.game.card.CardType;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.PlayCardEndEffect;
import nomadrealms.context.game.card.effect.PlayCardStartEffect;
import nomadrealms.context.game.world.World;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class CardPlayedEvent implements InputEvent, Card {

	private WorldCard card;

	CardPlayer source;
	Target target;

	/**
	 * No-arg constructor for serialization.
	 */
	protected CardPlayedEvent() {
	}

	public CardPlayedEvent(WorldCard card, CardPlayer source, Target target) {
		this.card = card;
		this.source = source;
		this.target = target;
	}


	public WorldCard card() {
		return card;
	}

	public CardPlayer source() {
		return source;
	}

	public Target target() {
		return target;
	}

	public CardType type() {
		return card.type();
	}

	public ProcChain procChain(World world) {
		List<Effect> effects = new ArrayList<>();
		List<Effect> cardEffects = card().card().expression().effects(new EffectContext().world(world).target(target()).source(source()).card(card()));
		effects.add(new PlayCardStartEffect(source(), card(), cardEffects));
		effects.addAll(cardEffects);
		effects.add(new PlayCardEndEffect(source(), card()));
		return new ProcChain(effects);
	}

	public void resolve(World world) {
		world.resolve(this);
	}

	public void resolve(GameInterface ui) {
		ui.resolve(this);
	}


	public String toString() {
		return "CardPlayedEvent{" +
				"card=" + card.card() +
				", source=" + source +
				", target=" + target +
				'}';
	}

	public void reindex(World world) {
	}

}
