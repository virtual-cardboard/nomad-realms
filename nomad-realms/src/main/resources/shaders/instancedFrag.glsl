#version 330 core
in vec4 color;
out vec4 fragColor;

void main() {
    fragColor = color;
    if (fragColor.a < 0.1) {
        discard;
    }
}
