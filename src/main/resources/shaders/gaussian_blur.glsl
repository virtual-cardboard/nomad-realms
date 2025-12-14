#version 330 core
#define MAX_KERNEL_RADIUS 15

out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D textureSampler;
uniform int horizontal;
uniform int radius;
uniform float weights[MAX_KERNEL_RADIUS + 1];

void main() {
    vec2 tex_offset = 1.0 / textureSize(textureSampler, 0); // gets size of single texel
    vec3 result = texture(textureSampler, texCoords).rgb * weights[0]; // current fragment's contribution
    if (horizontal == 1) {
        for (int i = 1; i <= radius; ++i) {
            result += texture(textureSampler, texCoords + vec2(tex_offset.x * i, 0.0)).rgb * weights[i];
            result += texture(textureSampler, texCoords - vec2(tex_offset.x * i, 0.0)).rgb * weights[i];
        }
    } else {
        for (int i = 1; i <= radius; ++i) {
            result += texture(textureSampler, texCoords + vec2(0.0, tex_offset.y * i)).rgb * weights[i];
            result += texture(textureSampler, texCoords - vec2(0.0, tex_offset.y * i)).rgb * weights[i];
        }
    }
    fragColor = vec4(result, 1.0);
}
