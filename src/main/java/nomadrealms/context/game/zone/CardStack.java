package nomadrealms.context.game.zone;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.util.Collections.singletonList;
import static nomadrealms.context.game.actor.status.StatusEffect.POISON;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;

import java.util.ArrayList;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.World;
import nomadrealms.render.RenderingEnvironment;
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

	public void clear() {
		cards = new ArrayList<>();
	}

	public void update(World world) {
		if (cards.isEmpty()) {
			return;
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

	public void reinitializeAfterLoad(World world) {
		for (CardStackEntry entry : cards) {
			entry.event().reinitializeAfterLoad(world);
		}
	}

	public void render(RenderingEnvironment re, ConstraintPair screenPos) {
		Constraint padding = absolute(2);
		Constraint iconSize = absolute(15);
		Constraint height = iconSize.add(padding).multiply(5).add(padding);
		Constraint width = iconSize.add(padding.multiply(2));
		ConstraintBox box = new ConstraintBox(
				screenPos.x().add(absolute(TILE_RADIUS / 4)).add(PADDING),
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
			new StackIcon(entry.event(), iconBox).render(re);

			Constraint overlayHeight = iconBox.h().multiply(1 - entry.getProgress());
			ConstraintBox overlayBox = new ConstraintBox(
					iconBox.x(), iconBox.y().add(iconBox.h()).add(overlayHeight.neg()),
					iconBox.w(), overlayHeight);
			re.defaultShaderProgram
					.set("color", toRangedVector(rgba(0, 0, 0, 100)))
					.set("transform", new Matrix4f(overlayBox, re.glContext))
					.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

			if (iconBox.contains(re.mouse.coordinate())) {
				ConstraintBox cardBox = new ConstraintBox(
						iconBox.x().add(iconBox.w()).add(padding),
						iconBox.y().add(iconBox.h().multiply(0.5f)).add(UICard.cardSize(1.5f).y().multiply(-0.5f)),
						UICard.cardSize(1.5f)
				);
				new UICard(entry.event().card(), cardBox).render(re);
			}
			i++;
		}
	}

}
