#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 size;          // bounding box dimensions (w, h) in pixels
uniform float radius;       // hexagon radius in pixels
uniform float borderWidth;  // border width in pixels (inside)
uniform vec4 fillColor;     // fill color (rgba)
uniform vec4 borderColor;   // border color (rgba)

float sdHexagon(in vec2 p, in float r)
{
    const vec3 k = vec3(-0.866025404, 0.5, 0.577350269);
    p = abs(p);
    p -= 2.0 * min(dot(k.xy, p), 0.0) * k.xy;
    p -= vec2(clamp(p.x, -k.z * r, k.z * r), r);
    return length(p) * sign(p.y);
}

void main() {
    // texCoord is from [0, 1] across the entire rectangle.
    // Convert to pixel space centered at (0, 0).
    vec2 p = (texCoord - vec2(0.5, 0.5)) * size;

    // Flat-topped hexagon
    float d = sdHexagon(p.yx, radius);

    float smoothing = fwidth(d);
    if (smoothing == 0.0) {
        smoothing = 1.0;
    }

    // Outside mask: 0 inside, 1 outside (smoothed)
    float outerMask = smoothstep(0.0, smoothing, d);

    // Border mask: 0 deep inside, 1 at the border edge
    float borderEdge = -borderWidth;
    float borderMask = smoothstep(borderEdge, borderEdge + smoothing, d);

    // Combine fill and border
    vec4 color = mix(fillColor, borderColor, borderMask);

    // Apply outer alpha
    color.a *= (1.0 - outerMask);

    if (color.a <= 0.0) {
        discard;
    }

    fragColor = color;
}
