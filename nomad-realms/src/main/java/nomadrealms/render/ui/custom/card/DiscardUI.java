package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Matrix4f;
import engine.common.math.Vector3f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class DiscardUI implements UI {

	private final ConstraintBox discardArea;
	private final List<UICard> discardUICards = new ArrayList<>();
	private final List<RestockTask> restockTasks = new ArrayList<>();

	public DiscardUI(ConstraintBox discardArea) {
		this.discardArea = discardArea;
	}

	public void addInitialCards(List<WorldCard> cards) {
		for (WorldCard card : cards) {
			ConstraintPair cardSize = UICard.cardSize(2.5f);
			ConstraintBox cardBox = new ConstraintBox(
					discardArea.x().add(absolute((float) Math.random() * (discardArea.w().get() - cardSize.x().get()))),
					discardArea.y().add(absolute((float) Math.random() * (discardArea.h().get() - cardSize.y().get()))),
					cardSize
			);
			UICard ui = new UICard(card, cardBox);
			ui.physics().rotate(new Vector3f(0, 0, 1), (float) (Math.random() * 40 - 20));
			ui.physics().snap();
			discardUICards.add(ui);
		}
	}

	public void addCard(WorldCard card) {
		ConstraintPair cardSize = UICard.cardSize(2.5f);
		ConstraintBox targetBox = new ConstraintBox(
				discardArea.x().add(absolute((float) Math.random() * (discardArea.w().get() - cardSize.x().get()))),
				discardArea.y().add(absolute((float) Math.random() * (discardArea.h().get() - cardSize.y().get()))),
				cardSize
		);
		ConstraintBox tempBox = new ConstraintBox(
				discardArea.center().add(cardSize.scale(-1.5f)),
				cardSize.scale(3)
		);
		ConstraintBox startBox = new ConstraintBox(
				tempBox.x(), absolute(-tempBox.h().get()),
				tempBox.w(), tempBox.h()
		);

		UICard ui = new UICard(card, startBox);
		ui.physics().rotate(new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), (float) (Math.random() * 360));
		ui.physics().targetTransform(new CardTransform(
				new engine.common.math.UnitQuaternion().rotateBy(new Vector3f(0, 0, 1), (float) (Math.random() * 40 - 20)),
				targetBox
		));
		discardUICards.add(ui);
	}

	public void restockDeck(Deck deck, ConstraintBox deckConstraint) {
		List<UICard> toRestock = discardUICards.stream()
				.filter(ui -> ui.card().deck() == deck)
				.collect(Collectors.toList());
		discardUICards.removeAll(toRestock);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < toRestock.size(); i++) {
			UICard ui = toRestock.get(i);
			ui.physics().targetTransform(new CardTransform(
					new engine.common.math.UnitQuaternion(),
					deckConstraint
			));
			restockTasks.add(new RestockTask(ui.card(), ui, deck, startTime + i * 100));
		}
	}

	public void processRestockTasks(Consumer<RestockTask> onRestockReady) {
		long currentTime = System.currentTimeMillis();
		Iterator<RestockTask> taskIterator = restockTasks.iterator();
		while (taskIterator.hasNext()) {
			RestockTask task = taskIterator.next();
			if (currentTime >= task.executeTime) {
				onRestockReady.accept(task);
				taskIterator.remove();
			}
		}
	}

	public List<UICard> discardUICards() {
		return discardUICards;
	}

	@Override
	public void render(RenderingEnvironment re) {
		renderBackground(re);
		renderCards(re);
	}

	public void renderBackground(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(180, 150, 110)))
				.set("transform", new Matrix4f(discardArea, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
	}

	public void renderCards(RenderingEnvironment re) {
		discardUICards.forEach(card -> card.render(re));
	}

	public void updateAnimations() {
		discardUICards.forEach(card -> {
			float targetY = discardArea.center().y().get() - card.physics().cardBox().h().multiply(0.5f).get();
			float t = (targetY != 0) ? card.position().y().get() / targetY : 1;
			t = Math.max(0, Math.min(1, t));
			card.physics().targetTransform().size(UICard.cardSize(2.5f).scale(1 + 2 * (1 - t)));
			card.physics().interpolate(0.2f);
		});
	}

	public ConstraintBox discardArea() {
		return discardArea;
	}
}
