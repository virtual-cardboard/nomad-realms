#version 330 core
out vec4 outColor;

in vec2 texCoord;
in vec4 fragColor;

void main() {
    outColor = fragColor;
}
