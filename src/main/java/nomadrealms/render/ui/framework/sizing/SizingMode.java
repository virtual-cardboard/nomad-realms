package nomadrealms.render.ui.framework.sizing;

public enum SizingMode {
    ABSOLUTE,       // Size is a fixed pixel value.
    PERCENTAGE,     // Size is a percentage of the parent's corresponding dimension.
    WRAP_CONTENT,   // Size is determined by the content of the element (e.g., sum of children sizes for a container, or intrinsic size for content).
    STRETCH         // Size will take up all available space in the parent (relevant for containers like a single child filling a parent).
}
