package nomadrealms.context.game.zone;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.util.stream.Collectors.toList;
import static nomadrealms.context.game.card.UICard.cardSize;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import java.util.LinkedList;
import java.util.List;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;

public class CardStack extends CardZone<CardPlayedEvent> {

	private final LinkedList<CardStackEntry> stack;

	public CardStack() {
		stack = new LinkedList<>();
	}

	@Override
	public List<CardPlayedEvent> getCards() {
		return stack.stream().map(CardStackEntry::event).collect(toList());
	}

	public CardPlayedEvent top() {
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek().event();
	}

	public int size() {
		return stack.size();
	}

	public CardPlayedEvent get(int index) {
		return stack.get(index).event();
	}

	public void add(CardPlayedEvent event) {
		stack.push(new CardStackEntry(event));
	}

	public boolean contains(CardPlayedEvent event) {
		return stack.stream().anyMatch(entry -> entry.event().equals(event));
	}

	public void remove(CardPlayedEvent event) {
		stack.removeIf(entry -> entry.event().equals(event));
	}

	public void clear() {
		stack.clear();
	}

	public void update(World world) {
		if (stack.isEmpty()) {
			return;
		}
		CardStackEntry topEntry = stack.peek();
		topEntry.incrementCounter();
		// TODO: this counter should not be hardcoded, instead it should depend on the card's speed
		if (topEntry.counter() >= 10) {
			CardStackEntry resolvedEntry = stack.pop();
			CardPlayedEvent event = resolvedEntry.event();
			world.procChains.add(event.procChain(world));
			event.source().lastResolvedCard(event.card());
		}
	}

	public void reinitializeAfterLoad(World world) {
		for (CardStackEntry entry : stack) {
			entry.event().reinitializeAfterLoad(world);
		}
	}

	public void render(RenderingEnvironment re, Vector2f screenPos) {
		Constraint padding = absolute(5);
		ConstraintPair cardSize = cardSize(0.4f);
		Constraint length = cardSize.x().add(padding).multiply(5).add(padding);
		ConstraintBox box =
				new ConstraintBox(
						absolute(screenPos.x()).add(length.multiply(0.5f).neg()),
						absolute(screenPos.y()).add(cardSize.y().multiply(0.5f).add(absolute(TILE_VERTICAL_SPACING)).neg()),
						length,
						cardSize.y());
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(100, 0, 0, 60)))
				.set("transform", new Matrix4f(box, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		int i = 0;
		for (CardStackEntry entry : stack) {
			int reversedI = stack.size() - 1 - i;
			CardPlayedEvent event = entry.event();
			event.ui().physics().targetCoord(
					new ConstraintPair(
							box.x().add(padding).add(cardSize(0.4f).x().add(padding).multiply(reversedI)),
							box.y().add(padding))).snap();
			event.render(re);

			// TODO: same as above, counter speed should not be hardcoded
			float progress = entry.counter() / 10.0f;

			ConstraintBox cardBox = event.ui().physics().cardBox();
			Constraint overlayHeight = cardBox.h().multiply(1 - progress);
			ConstraintBox overlayBox = new ConstraintBox(
					cardBox.x(), cardBox.y().add(cardBox.h()).add(overlayHeight.neg()),
					cardBox.w(), overlayHeight);
			re.defaultShaderProgram
					.set("color", toRangedVector(rgba(0, 0, 0, 100)))
					.set("transform", new Matrix4f(overlayBox, re.glContext))
					.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
			i++;
		}
	}

}
