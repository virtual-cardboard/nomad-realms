package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;

public class MaterializeItemEffect extends Effect {

	private final Item item;

	public MaterializeItemEffect(CardPlayer source, Item item) {
		super(source);
		this.item = item;
	}

	@Override
	public void resolve(World world) {
		((CardPlayer) source()).inventory().add(new WorldItem(item));
	}

}
