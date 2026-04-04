package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;

import java.util.List;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.SummonCreatureEffect;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class SummonCreatureExpression implements CardExpression {

	private String name;
	private String image;
	private int health;
	private int mana;
	private GameCard[] cards;

	public SummonCreatureExpression() {
	}

	public static SummonCreatureExpression summonCreature() {
		return new SummonCreatureExpression();
	}

	public SummonCreatureExpression name(String name) {
		this.name = name;
		return this;
	}

	public SummonCreatureExpression image(String image) {
		this.image = image;
		return this;
	}

	public SummonCreatureExpression health(int health) {
		this.health = health;
		return this;
	}

	public SummonCreatureExpression mana(int mana) {
		this.mana = mana;
		return this;
	}

	public SummonCreatureExpression cards(GameCard... cards) {
		this.cards = cards;
		return this;
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new SummonCreatureEffect(source, (Tile) target, name, image, health, mana, cards));
	}

}
