#version 330 core
out vec4 fragColor;

in vec2 texCoord;

uniform sampler2D textureSampler;
uniform vec2 textureDim;
uniform int blurAmount;
uniform int horizontal;

void main() {
    int x = horizontal;
    int y = 1 - horizontal;
    vec4 sum = vec4(0);
    int count = 0;
    for (int i = -blurAmount; i <= blurAmount; i++) {
        if (texCoord.x + x * i / textureDim.x < 0 || texCoord.x + x * i / textureDim.x > 1) continue;
        if (texCoord.y + y * i / textureDim.y < 0 || texCoord.y + y * i / textureDim.y > 1) continue;
        count++;
        sum += texture(textureSampler, vec2(texCoord.x + x * i / textureDim.x, texCoord.y + y * i / textureDim.y));
    }
    fragColor = sum / count;
}