package nomadrealms.context.game.zone;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.actor.status.StatusEffect.POISON;
import static nomadrealms.context.game.card.CardKeyword.HEAVY;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import static java.util.Collections.singletonList;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.ArrayList;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.Arrow;
import nomadrealms.render.ui.custom.card.StackIcon;

public class CardStack extends CardZone<CardStackEntry> {

	private static final int PADDING = 5;

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

	public void update(World world) {
		if (cards.isEmpty()) {
			return;
		}
		if (cards.size() > 1 && top().keywords().contains(HEAVY)) {
			CardStackEntry heavyCard = cards.remove(cards.size() - 1);
			cards.add(0, heavyCard);
		}
		top().incrementCounter();
		if (top().isReady()) {
			CardPlayedEvent event = pop().event();
			if (event.source().status().count(POISON) > 0) {
				world.procChains.add(new ProcChain(singletonList(new DamageEffect(event.source(), event.source(), 1))));
				event.source().status().remove(POISON, 1);
			}
			// TODO: if all queries returned no targets, the card fizzles
			world.procChains.add(event.procChain(world));
			event.source().lastResolvedCard(event.card());
		}
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(World world) {
		for (CardStackEntry entry : cards) {
			entry.event().reindex(world);
		}
	}

	public void render(RenderingEnvironment re, ConstraintPair screenPos) {
		float zoom = re.is.camera.zoom().get();
		if (zoom < 2.0f) {
			renderZoomedOut(re, screenPos, zoom);
		} else {
			renderNormal(re, screenPos);
		}
	}

	private void renderZoomedOut(RenderingEnvironment re, ConstraintPair screenPos, float zoom) {
		int numCards = getCards().size();
		int maxSlots = 5;
		float dotRadius = Math.max(1.25f, 3.0f * zoom);
		float dotSpacing = dotRadius * 2.5f;
		float pad = dotRadius * 1.5f;

		float capsuleWidth = dotRadius * 2 + pad * 2;
		float capsuleHeight = (maxSlots - 1) * dotSpacing + dotRadius * 2 + pad * 2;

		float posX = screenPos.x().get() + (TILE_RADIUS / 4 + PADDING) * zoom;
		float posY = screenPos.y().get() - capsuleHeight * 0.5f;
		float cornerRadius = capsuleWidth * 0.5f;

		int fillColor = rgba(30, 30, 30, 140);
		int borderColor = rgba(255, 255, 255, 60);

		re.rectangleRenderer.render(posX, posY, capsuleWidth, capsuleHeight, cornerRadius, fillColor, borderColor, Math.max(1.0f, 1.0f * zoom));

		float dotCx = posX + capsuleWidth * 0.5f;
		for (int i = 0; i < numCards; i++) {
			CardStackEntry entry = cards.get(i);
			float dotCy = posY + capsuleHeight - pad - dotRadius - i * dotSpacing;

			int dotColor = (i == numCards - 1) ? rgba(255, 200, 100, 240) : rgba(210, 180, 140, 220);
			re.circleRenderer.render(dotCx, dotCy, dotRadius, dotColor);

			ConstraintBox dotBox = new ConstraintBox(
					absolute(dotCx - dotRadius - 2),
					absolute(dotCy - dotRadius - 2),
					absolute(dotRadius * 2 + 4),
					absolute(dotRadius * 2 + 4)
			);
			if (dotBox.contains(re.is.mouse.coordinate())) {
				ConstraintBox cardBox = new ConstraintBox(
						absolute(posX + capsuleWidth + 5),
						absolute(dotCy - UICard.cardSize(1.5f).y().get() * 0.5f),
						UICard.cardSize(1.5f)
				);
				UICard uiCard = new UICard(entry.event().card(), cardBox);
				if (entry.event().target() != null) {
					new Arrow(uiCard.centerPosition(), entry.event().target().tile().getScreenPosition(re))
							.targetCenter(entry.event().target().tile().getScreenPosition(re)).render(re);
				}
				uiCard.render(re);
			}
		}
	}

	private void renderNormal(RenderingEnvironment re, ConstraintPair screenPos) {
		Constraint padding = absolute(2).multiply(re.is.camera.zoom());
		Constraint iconSize = absolute(15).multiply(re.is.camera.zoom());
		Constraint height = iconSize.add(padding).multiply(5).add(padding);
		Constraint width = iconSize.add(padding.multiply(2));
		ConstraintBox box = new ConstraintBox(
				screenPos.x().add(absolute(TILE_RADIUS / 4).add(PADDING).multiply(re.is.camera.zoom())),
				screenPos.y().add(height.multiply(0.5f).neg()),
				width, height);
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(100, 0, 0, 60)))
				.set("transform", new Matrix4f(box, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		int i = 0;
		for (CardStackEntry entry : getCards()) {
			ConstraintBox iconBox = new ConstraintBox(
					box.x().add(padding),
					box.y().add(box.h()).add(padding.neg()).add(iconSize.neg())
							.add(iconSize.add(padding).multiply(i).neg()),
					iconSize, iconSize);
			entry.icon().constraintBox(iconBox).render(re);

			Constraint overlayHeight = iconBox.h().multiply(1 - entry.getProgress());
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
