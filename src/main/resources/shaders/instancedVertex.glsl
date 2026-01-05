#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec4 instanceColor;
layout(location = 3) in mat4 instanceTransform;

out vec2 texCoord;
out vec4 fragColor;

void main() {
    gl_Position = instanceTransform * vec4(position, 1.0);
    texCoord = uv;
    fragColor = instanceColor;
}
