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

	private final String name;
	private final String image;
	private final int health;
	private final GameCard[] cards;

	public SummonCreatureExpression(String name, String image, int health, GameCard... cards) {
		this.name = name;
		this.image = image;
		this.health = health;
		this.cards = cards;
	}

	public static SummonCreatureExpression summonCreature(String name, String image, int health, GameCard... cards) {
		return new SummonCreatureExpression(name, image, health, cards);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		return singletonList(new SummonCreatureEffect(source, (Tile) target, name, image, health, cards));
	}

}
