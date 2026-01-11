package nomadrealms.context.game.zone;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.card.UICard.cardSize;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import java.util.ArrayList;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.StackIcon;

public class CardStack extends CardZone<CardStackEntry> {

	public CardStackEntry top() {
		if (cards.isEmpty()) {
			return null;
		}
		return cards.get(cards.size() - 1);
	}

	public CardStackEntry pop() {
		CardStackEntry top = top();
		if (top == null) {
			return null;
		}
		removeCard(top);
		return top;
	}

	public CardPlayedEvent get(int index) {
		return cards.get(index).event();
	}

	public void add(CardPlayedEvent event) {
		addCard(new CardStackEntry(event));
	}

	public boolean contains(CardPlayedEvent event) {
		return cards.stream().anyMatch(entry -> entry.event().equals(event));
	}

	public void clear() {
		cards = new ArrayList<>();
	}

	public void update(World world) {
		if (cards.isEmpty()) {
			return;
		}
		top().incrementCounter();
		// TODO: this counter should not be hardcoded, instead it should depend on the card's speed
		if (top().counter() >= 20) {
			CardPlayedEvent event = pop().event();
			// TODO: right here we need to resolve queries from the card expression/intents
			// TODO: if all queries returned no targets, the card fizzles
			world.procChains.add(event.procChain(world));
			event.source().lastResolvedCard(event.card());
		}
	}

	public void reinitializeAfterLoad(World world) {
		for (CardStackEntry entry : cards) {
			entry.event().reinitializeAfterLoad(world);
		}
	}

	public void render(RenderingEnvironment re, ConstraintPair screenPos) {
		Constraint padding = absolute(5);
		Constraint iconSize = absolute(50);
		Constraint length = iconSize.add(padding).multiply(5).add(padding);
		ConstraintBox box = new ConstraintBox(
				screenPos.x().add(length.multiply(0.5f).neg()),
				screenPos.y().add(TILE_VERTICAL_SPACING),
				length,
				iconSize.add(padding.multiply(2)));
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(100, 0, 0, 60)))
				.set("transform", new Matrix4f(box, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		int i = 0;
		for (CardStackEntry entry : getCards()) {
			ConstraintBox iconBox = new ConstraintBox(
					box.x().add(padding).add(iconSize.add(padding).multiply(i)),
					box.y().add(padding),
					iconSize,
					iconSize);
			new StackIcon(entry.event(), iconBox).render(re);

			// TODO: same as above, counter speed should not be hardcoded
			float progress = entry.counter() / 20.0f;

			Constraint overlayHeight = iconBox.h().multiply(1 - progress);
			ConstraintBox overlayBox = new ConstraintBox(
					iconBox.x(), iconBox.y().add(iconBox.h()).add(overlayHeight.neg()),
					iconBox.w(), overlayHeight);
			re.defaultShaderProgram
					.set("color", toRangedVector(rgba(0, 0, 0, 100)))
					.set("transform", new Matrix4f(overlayBox, re.glContext))
					.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
			i++;
		}
	}

}
