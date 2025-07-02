package nomadrealms.render.ui.framework;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.framework.sizing.SizeRule;
import visuals.constraint.posdim.AbsoluteConstraint;
// Import other necessary constraint types if used for positioning later
// import visuals.constraint.posdim.Constraint;
// import visuals.constraint.posdim.MultiplierConstraint;
// import visuals.constraint.posdim.RelativeConstraint;

public class FlowGuiContainer extends GuiElement {

    public enum FlowDirection {
        HORIZONTAL,
        VERTICAL
    }

    private FlowDirection direction = FlowDirection.HORIZONTAL;
    private float spacing = 0; // Spacing between elements
    // TODO: Add alignment properties (e.g., align items to start, center, end of the cross-axis for primary and secondary axes)
    // e.g. justification (main axis), alignment (cross axis)

    public FlowGuiContainer(GuiElement parent) {
        super(parent);
        // Default SizeRules for a FlowGuiContainer should be WRAP_CONTENT,
        // as it typically sizes itself based on its children.
        this.widthRule = SizeRule.wrapContent();
        this.heightRule = SizeRule.wrapContent();
    }

    public FlowGuiContainer(GuiElement parent, FlowDirection direction) {
        this(parent);
        this.direction = direction;
    }

    public FlowDirection getDirection() {
        return direction;
    }

    public void setDirection(FlowDirection direction) {
        if (this.direction != direction) {
            this.direction = direction;
            markLayoutDirty();
        }
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        if (this.spacing != spacing) {
            this.spacing = spacing;
            markMeasureDirty(); // Direction change affects layout
        }
    }

    @Override
    protected float calculateMeasuredContentWidth(RenderingEnvironment re) {
        float totalWidth = 0;
        float maxChildHeightForHorizontal = 0; // Used if this container wraps height for horizontal flow

        if (direction == FlowDirection.HORIZONTAL) {
            for (int i = 0; i < children.size(); i++) {
                GuiElement child = children.get(i);
                // Child's width is determined by its own rules, possibly influenced by this container's width if child is 'stretch' or 'percentage'
                // For WRAP_CONTENT, we need the child's measuredContentWidth.
                // The measureAndLayout in GuiElement should have already called child.measureAndLayout if we are WRAP_CONTENT.
                totalWidth += child.widthRule.calculateEffectiveSize(child, this.constraintBox.w().get(), child.measuredContentWidth, re);
                if (i < children.size() - 1) {
                    totalWidth += spacing;
                }
                maxChildHeightForHorizontal = Math.max(maxChildHeightForHorizontal,
                    child.heightRule.calculateEffectiveSize(child, this.constraintBox.h().get(), child.measuredContentHeight, re));
            }
            // If this container's height rule is WRAP_CONTENT, its measured content height would be maxChildHeightForHorizontal.
            // This is handled in calculateMeasuredContentHeight.
        } else { // VERTICAL
            float maxWidthForVertical = 0;
            for (GuiElement child : children) {
                maxWidthForVertical = Math.max(maxWidthForVertical,
                    child.widthRule.calculateEffectiveSize(child, this.constraintBox.w().get(), child.measuredContentWidth, re));
            }
            totalWidth = maxWidthForVertical;
        }
        return totalWidth;
    }

    @Override
    protected float calculateMeasuredContentHeight(RenderingEnvironment re) {
        float totalHeight = 0;
        float maxChildWidthForVertical = 0; // Used if this container wraps width for vertical flow

        if (direction == FlowDirection.VERTICAL) {
            for (int i = 0; i < children.size(); i++) {
                GuiElement child = children.get(i);
                totalHeight += child.heightRule.calculateEffectiveSize(child, this.constraintBox.h().get(), child.measuredContentHeight, re);
                if (i < children.size() - 1) {
                    totalHeight += spacing;
                }
                 maxChildWidthForVertical = Math.max(maxChildWidthForVertical,
                    child.widthRule.calculateEffectiveSize(child, this.constraintBox.w().get(), child.measuredContentWidth, re));
            }
            // If this container's width rule is WRAP_CONTENT, its measured content width would be maxChildWidthForVertical.
            // This is handled in calculateMeasuredContentWidth.
        } else { // HORIZONTAL
            float maxHeightForHorizontal = 0;
            for (GuiElement child : children) {
                maxHeightForHorizontal = Math.max(maxHeightForHorizontal,
                    child.heightRule.calculateEffectiveSize(child, this.constraintBox.h().get(), child.measuredContentHeight, re));
            }
            totalHeight = maxHeightForHorizontal;
        }
        return totalHeight;
    }

    @Override
    protected void arrangeChildren(RenderingEnvironment re) {
        if (this.constraintBox == null) return;

        float currentX = this.constraintBox.x().get();
        float currentY = this.constraintBox.y().get();
        float containerWidth = this.constraintBox.w().get();
        float containerHeight = this.constraintBox.h().get();

        for (GuiElement child : children) {
            // Child's final size is determined by its rules relative to this container's *final* size.
            float childFinalWidth = child.getWidthRule().calculateEffectiveSize(child, containerWidth, child.measuredContentWidth, re);
            float childFinalHeight = child.getHeightRule().calculateEffectiveSize(child, containerHeight, child.measuredContentHeight, re);

            child.getConstraintBox().w(new AbsoluteConstraint(childFinalWidth));
            child.getConstraintBox().h(new AbsoluteConstraint(childFinalHeight));

            // Positioning logic (simple for now, no advanced alignment)
            if (direction == FlowDirection.HORIZONTAL) {
                child.getConstraintBox().x(new AbsoluteConstraint(currentX));
                // Basic top alignment for horizontal flow.
                // TODO: Implement cross-axis alignment (e.g., center child vertically)
                child.getConstraintBox().y(new AbsoluteConstraint(currentY));
                currentX += childFinalWidth + spacing;
            } else { // VERTICAL
                // Basic left alignment for vertical flow.
                // TODO: Implement cross-axis alignment (e.g., center child horizontally)
                child.getConstraintBox().x(new AbsoluteConstraint(currentX));
                child.getConstraintBox().y(new AbsoluteConstraint(currentY));
                currentY += childFinalHeight + spacing;
            }
            // The recursive call to child.measureAndLayout in GuiElement.measureAndLayout
            // will handle arranging the child's own children if it's a container.
        }
    }

    // Render method is inherited from GuiElement.
    // It can be overridden here if FlowGuiContainer needs to draw its own background, etc.
    // For example:
    // @Override
    // public void render(RenderingEnvironment re) {
    //     if (this.parent == null) { measureAndLayout(re); }
    //
    //     // Draw background for FlowGuiContainer
    //     // re.defaultShaderProgram...
    //
    //     for (GuiElement child : children) {
    //         child.render(re);
    //     }
    // }
}
