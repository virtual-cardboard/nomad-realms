#version 330 core
out vec4 fragColor;

in vec2 texCoord;

uniform sampler2D textureSampler;
uniform vec4 diffuse = vec4(1, 1, 1, 0.7f);

void main()
{
    fragColor = texture(textureSampler, texCoord);
    fragColor = (1 - fragColor.a) * fragColor + fragColor.a * diffuse;
    if (fragColor.a == 0) {
    	discard;
    }
}