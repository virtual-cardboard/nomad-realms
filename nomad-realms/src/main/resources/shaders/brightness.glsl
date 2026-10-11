#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D textureSampler;

void main() {
    vec3 color = texture(textureSampler, texCoords).rgb;
    float brightness = max(color.r, max(color.g, color.b));
    if (brightness > 0.4) {
        fragColor = vec4(color, 1.0);
    } else {
        fragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}
