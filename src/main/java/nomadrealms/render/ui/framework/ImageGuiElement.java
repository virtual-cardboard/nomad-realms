package nomadrealms.render.ui.framework;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.RenderingEnvironment;
// Assuming a Texture class or similar exists for holding image data
// import nomadrealms.render.Texture;
import common.math.Matrix4f; // From existing UI code
import common.colour.Colour; // From existing UI code
import visuals.builtin.RectangleVertexArrayObject; // From existing UI code
import visuals.constraint.posdim.AbsoluteConstraint;
import visuals.lwjgl.render.meta.DrawFunction; // From existing UI code

import visuals.lwjgl.render.meta.DrawFunction; // From existing UI code

public class ImageGuiElement extends GuiElement {

    // private Texture texture; // Placeholder for actual image data
    private float actualImageWidth = 0; // Actual width of the loaded image
    private float actualImageHeight = 0; // Actual height of the loaded image

    // TODO: Add a constructor that takes an image path or Texture object
    public ImageGuiElement(GuiElement parent) {
        super(parent);
        // Default to wrap_content for images, meaning they want to be their actual size.
        // Parent container can then decide to override this (e.g. stretch the image).
    }

    // TODO: Method to set the image and update actualImageWidth/Height
    // public void setImage(Texture texture) {
    //     this.texture = texture;
    //     if (texture != null) {
    //         this.actualImageWidth = texture.getWidth();
    //         this.actualImageHeight = texture.getHeight();
    //     } else {
    //         this.actualImageWidth = 0;
    //         this.actualImageHeight = 0;
    //     }
    //     markMeasureDirty(); // Size has changed
    // }

    // Setter for placeholder dimensions until image loading is integrated
    public void setDebugImageSize(float width, float height) {
        this.actualImageWidth = width;
        this.actualImageHeight = height;
        markMeasureDirty();
    }

    @Override
    protected float calculateMeasuredContentWidth(RenderingEnvironment re) {
        // The "content" width for an image is simply its actual width.
        return actualImageWidth;
    }

    @Override
    protected float calculateMeasuredContentHeight(RenderingEnvironment re) {
        // The "content" height for an image is its actual height.
        return actualImageHeight;
    }

    @Override
    protected void arrangeChildren(RenderingEnvironment re) {
        // ImageGuiElement does not have children, so this method is empty.
    }

    @Override
    public void render(RenderingEnvironment re) {
        // The measureAndLayout call is handled by the root or parent's render call.
        // super.render(re) will handle calling measureAndLayout if this element is root.
        // If not root, its layout is determined by its parent.

        // GuiElement.render() calls measureAndLayout if this is root.
        // Then it renders children. We need to insert this element's specific rendering here.
        if (this.parent == null) { // If this is the root element (unlikely for an image, but for completeness)
            measureAndLayout(re);
        }

        // Actual rendering logic for the image
        // The constraintBox should now have its final, calculated values.
        if (this.constraintBox != null && this.constraintBox.w().get() > 0 && this.constraintBox.h().get() > 0) {
            // This is a placeholder rendering using a colored rectangle.
            // In a real scenario, this would bind the image texture and render a textured quad.
            // if (texture != null) {
            // re.textureRenderer.render(texture, constraintBox.x().get(), constraintBox.y().get(), constraintBox.w().get(), constraintBox.h().get());
            // } else {
            re.defaultShaderProgram
                .set("color", Colour.toRangedVector(Colour.rgb(100, 100, 200))) // A distinct color for debug
                .set("transform", new Matrix4f(this.constraintBox, re.glContext))
                .use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
            // }
        }

        // ImageGuiElement does not have children, so no need to call super.render()'s child loop.
        // However, the base render also handles calling measureAndLayout if it's a root.
        // The current GuiElement.render is:
        // if (this.parent == null) { measureAndLayout(re); }
        // ... render this ...
        // for (GuiElement child : children) { child.render(re); }
        // So, we don't need to call super.render() if we've done the above.
    }
}
