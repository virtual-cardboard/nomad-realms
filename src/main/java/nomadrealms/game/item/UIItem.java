package nomadrealms.game.item;

import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.CardPhysics;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.constraint.ConstraintSize;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 */
public class UIItem {

    public final ConstraintBox basePosition;
    private final CardPhysics physics;

    private final WorldItem item;

    public UIItem(WorldItem item, ConstraintBox screen, ConstraintBox basePosition) {
        this.item = item;
        this.basePosition = basePosition;
        physics = new CardPhysics(UIItem.size(screen, 2)).targetCoord(basePosition.coordinate()).snap();
    }

    public void render(RenderingEnvironment re) {
        DefaultFrameBuffer.instance().render(
                () -> {
                    // Simple rotate
                    re.defaultShaderProgram
                            .set("color", toRangedVector(rgb(100, 0, 0)))
                            .set("transform", physics.cardTransform(
                                    re.glContext,
                                    new Vector3f(0, 0, 0),
                                    new Vector2f(basePosition.w().get(), basePosition.h().get())))
                            .use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

                    re.textRenderer
                            .render(physics.cardTransform(
                                            re.glContext,
                                            new Vector3f(0, 0, 0),
                                            new Vector2f(1, 1)),
                                    item.item.name(), 0, re.font, 20f, rgb(255, 255, 255));
                    re.textRenderer
                            .render(physics.cardTransform(
                                            re.glContext,
                                            new Vector3f(0, 40, 0),
                                            new Vector2f(1, 1)),
                                    item.item.description(), 100, re.font, 15f, rgb(255, 255, 255));
                }
        );
    }

    public WorldItem item() {
        return item;
    }

    public void restoreOrientation() {
        physics.restoreOrientation();
    }

    public void restorePosition() {
        physics.restorePosition();
    }

    public void move(int x, int y) {
        physics.cardPos = physics.cardPos.add(x, y);
    }

    public void tilt(Vector2f velocity) {
        physics.tilt(velocity);
    }

    public static ConstraintSize size(ConstraintBox screen, float scale) {
        return new ConstraintSize(
                absolute(100),
                absolute(100)
        );
    }

    public Vector2f position() {
        return physics.position();
    }

    public Vector2f centerPosition() {
        return physics.centerPosition();
    }

    public CardPhysics physics() {
        return physics;
    }

}
