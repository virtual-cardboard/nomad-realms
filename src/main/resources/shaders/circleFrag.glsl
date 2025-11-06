#version 330 core
in vec2 texCoord;

out vec4 fragColor;

uniform vec4 color;

void main() {
    vec2 pos = texCoord;
    vec2 dist = pos - vec2(0.5, 0.5);
    float len = length(dist);
    if (len > 0.5) {
        discard;
    }
    fragColor = color;
}