#version 330 core
layout (location = 0) in vec3 vertexPosition;

uniform mat4 matrix4f;

void main() {
	gl_Position = matrix4f * vec4(vertexPosition, 1.0);
}