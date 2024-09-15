package nomadrealms.render.ui;

import common.math.Matrix4f;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;

public class DebugTab implements UI {
    CardPlayer owner;
    ConstraintBox screen;
    ConstraintBox constraintBox;
    public DebugTab(CardPlayer owner, ConstraintBox screen) {
        this.owner = owner;
        this.screen = screen;
        constraintBox = new ConstraintBox(
                absolute(0),
                absolute(0),
                screen.w().multiply(0.3f),
                screen.h().multiply(0.3f));
    }

    @Override
    public void render(RenderingEnvironment re) {
        DefaultFrameBuffer.instance().render(
                () -> {
                    re.defaultShaderProgram
                            .set("color", toRangedVector(rgb(210, 180, 140)))
                            .set("transform", new Matrix4f(constraintBox, re.glContext))
                            .use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
                }
        );
    }

}
