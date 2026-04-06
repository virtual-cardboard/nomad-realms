package nomadrealms.context.game.card.expression;

import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SummonCreatureEffect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.event.game.effect.EffectContext;

import java.util.List;

import static java.util.Collections.singletonList;

public class SummonCreatureExpression implements CardExpression {

	private final String name;
	private final int health;
	private final int mana;
	private final String image;
	private final DeckList deck;

	public SummonCreatureExpression(String name, int health, int mana, String image, DeckList deck) {
		this.name = name;
		this.health = health;
		this.mana = mana;
		this.image = image;
		this.deck = deck;
	}

	public static SummonCreatureExpression summonCreature(String name, int health, int mana, String image, DeckList deck) {
		return new SummonCreatureExpression(name, health, mana, image, deck);
	}

	@Override
	public List<Effect> effects(EffectContext context) {
		return singletonList(new SummonCreatureEffect(context.source(), name, (Tile) context.target(), health, mana, image, deck));
	}

}
