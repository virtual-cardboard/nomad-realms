package nomadrealms.render.ui.framework;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.framework.sizing.SizeRule;
// import nomadrealms.render.ui.content.ScreenContainerContent; // No longer directly used for ConstraintBox init this way
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.posdim.AbsoluteConstraint;

public class RootGuiElement extends GuiElement {

    private RenderingEnvironment re; // Keep a reference if needed for screen size updates

    public RootGuiElement(RenderingEnvironment re) {
        super(null); // Root has no parent
        this.re = re;

        // The RootGuiElement's size is fixed to the screen dimensions.
        // Its ConstraintBox is directly set up during construction or first layout.
        // Its SizeRules should reflect that it's absolute and matches screen.
        if (re != null && re.glContext != null && re.glContext.screen != null) {
            ConstraintBox screenConstraintBox = re.glContext.screen;
            this.widthRule = SizeRule.absolute(screenConstraintBox.w().get());
            this.heightRule = SizeRule.absolute(screenConstraintBox.h().get());
            // Set its constraintBox directly as its position and size are fixed by the screen.
            this.constraintBox.x(screenConstraintBox.x());
            this.constraintBox.y(screenConstraintBox.y());
            this.constraintBox.w(screenConstraintBox.w());
            this.constraintBox.h(screenConstraintBox.h());
        } else {
            System.err.println("Warning: RootGuiElement created with null RenderingEnvironment or screen. Defaulting size to 0.");
            this.widthRule = SizeRule.absolute(0);
            this.heightRule = SizeRule.absolute(0);
        }
        this.measureDirty = true;
        this.layoutDirty = true;
    }

    @Override
    protected float calculateMeasuredContentWidth(RenderingEnvironment re) {
        // The "content" width of the root is effectively the screen width,
        // as its size is dictated by the screen, not by its children in a "wrap" sense.
        return this.constraintBox.w().get();
    }

    @Override
    protected float calculateMeasuredContentHeight(RenderingEnvironment re) {
        // Similarly, content height is screen height.
        return this.constraintBox.h().get();
    }

    @Override
    protected void arrangeChildren(RenderingEnvironment re) {
        // The RootGuiElement's ConstraintBox IS the screen.
        // Children are arranged within these screen bounds.
        float myWidth = this.constraintBox.w().get();
        float myHeight = this.constraintBox.h().get();

        for (GuiElement child : children) {
            // Determine child's final width
            float childFinalWidth = child.getWidthRule().calculateEffectiveSize(
                child, myWidth, child.measuredContentWidth, re
            );
            // Determine child's final height
            float childFinalHeight = child.getHeightRule().calculateEffectiveSize(
                child, myHeight, child.measuredContentHeight, re
            );

            // For Root, children are typically positioned absolutely or via some alignment logic
            // that's not yet implemented (e.g. centerInParent).
            // For now, let's assume children need explicit positioning or default to (0,0) relative to root.
            // If a child has its own x/y rules (e.g. PercentPositionRule), they'd be applied here.
            // This part needs more detailed logic for positioning (anchors, margins, etc.)

            // Example: Default to (0,0) if no other positioning is set on child
            // More advanced: child.getXRule().calculateEffectivePosition(...)
            float childX = this.constraintBox.x().get(); // Default: child's X relative to parent's X
            float childY = this.constraintBox.y().get(); // Default: child's Y relative to parent's Y

            // A simple positioning for test: if child has absolute constraints for x/y, use them.
            // This is a placeholder for a more robust positioning system.
            if (child.getConstraintBox().x() instanceof AbsoluteConstraint) {
                 // If child's x was already set absolutely (e.g. by user), respect it.
                 // However, the SizeRule system is more about size. Positioning is another layer.
                 // Let's assume for now the child's constraintBox X/Y are pre-set if not (0,0) by default.
                 // This means the GameInterface test code that sets absolute X/Y on the FlowContainer will work.
            }


            child.getConstraintBox().w(new AbsoluteConstraint(childFinalWidth));
            child.getConstraintBox().h(new AbsoluteConstraint(childFinalHeight));
            // child.getConstraintBox().x(...) and child.getConstraintBox().y(...)
            // need to be set here based on alignment, explicit child position rules, etc.
            // For now, if they were set absolutely (like in GameInterface test), they remain.
            // If they are default (0,0), they remain (0,0) relative to root.

            // If the child is a container, it now has its final size, so it can arrange its own children.
            // The recursive call in measureAndLayout handles this.
        }
    }

    // Render method inherited from GuiElement will call measureAndLayout()
    // as this is the root (parent == null).
}
