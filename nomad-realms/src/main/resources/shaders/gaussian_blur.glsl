#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D textureSampler;
uniform int horizontal;
uniform float radius;

void main() {
    float r = max(radius, 1.0);
    float sigma = max(r / 2.0, 0.5);
    float twoSigmaSq = 2.0 * sigma * sigma;

    int sampleRadius = clamp(int(ceil(sigma * 3.0)), 1, 32);

    vec2 tex_offset = 1.0 / textureSize(textureSampler, 0);
    vec3 result = vec3(0.0);
    float totalWeight = 0.0;

    if (horizontal == 1) {
        for (int i = -sampleRadius; i <= sampleRadius; ++i) {
            float weight = exp(-float(i * i) / twoSigmaSq);
            result += texture(textureSampler, texCoords + vec2(tex_offset.x * float(i), 0.0)).rgb * weight;
            totalWeight += weight;
        }
    } else {
        for (int i = -sampleRadius; i <= sampleRadius; ++i) {
            float weight = exp(-float(i * i) / twoSigmaSq);
            result += texture(textureSampler, texCoords + vec2(0.0, tex_offset.y * float(i))).rgb * weight;
            totalWeight += weight;
        }
    }

    fragColor = vec4(result / totalWeight, 1.0);
}
