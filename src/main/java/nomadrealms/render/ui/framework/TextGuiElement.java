package nomadrealms.render.ui.framework;

import nomadrealms.render.RenderingEnvironment;
// For placeholder rendering, we might need access to drawing capabilities.
// This is highly dependent on the engine. For now, we'll focus on structure
// and very basic placeholder rendering or logging.
// import java.awt.FontMetrics; // Would be used for accurate text measurement
// import java.awt.Graphics2D; // Would be used for text rendering in AWT/Swing

public class TextGuiElement extends GuiElement {

    private String text = "";
    // Placeholder font properties for naive measurement
    private float estimatedCharWidth = 8f; // Average width per character
    private float estimatedLineHeight = 15f; // Height per line of text

    public TextGuiElement(GuiElement parent) {
        super(parent);
        // Text elements typically wrap their content by default.
        this.widthRule = nomadrealms.render.ui.framework.sizing.SizeRule.wrapContent();
        this.heightRule = nomadrealms.render.ui.framework.sizing.SizeRule.wrapContent();
    }

    public TextGuiElement(GuiElement parent, String text) {
        this(parent);
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null) {
            this.text = "";
        } else {
            this.text = text;
        }
        markMeasureDirty(); // Text change affects size
    }

    // Basic naive width calculation
    private float calculateNaiveWidth() {
        if (text.isEmpty()) {
            return 0;
        }
        // This doesn't handle multiline text properly for width if WRAP_CONTENT.
        // Assumes longest line if text were pre-split, or just full length.
        // A proper implementation needs font metrics.
        return text.lines().mapToInt(String::length).max().orElse(0) * estimatedCharWidth;
    }

    // Basic naive height calculation
    private float calculateNaiveHeight() {
        if (text.isEmpty()) {
            return 0;
        }
        long lineCount = text.lines().count();
        return lineCount * estimatedLineHeight;
    }

    @Override
    protected float calculateMeasuredContentWidth(RenderingEnvironment re) {
        return calculateNaiveWidth();
    }

    @Override
    protected float calculateMeasuredContentHeight(RenderingEnvironment re) {
        return calculateNaiveHeight();
    }

    @Override
    protected void arrangeChildren(RenderingEnvironment re) {
        // TextGuiElement does not have children
    }

    @Override
    public void render(RenderingEnvironment re) {
        if (this.parent == null) { // If this is the root element
            measureAndLayout(re);
        }

        // Placeholder for actual text rendering.
        // This is highly dependent on the rendering engine and font system.
        // For now, we can print to console or try a very basic visual if possible.
        if (this.constraintBox != null && this.constraintBox.w().get() > 0 && this.constraintBox.h().get() > 0) {
            // System.out.println("Render Text: '" + text + "' at X:" + constraintBox.x().get() + " Y:" + constraintBox.y().get() +
            //                    " W:" + constraintBox.w().get() + " H:" + constraintBox.h().get());

            // If there was a simple way to draw text provided by RenderingEnvironment:
            // re.textRenderer.drawText(text, constraintBox.x().get(), constraintBox.y().get(), someFont, someColor);

            // As a visual placeholder, draw a rectangle with a different color
            // This is just to show it's being placed and sized.
            // Assuming 'common.math.Matrix4f' and 'common.colour.Colour' are the correct paths
            // as seen in other UI framework files if they are from an external library.
            // If they are part of nomadrealms, the path would be nomadrealms.common...
            common.math.Matrix4f transform = new common.math.Matrix4f(this.constraintBox, re.glContext);
            re.defaultShaderProgram
                .set("color", common.colour.Colour.toRangedVector(common.colour.Colour.rgb(50, 200, 50))) // Greenish
                .set("transform", transform)
                .use(new visuals.lwjgl.render.meta.DrawFunction().vao(visuals.builtin.RectangleVertexArrayObject.instance()).glContext(re.glContext));
        }
        // No children to render
    }
}
