package nomadrealms.context.game.card.intent;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.world.World;

public class SurfaceCardIntent implements Intent {

	private final CardPlayer source;
	private final Query<WorldCard> query;

	public SurfaceCardIntent(CardPlayer source, Query<WorldCard> query) {
		this.source = source;
		this.query = query;
	}

	@Override
	public void resolve(World world) {
		for (WorldCard card : query.find(world, source)) {
			System.out.println("Surfacing card: " + card);
			card.zone().surface(card);
		}
	}

	public CardPlayer source() {
		return source;
	}

}
