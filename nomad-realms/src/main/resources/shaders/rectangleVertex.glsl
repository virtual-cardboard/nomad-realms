#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;

out vec2 texCoord;

uniform mat4 transform;
uniform vec4 crop = vec4(0, 0, 1, 1);

void main() {
    gl_Position = transform * vec4(position, 1.0);
    texCoord = crop.xy + uv * crop.zw;
}
