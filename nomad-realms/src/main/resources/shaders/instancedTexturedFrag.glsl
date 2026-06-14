#version 330 core

in vec4 color;
in vec2 texCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
    vec4 texColor = texture(textureSampler, texCoord);
    if (texColor.a < 0.1) {
        discard;
    }
    fragColor = texColor * color;
}
