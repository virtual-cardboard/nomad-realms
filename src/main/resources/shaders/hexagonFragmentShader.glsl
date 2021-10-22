#version 330 core

layout (location = 0) out vec4 fragmentColour;

uniform vec4 fill;

void main() {
	fragmentColour = fill;
}