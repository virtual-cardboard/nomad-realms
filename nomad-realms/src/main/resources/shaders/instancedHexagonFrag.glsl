#version 330 core

in vec2 texCoord;
in vec4 color;
out vec4 fragColor;

uniform vec2 size;          // bounding box dimensions (w, h) in pixels
uniform float radius;       // hexagon radius in pixels

float sdHexagon(in vec2 p, in float r)
{
    const vec3 k = vec3(-0.866025404, 0.5, 0.577350269);
    p = abs(p);
    p -= 2.0 * min(dot(k.xy, p), 0.0) * k.xy;
    p -= vec2(clamp(p.x, -k.z * r, k.z * r), r);
    return length(p) * sign(p.y);
}

void main() {
    vec2 p = (texCoord - vec2(0.5, 0.5)) * size;

    // Flat-topped hexagon
    float d = sdHexagon(p.yx, radius);

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
