#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 size;          // rectangle dimensions (w, h) in pixels
uniform float radius;       // corner radius in pixels
uniform float borderWidth;  // border width in pixels (inside)
uniform vec4 fillColor;     // fill color (rgba)
uniform vec4 borderColor;   // border color (rgba)

// SDF function for a rounded rectangle
float roundedRectSDF(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;
}

void main() {
    // texCoord is from [0, 1] across the entire rectangle.
    // Convert to pixel space centered at (0, 0).
    vec2 p = (texCoord - vec2(0.5, 0.5)) * size;

    // Half-size for SDF
    vec2 b = size * 0.5;

    // Compute distance to edge
    float d = roundedRectSDF(p, b, radius);

    // Antialiasing using fwidth for smoothness
    float smoothing = 1.0; // Assume 1 pixel smoothing for UI

    // Outside mask: 0 inside, 1 outside (smoothed)
    float outerMask = smoothstep(0.0, smoothing, d);

    // Border mask: 0 deep inside, 1 at the border edge
    float borderEdge = -borderWidth;
    float borderMask = smoothstep(borderEdge, borderEdge + smoothing, d);

    // Combine fill and border
    vec4 color = mix(fillColor, borderColor, borderMask);

    // Apply outer alpha (for rounded corners and edges)
    color.a *= (1.0 - outerMask);

    if (color.a <= 0.0) {
        discard;
    }

    fragColor = color;
}
