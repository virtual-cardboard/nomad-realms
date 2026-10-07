#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D textureSampler;
uniform int horizontal;
uniform float radius;

// Bilinear texture sampling offsets and weights for 9-tap Gaussian blur
float offset[3] = float[](0.0, 1.3846153846, 3.2307692308);
float weight[3] = float[](0.2270270270, 0.3162162162, 0.0702702703);

void main() {
    float r = radius > 0.0 ? radius : 1.0;
    vec2 tex_offset = (1.0 / textureSize(textureSampler, 0)) * r;
    vec3 result = texture(textureSampler, texCoords).rgb * weight[0];
    if (horizontal == 1) {
        for (int i = 1; i < 3; ++i) {
            result += texture(textureSampler, texCoords + vec2(tex_offset.x * offset[i], 0.0)).rgb * weight[i];
            result += texture(textureSampler, texCoords - vec2(tex_offset.x * offset[i], 0.0)).rgb * weight[i];
        }
    } else {
        for (int i = 1; i < 3; ++i) {
            result += texture(textureSampler, texCoords + vec2(0.0, tex_offset.y * offset[i])).rgb * weight[i];
            result += texture(textureSampler, texCoords - vec2(0.0, tex_offset.y * offset[i])).rgb * weight[i];
        }
    }
    fragColor = vec4(result, 1.0);
}
