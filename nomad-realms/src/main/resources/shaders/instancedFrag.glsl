#version 330 core

in vec4 color;
in vec2 fragUV;

out vec4 fragColor;

uniform sampler2D tex;

void main() {
    vec4 texColor = texture(tex, fragUV);
    if (texColor.a < 0.1) {
        discard;
    }
    fragColor = texColor * color;
}
