#version 330 core

in vec2 texCoord;
in vec4 color;

uniform sampler2D textureSampler;

out vec4 fragColor;

void main() {
    fragColor = texture(textureSampler, texCoord) * color;
    if (fragColor.a < 0.1) {
        discard;
    }
}
