#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoords;

out vec2 texCoords;

uniform mat4 transform;

void main() {
    texCoords = aTexCoords;
    gl_Position = transform * vec4(aPos, 0.0, 1.0);
}
