#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 size;          // original rectangle dimensions (w, h) in pixels
uniform vec2 paddedSize;    // padded dimensions (w + 2*brushRadius, h + 2*brushRadius) in pixels
uniform float radius;       // corner radius of original rectangle in pixels
uniform float brushRadius;  // brush radius R (diameter / 2) in pixels
uniform float hardness;     // brush hardness (e.g. 0.75)
uniform vec4 fillColor;     // fill color (rgba)
uniform vec4 borderColor;   // outline/border/brush color (rgba)

// SDF function for a rounded rectangle
float roundedRectSDF(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;
}

void main() {
    // Convert texCoord [0, 1] across the padded quad to pixel space centered at (0, 0)
    vec2 p = (texCoord - vec2(0.5, 0.5)) * paddedSize;

    // Half-size for SDF
    vec2 b = size * 0.5;

    // Compute distance to edge
    float d = roundedRectSDF(p, b, radius);

    float distToCenter = abs(d);
    float brushAlpha = 1.0 - smoothstep(brushRadius * hardness, brushRadius, distToCenter);

    vec4 color;
    if (d <= 0.0) {
        // Inside shape: blend fill and border color based on brush opacity
        color = mix(fillColor, borderColor, brushAlpha);
    } else {
        // Outside shape: border color fading out to transparent
        color = borderColor;
        color.a *= brushAlpha;
    }

    if (color.a <= 0.0) {
        discard;
    }

    fragColor = color;
}
