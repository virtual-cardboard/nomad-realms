#version 330 core

in vec2 texCoord;
in vec4 color;

out vec4 fragColor;

uniform sampler2D tex;

void main() {
    vec4 texColor = texture(tex, texCoord) * color;
    if (texColor.a < 0.1) {
        discard;
    }
    fragColor = texColor;
}
