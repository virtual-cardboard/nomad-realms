#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D textureSampler;
uniform float threshold;

void main() {
    vec3 color = texture(textureSampler, texCoords).rgb;
    float brightness = max(color.r, max(color.g, color.b));
    if (brightness > threshold) {
        fragColor = vec4(color, 1.0);
    } else {
        fragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}
