#version 330 core

in vec2 texCoord;
in vec3 tileColor;

out vec4 fragColor;

void main() {
    fragColor = vec4(tileColor, 1.0);
}
