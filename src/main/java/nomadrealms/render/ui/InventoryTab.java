package nomadrealms.render.ui;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import nomadrealms.game.actor.HasInventory;
import nomadrealms.game.card.intent.DropItemIntent;
import nomadrealms.game.item.UIItem;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.constraint.ConstraintCoordinate;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static java.util.Comparator.comparingInt;
import static visuals.constraint.posdim.MultiplierPosDimConstraint.factor;

public class InventoryTab implements UI {

    ConstraintBox screen;
    ConstraintBox constraintBox;
    Map<UIItem, ConstraintBox> itemConstraints = new HashMap<>();

    private final World world;
    HasInventory owner;

    UIItem selectedItem;

    /**
     *
     */
    public InventoryTab(World world, HasInventory owner, ConstraintBox screen,
                        List<Consumer<MousePressedInputEvent>> onClick,
                        List<Consumer<MouseMovedInputEvent>> onDrag,
                        List<Consumer<MouseReleasedInputEvent>> onDrop) {
        this.world = world;
        this.owner = owner;
        this.screen = screen;
        constraintBox = new ConstraintBox(
                screen.x().add(screen.w().multiply(0.2f)),
                screen.y().add(screen.h().multiply(0.2f)),
                factor(screen.w(), 0.6f),
                factor(screen.h(), 0.6f)
        );
        owner.inventory().items().forEach(this::addUI);
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
                        System.out.println("Drop item");
                        owner.inventory().remove(selectedItem.item());
                        deleteUI(selectedItem);
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
        itemConstraints.keySet().removeIf(item -> item.item().owner() == null);
        cards().sorted(ySort()).forEach(card -> card.render(re));
        cards().forEach(UIItem::restoreOrientation);
        cards().filter(card -> card != selectedItem).forEach(UIItem::restorePosition);
    }

    public Stream<UIItem> cards() {
        return itemConstraints.keySet().stream();
    }

    public void deleteUI(WorldItem item) {
        throw new RuntimeException("Deleting items not yet implemented");
    }

    public void deleteUI(UIItem item) {
        world.proc(new DropItemIntent(owner, item.item()));
    }

    public void addUI(WorldItem item) {
        ConstraintCoordinate coord = constraintBox.coordinate().translate(100, itemConstraints.size() * 50 + 100);
        itemConstraints.put(new UIItem(item, screen, coord), constraintBox);
    }

    public Comparator<UIItem> ySort() {
        return comparingInt(item -> (int) item.position().y());
    }

}
