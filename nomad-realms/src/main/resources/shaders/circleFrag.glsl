#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform vec4 color;
uniform vec2 size;          // bounding box dimensions (w, h) in pixels
uniform float radius;       // circle radius in pixels

float sdCircle(in vec2 p, in float r)
{
    return length(p) - r;
}

void main() {
    vec2 p = (texCoord - vec2(0.5, 0.5)) * size;

    float d = sdCircle(p, radius);

    float smoothing = fwidth(d);
    if (smoothing == 0.0) {
        smoothing = 1.0;
    }

    float outerMask = smoothstep(0.0, smoothing, d);

    vec4 finalColor = color;
    finalColor.a *= (1.0 - outerMask);

    if (finalColor.a <= 0.0) {
        discard;
    }

    fragColor = finalColor;
}
