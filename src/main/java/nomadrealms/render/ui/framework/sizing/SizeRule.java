package nomadrealms.render.ui.framework.sizing;

import nomadrealms.render.ui.framework.GuiElement;
import nomadrealms.render.RenderingEnvironment;

public class SizeRule {

    private SizingMode mode;
    private float value; // Used for ABSOLUTE (pixels) or PERCENTAGE (0.0 to 1.0)

    // Min/max dimensions
    private float minSize = 0;
    private float maxSize = Float.MAX_VALUE;

    public SizeRule(SizingMode mode, float value) {
        this.mode = mode;
        this.value = value;
    }

    public SizeRule(SizingMode mode) {
        this(mode, 0); // Value might not be relevant for e.g. WRAP_CONTENT initially
        if (mode == SizingMode.PERCENTAGE) {
            throw new IllegalArgumentException("Percentage mode requires a value.");
        }
    }

    public SizingMode getMode() {
        return mode;
    }

    public float getValue() {
        return value;
    }

    public float getMinSize() {
        return minSize;
    }

    public void setMinSize(float minSize) {
        this.minSize = Math.max(0, minSize);
        if (this.minSize > this.maxSize) {
            this.maxSize = this.minSize;
        }
    }

    public float getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(float maxSize) {
        this.maxSize = Math.max(0, maxSize);
        if (this.maxSize < this.minSize) {
            this.minSize = this.maxSize;
        }
    }

    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Calculates the effective size based on this rule.
     * @param parentDimension The relevant dimension (width or height) of the parent container.
     *                        Required for PERCENTAGE mode.
     * @param contentDimension The natural/intrinsic dimension of the content.
     *                         Required for WRAP_CONTENT mode (though often calculated within the element itself).
     * @param element The GuiElement this rule is for (can be used by WRAP_CONTENT).
     * @param re The RenderingEnvironment.
     * @return The calculated dimension, clamped by min/max.
     */
    public floatcalculateEffectiveSize(GuiElement element, float parentDimension, float contentDimension, RenderingEnvironment re) {
        float calculatedSize;
        switch (mode) {
            case ABSOLUTE:
                calculatedSize = value;
                break;
            case PERCENTAGE:
                if (element.getParent() == null) {
                     // No parent, percentage is ambiguous. Default to a small size or error.
                    System.err.println("Warning: PERCENTAGE size used on GuiElement with no parent. Defaulting to 0.");
                    calculatedSize = 0;
                } else {
                    calculatedSize = parentDimension * value;
                }
                break;
            case WRAP_CONTENT:
                // For WRAP_CONTENT, the 'contentDimension' is the key.
                // This value is typically calculated by the element itself by querying its children or intrinsic properties.
                calculatedSize = contentDimension;
                break;
            case STRETCH:
                // Stretch will try to take all available parent space.
                // This often means the parent container will determine the size directly.
                // If used naively, could be parentDimension, but usually containers handle STRETCH children specifically.
                // For now, let's treat it as "as large as parent allows".
                calculatedSize = parentDimension;
                break;
            default:
                throw new IllegalStateException("Unknown SizingMode: " + mode);
        }
        return Math.max(minSize, Math.min(maxSize, calculatedSize));
    }

    // Static factory methods for convenience
    public static SizeRule absolute(float pixels) {
        return new SizeRule(SizingMode.ABSOLUTE, pixels);
    }

    public static SizeRule percentage(float percentOfParent) { // e.g., 0.5f for 50%
        if (percentOfParent < 0 || percentOfParent > 1.0f) {
            // Allow > 1.0f for things like scrolling content that's larger than parent
            // Consider if this should be clamped or warned. For now, allow.
        }
        return new SizeRule(SizingMode.PERCENTAGE, percentOfParent);
    }

    public static SizeRule wrapContent() {
        return new SizeRule(SizingMode.WRAP_CONTENT);
    }

    public static SizeRule stretch() {
        return new SizeRule(SizingMode.STRETCH);
    }
}
