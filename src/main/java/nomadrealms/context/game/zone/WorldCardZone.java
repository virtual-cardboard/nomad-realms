package nomadrealms.context.game.zone;

import nomadrealms.context.game.card.WorldCard;

import nomadrealms.context.game.world.World;

public class WorldCardZone extends CardZone<WorldCard> {

	@Override
	public WorldCardZone addCard(WorldCard card) {
		super.addCard(card);
		card.zone(this);
		return this;
	}

	public void reinitializeAfterLoad(World world) {
		for (WorldCard card : cards) {
			card.zone(this);
		}
	}

}
