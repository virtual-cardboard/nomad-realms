package nomadrealms.game.event;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.Card;
import nomadrealms.game.card.UICard;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.PlayCardEndIntent;
import nomadrealms.game.card.intent.PlayCardStartIntent;
import nomadrealms.game.world.World;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.GameInterface;

public class CardPlayedEvent implements InputEvent, Card {

	UICard card;
	CardPlayer source;
	Target target;

	public CardPlayedEvent(WorldCard card, CardPlayer source, Target target) {
		this.card = new UICard(card, new ConstraintBox(absolute(0), absolute(0), UICard.cardSize(1)));
		this.source = source;
		this.target = target;
	}

	public void render(RenderingEnvironment re) {
		System.out.println("Rendering card: " + card);
		System.out.println("at " + card.position());
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
