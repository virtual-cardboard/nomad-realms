package nomadrealms.render.ui.custom.inventory;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static java.util.Comparator.comparingInt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.event.DropItemEvent;
import nomadrealms.game.item.UIItem;
import nomadrealms.game.item.WorldItem;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class InventoryTab implements UI {

	ConstraintBox screen;
	ConstraintBox constraintBox;
	Map<WorldItem, UIItem> itemsUI = new HashMap<>();

	CardPlayer owner;

	UIItem selectedItem;

	/**
	 *
	 */
	public InventoryTab(CardPlayer owner, ConstraintBox screen,
						List<Consumer<MousePressedInputEvent>> onClick,
						List<Consumer<MouseMovedInputEvent>> onDrag,
						List<Consumer<MouseReleasedInputEvent>> onDrop) {
		this.owner = owner;
		this.screen = screen;
		constraintBox = new ConstraintBox(
				screen.x().add(screen.w().multiply(0.2f)),
				screen.y().add(screen.h().multiply(0.2f)),
				screen.w().multiply(0.6f),
				screen.h().multiply(0.6f)
		);
		addCallbacks(onClick, onDrag, onDrop);
	}

	private void addCallbacks(List<Consumer<MousePressedInputEvent>> onClick,
							  List<Consumer<MouseMovedInputEvent>> onDrag,
							  List<Consumer<MouseReleasedInputEvent>> onDrop) {
		onClick.add(
				(event) -> {
					selectedItem = cards()
							.filter(card -> card.physics().cardBox().contains(event.mouse().coordinate()))
							.max(ySort())
							.orElse(null);
				}
		);
		onDrag.add(
				(event) -> {
					if (selectedItem != null) {
						selectedItem.move(event.offsetX(), event.offsetY());
						selectedItem.tilt(new Vector2f(event.offsetX(), event.offsetY()));
					}
				}
		);
		onDrop.add(
				(event) -> {
					if (selectedItem != null && !constraintBox.contains(selectedItem.centerPosition())) {
						owner.addNextPlay(new DropItemEvent(selectedItem.item(), owner));
					}
					selectedItem = null;
				}
		);
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (!owner.inventory().isOpen()) {
			cards().forEach(card -> card.physics().snap());
			return;
		}
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(156, 107, 82)))
							.set("transform", new Matrix4f(constraintBox, re.glContext))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
				}
		);
		itemsUI.values().removeIf(item -> item.item().owner() == null);
		owner.inventory().items().forEach(this::addUIIfAbsent);
		cards().sorted(ySort()).forEach(card -> card.render(re));
		cards().forEach(UIItem::interpolate);
		cards().filter(card -> card != selectedItem).forEach(UIItem::interpolate);
	}

	public Stream<UIItem> cards() {
		return itemsUI.values().stream();
	}

	public CardPlayer owner() {
		return owner;
	}

	public void addUIIfAbsent(WorldItem item) {
		ConstraintPair coord = constraintBox.coordinate().add(100, itemsUI.size() * 50 + 100);
		itemsUI.putIfAbsent(item, new UIItem(item, screen, coord));
	}

	public Comparator<UIItem> ySort() {
		return comparingInt(item -> (int) item.position().y().get());
	}

}
