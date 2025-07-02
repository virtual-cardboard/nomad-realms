package nomadrealms.render.ui.framework;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.ui.framework.sizing.SizeRule;
import visuals.constraint.box.ConstraintBox;
import visuals.constraint.posdim.AbsoluteConstraint; // For setting final values

public abstract class GuiElement implements UI {

    protected GuiElement parent;
    protected List<GuiElement> children = new ArrayList<>();
    protected ConstraintBox constraintBox; // This will store the FINAL calculated position and size

    // Sizing rules defining desired width and height behavior
    protected SizeRule widthRule = SizeRule.wrapContent(); // Default: size to content
    protected SizeRule heightRule = SizeRule.wrapContent();

    // Calculated content dimensions - these are results of a "measure" pass
    protected float measuredContentWidth = 0;
    protected float measuredContentHeight = 0;

    // Indicates if the layout is dirty and needs recalculation
    protected boolean layoutDirty = true;
    protected boolean measureDirty = true; // Indicates if content dimensions need recalculation

    public GuiElement(GuiElement parent) {
        this.parent = parent;
        this.constraintBox = new ConstraintBox(); // Initialize with default, non-null ConstraintBox
    }

    public GuiElement getParent() {
        return parent;
    }

    public void setParent(GuiElement parent) {
        this.parent = parent;
        markMeasureDirty();
        markLayoutDirty();
    }

    public List<GuiElement> getChildren() {
        return children;
    }

    public void addChild(GuiElement child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
            child.setParent(this); // Automatically set this as parent
            markMeasureDirty(); // Children affect content size
            markLayoutDirty();
        }
    }

    public void removeChild(GuiElement child) {
        if (child != null && children.remove(child)) {
            child.setParent(null); // Remove parent reference
            markMeasureDirty(); // Children affect content size
            markLayoutDirty();
        }
    }

    public SizeRule getWidthRule() {
        return widthRule;
    }

    public void setWidthRule(SizeRule widthRule) {
        this.widthRule = widthRule;
        markMeasureDirty();
        markLayoutDirty();
    }

    public SizeRule getHeightRule() {
        return heightRule;
    }

    public void setHeightRule(SizeRule heightRule) {
        this.heightRule = heightRule;
        markMeasureDirty();
        markLayoutDirty();
    }

    public ConstraintBox getConstraintBox() {
        if (constraintBox == null) {
            // Fallback to a default or try to compute if not set.
            // For now, let's ensure it's never null after construction.
            // This might be refined later.
            constraintBox = new ConstraintBox();
        }
        return constraintBox;
    }

    public void setConstraintBox(ConstraintBox constraintBox) {
        // This method might be used for explicitly setting the final box,
        // e.g. by a parent container after calculations.
        this.constraintBox = constraintBox;
        // If the box is set externally, it might affect children's percentage sizes.
        markLayoutDirty();
    }

    public void markMeasureDirty() {
        this.measureDirty = true;
        if (parent != null) {
            // If this element's preferred size changes, its parent might need to remeasure/re-layout
            parent.markMeasureDirty();
        }
        markLayoutDirty(); // If measure is dirty, layout is also dirty
    }

    /**
     * Marks this element and its ancestors as needing a layout recalculation.
     */
    public void markLayoutDirty() {
        this.layoutDirty = true;
        if (parent != null) {
            parent.markLayoutDirty();
        }
    }

    protected void clearDirtyFlags() {
        this.measureDirty = false;
        this.layoutDirty = false;
    }

    /**
     * Calculates and caches the natural content width of this element.
     * For simple elements, this is their intrinsic width (e.g., image width).
     * For containers, this involves measuring their children.
     * This is the "measure" part of the layout pass.
     * @param re RenderingEnvironment, can be used by children if needed for measuring.
     * @return The content width.
     */
    protected abstract float calculateMeasuredContentWidth(RenderingEnvironment re);

    /**
     * Calculates and caches the natural content height of this element.
     * @param re RenderingEnvironment.
     * @return The content height.
     */
    protected abstract float calculateMeasuredContentHeight(RenderingEnvironment re);

    /**
     * This is the "layout" or "arrange" part.
     * Positions and sizes its children within its own final bounds (this.constraintBox).
     * This method assumes its own size has already been determined and set in this.constraintBox.
     */
    protected abstract void arrangeChildren(RenderingEnvironment re);

    /**
     * The core layout process. It's a two-pass system: measure then arrange.
     * 1. Measure pass: Determine preferred sizes (bottom-up for WRAP_CONTENT).
     * 2. Layout/Arrange pass: Assign final positions and sizes (top-down).
     */
    public void measureAndLayout(RenderingEnvironment re) {
        // Phase 1: Measure (Calculate preferred sizes)
        // If this element's preferred size depends on children (WRAP_CONTENT),
        // those children must be measured first.
        if (measureDirty || layoutDirty) { // layoutDirty implies potential need for remeasure for some modes
            // Measure children first if this element wraps them
            if (widthRule.getMode() == SizeRule.SizingMode.WRAP_CONTENT || heightRule.getMode() == SizeRule.SizingMode.WRAP_CONTENT) {
                for (GuiElement child : children) {
                    child.measureAndLayout(re); // Recursive call
                }
            }
            // Now calculate this element's own content dimensions
            this.measuredContentWidth = calculateMeasuredContentWidth(re);
            this.measuredContentHeight = calculateMeasuredContentHeight(re);
        }

        // Phase 2: Determine final size and position for THIS element
        // This is typically done by the PARENT container during its arrangeChildren pass,
        // or for the root, it's fixed.
        // For now, we'll assume the parent has set our constraintBox if we are not root.
        // If we are root, our constraintBox is already set (screen size).

        // After this element's ConstraintBox is finalized (either by parent or if it's root),
        // it can arrange its children.
        if (layoutDirty) { // Only arrange if necessary
            // Ensure parent has laid us out first if we are not root.
            // This is tricky. The call to measureAndLayout should propagate from root.
            // The parent's arrangeChildren is what would set our constraintBox.

            // For a simplified flow for now: assume if we reach here, our constraintBox
            // is either set by parent or we are root.
            // If this element has its final size, it can now arrange its children.
            if (constraintBox.w().get() > 0 || constraintBox.h().get() > 0 || children.isEmpty()) { // Avoid arranging if size is zero unless no children
                 arrangeChildren(re);
            }
        }

        // Recursively call for children that might still be dirty,
        // especially if they are containers that need to arrange their own children.
        // This ensures the layout pass propagates down.
        for (GuiElement child : children) {
            if (child.layoutDirty || child.measureDirty) {
                 // If a child's size is dependent on us (e.g. percentage) and we just got our final size,
                 // it needs to re-evaluate.
                child.measureAndLayout(re);
            }
        }
        clearDirtyFlags();
    }


    @Override
    public void render(RenderingEnvironment re) {
        if (this.parent == null) { // If this is the root element
            measureAndLayout(re);
        }
        // Actual rendering logic for the element itself will go here or be overridden by subclasses.
        // Example: draw background, border, etc. based on this.constraintBox

        // Render children
        for (GuiElement child : children) {
            // TODO: Add clipping if necessary: check if child is within this element's bounds
            child.render(re);
        }
    }

}
