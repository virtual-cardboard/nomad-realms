package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.rendering.text.GameFont;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.render.ui.content.ContainerContent;
import nomadrealms.render.ui.content.TextContent;
import nomadrealms.render.ui.content.UIContent;

public class DeckListCardUI extends ContainerContent {

	public DeckListCardUI(UIContent parent, GameCard card, ConstraintBox box, GameFont font) {
		super(parent, box);
		fill(rgb(80, 80, 80));

		TextContent manaText = new TextContent(
				String.valueOf(card.manaCost()),
				0, 14, font,
				box.coordinate().add(absolute(8), absolute(4))
		);
		addChild(manaText);

		TextContent titleText = new TextContent(
				card.title(),
				0, 14, font,
				box.coordinate().add(absolute(35), absolute(4))
		);
		addChild(titleText);

		TextContent resText = new TextContent(
				String.valueOf(card.resolutionTime()),
				0, 14, font,
				box.coordinate().add(box.w().add(absolute(-20)), absolute(4))
		);
		addChild(resText);
	}

}
