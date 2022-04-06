#version 330 core
out vec4 fragColor;

in vec2 texCoord;

uniform sampler2D textureSampler;
uniform float threshold = 0.5f;
uniform vec4 bright = vec4(0, 0, 0, 0);
uniform vec4 dark = vec4(0, 0, 0, 1);

void main()
{
    fragColor = texture(textureSampler, texCoord);
    float brightness = fragColor.r + fragColor.g + fragColor.b;
    if (brightness < threshold) {
    	fragColor = bright * fragColor.a;
    } else {
    	fragColor = dark * fragColor.a;
    }
}