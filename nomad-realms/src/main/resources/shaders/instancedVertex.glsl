#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in mat4 instanceTransform;
layout(location = 6) in vec4 instanceColor;
layout(location = 7) in vec4 instanceCrop;

out vec4 color;

void main() {
    gl_Position = instanceTransform * vec4(position, 1.0);
    color = instanceColor;
    // We must use instanceCrop to prevent it from being optimized away, which can cause VAO mismatches.
    vec2 unused = instanceCrop.xy;
}
