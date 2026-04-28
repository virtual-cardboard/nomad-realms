package nomadrealms.render.ui.custom.card;

import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.zone.Deck;

public class RestockTask {
	public WorldCard card;
	public UICard ui;
	public Deck deck;
	public long executeTime;

	public RestockTask(WorldCard card, UICard ui, Deck deck, long executeTime) {
		this.card = card;
		this.ui = ui;
		this.deck = deck;
		this.executeTime = executeTime;
	}
}
