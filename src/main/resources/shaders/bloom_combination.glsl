#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D sceneTexture;
uniform sampler2D bloomTexture;

void main() {
    vec3 sceneColor = texture(sceneTexture, texCoords).rgb;
    vec3 bloomColor = texture(bloomTexture, texCoords).rgb;
    fragColor = vec4(sceneColor + bloomColor, 1.0);
}
