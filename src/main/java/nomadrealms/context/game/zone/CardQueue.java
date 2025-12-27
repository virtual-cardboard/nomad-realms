package nomadrealms.context.game.zone;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.card.UICard.cardSize;
import static nomadrealms.context.game.world.map.area.Tile.TILE_VERTICAL_SPACING;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

public class CardQueue extends CardZone<CardPlayedEvent> {

	private int counter = 0;

	private LinkedList<CardPlayedEvent> queue;

	public CardQueue() {
		queue = new LinkedList<>();
	}

	public void addCardPlayedEvent(CardPlayedEvent event) {
		queue.add(event);
	}

	public CardPlayedEvent getNextCardPlayedEvent() {
		return queue.poll();
	}

	public List<CardPlayedEvent> getCards() {
		return new LinkedList<>(queue);
	}

	public int size() {
		return queue.size();
	}

	public CardPlayedEvent get(int index) {
		return queue.get(index);
	}

	public void add(CardPlayedEvent event) {
		queue.add(event);
	}

	public boolean contains(CardPlayedEvent event) {
		return queue.contains(event);
	}

	public void remove(CardPlayedEvent event) {
		queue.remove(event);
	}

	public void clear() {
		queue.clear();
	}

	/**
	 * Returns a copy of the queue
	 *
	 * @return a copy of the queue
	 */
	public Queue<CardPlayedEvent> getQueue() {
		return new LinkedList<>(queue);
	}

	public void update(World world) {
		if (queue.isEmpty()) {
			return;
		}
		counter++;
		// TODO: this counter should not be hardcoded, instead it should depend on the card's speed
		if (counter == 10) {
			counter = 0;
			CardPlayedEvent event = queue.pop();
			world.procChains.add(event.procChain(world));
			event.source().lastResolvedCard(event.card());
		}
	}

	public void reinitializeAfterLoad(World world) {
		for (CardPlayedEvent event : queue) {
			event.reinitializeAfterLoad(world);
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
		for (CardPlayedEvent event : queue) {
			event.ui().physics().targetCoord(
					new ConstraintPair(
							box.x().add(padding).add(cardSize(0.4f).x().add(padding).multiply(i)),
							box.y().add(padding))).snap();
			event.render(re);
			ConstraintBox cardBox = event.ui().physics().cardBox();
			Constraint overlayHeight = cardBox.h().multiply(1 - counter / 10.0f);
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
