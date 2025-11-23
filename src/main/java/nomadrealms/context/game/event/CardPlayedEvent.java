package nomadrealms.context.game.event;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.Card;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.card.intent.PlayCardEndIntent;
import nomadrealms.context.game.card.intent.PlayCardStartIntent;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.game.GameInterface;

public class CardPlayedEvent implements InputEvent, Card {

	private transient UICard card;

	CardPlayer source;
	Target target;

	/**
	 * No-arg constructor for serialization.
	 */
	private CardPlayedEvent() {
	}

	public CardPlayedEvent(WorldCard card, CardPlayer source, Target target) {
		this.card = new UICard(card, new ConstraintBox(absolute(0), absolute(0), UICard.cardSize(1)));
		this.source = source;
		this.target = target;
	}

	public void render(RenderingEnvironment re) {
		card.render(re);
	}

	public UICard card() {
		return card;
	}

	public CardPlayer source() {
		return source;
	}

	public Target target() {
		return target;
	}

	public ProcChain procChain(World world) {
		List<Intent> intents = new ArrayList<>();
		intents.add(new PlayCardStartIntent(card().card()));
		intents.addAll(card().card().card().expression().intents(world, target(), source()));
		intents.add(new PlayCardEndIntent(source(), card().card()));
		return new ProcChain(intents);
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
				"card=" + card.card() +
				", source=" + source +
				", target=" + target +
				'}';
	}

}
