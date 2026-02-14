#version 330 core
out vec4 fragColor;

in vec2 texCoord;

uniform sampler2D textureSampler;
uniform vec4 color;

void main() {
    fragColor = texture(textureSampler, texCoord) * color;
}
