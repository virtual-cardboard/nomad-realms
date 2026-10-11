#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D textureSampler;
uniform int horizontal;
uniform int sampleRadius;
uniform float weights[33];

void main() {
    vec2 tex_offset = 1.0 / textureSize(textureSampler, 0);
    vec3 result = texture(textureSampler, texCoords).rgb * weights[0];

    if (horizontal == 1) {
        for (int i = 1; i <= sampleRadius; ++i) {
            vec2 offset = vec2(tex_offset.x * float(i), 0.0);
            result += texture(textureSampler, texCoords + offset).rgb * weights[i];
            result += texture(textureSampler, texCoords - offset).rgb * weights[i];
        }
    } else {
        for (int i = 1; i <= sampleRadius; ++i) {
            vec2 offset = vec2(0.0, tex_offset.y * float(i));
            result += texture(textureSampler, texCoords + offset).rgb * weights[i];
            result += texture(textureSampler, texCoords - offset).rgb * weights[i];
        }
    }

    fragColor = vec4(result, 1.0);
}
