package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.List;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.rendering.text.GameFont;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.render.ui.content.ContainerContent;
import nomadrealms.render.ui.content.TextContent;
import nomadrealms.render.ui.content.UIContent;

public class DeckListUI extends ContainerContent {

	public DeckListUI(UIContent parent, String title, DeckList deckList, ConstraintBox box, GameFont font) {
		super(parent, box);
		fill(rgb(50, 50, 50));

		TextContent text = new TextContent(
				title,
				0, 20, font,
				box.coordinate(),
				10.0f
		);
		addChild(text);

		List<GameCard> cards = deckList.getCards();
		float cardStripHeight = 25.0f;
		float cardStripPadding = 4.0f;
		float startY = 40.0f;
		for (int j = 0; j < cards.size(); j++) {
			GameCard card = cards.get(j);
			ConstraintBox stripBox = new ConstraintBox(
					box.x().add(absolute(10)),
					box.y().add(absolute(startY + j * (cardStripHeight + cardStripPadding))),
					box.w().add(absolute(-20)),
					absolute(cardStripHeight)
			);
			addChild(new DeckListCardUI(this, card, stripBox, font));
		}
	}

}
