#version 330 core

out vec4 fragColour;

uniform vec4 fill;

void main() {
	fragColour = fill;
}