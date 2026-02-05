#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec2 tileOffset;
layout(location = 3) in vec3 color;

out vec2 texCoord;
out vec3 tileColor;

uniform mat4 projection;
uniform vec2 chunkPos;
uniform vec2 cameraPos;
uniform float zoom;
uniform vec2 tileScale;

void main() {
    vec2 worldPos = chunkPos + tileOffset;
    vec2 screenPos = (worldPos - cameraPos) * zoom;

    vec2 scaledVertexPos = position.xy * tileScale * zoom;
    vec2 finalPixelPos = screenPos + scaledVertexPos;

    gl_Position = projection * vec4(finalPixelPos, 0.0, 1.0);

    texCoord = uv;
    tileColor = color;
}
