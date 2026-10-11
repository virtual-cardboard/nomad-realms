#version 330 core
out vec4 fragColor;

in vec2 texCoords;

uniform sampler2D sceneTexture;
uniform sampler2D bloomTexture;
uniform float bloomIntensity;
uniform vec4 backgroundColor;

void main() {
    vec4 scene = texture(sceneTexture, texCoords);
    vec3 bloom = texture(bloomTexture, texCoords).rgb;
    vec3 baseColor = mix(backgroundColor.rgb, scene.rgb, scene.a);
    fragColor = vec4(baseColor + bloom * bloomIntensity, 1.0);
}
