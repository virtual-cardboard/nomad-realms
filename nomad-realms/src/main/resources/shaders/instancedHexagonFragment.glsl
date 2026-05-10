#version 330 core

in vec2 texCoord;
in vec4 color;
in vec2 size;
out vec4 fragColor;

uniform vec2 screenDim;

float sdHexagon( in vec2 p, in float r )
{
    const vec3 k = vec3(-0.866025404,0.5,0.577350269);
    p = abs(p);
    p -= 2.0*min(dot(k.xy,p),0.0)*k.xy;
    p -= vec2(clamp(p.x, -k.z*r, k.z*r), r);
    return length(p)*sign(p.y);
}

void main() {
    // size is passed in normalized screen coordinates (0-2 range for full screen)
    // we convert to pixels for SDF
    vec2 pixelSize = size * screenDim * 0.5;
    vec2 p = (texCoord - vec2(0.5, 0.5)) * pixelSize;

    float d = sdHexagon(p.yx, pixelSize.x * 0.5);

    float smoothing = 1.0;
    float outerMask = smoothstep(0.0, smoothing, d);

    vec4 outColor = color;
    outColor.a *= (1.0 - outerMask);

    if (outColor.a <= 0.0) {
        discard;
    }

    fragColor = outColor;
}
