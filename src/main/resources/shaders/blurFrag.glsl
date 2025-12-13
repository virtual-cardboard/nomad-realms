#version 330

in vec2 textureCoords;
out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
    float resolution = 800.0; // Hardcoded or passed as uniform if needed
    float blurRadius = 4.0;
    vec2 tex_offset = 1.0 / vec2(resolution, resolution);
    vec3 result = texture(textureSampler, textureCoords).rgb;

    // Simple box blur loop
    int count = 1;
    for(float x = -blurRadius; x <= blurRadius; x += 1.0) {
        for(float y = -blurRadius; y <= blurRadius; y += 1.0) {
            if (x == 0.0 && y == 0.0) continue;
            result += texture(textureSampler, textureCoords + vec2(x * tex_offset.x, y * tex_offset.y)).rgb;
            count++;
        }
    }

    fragColor = vec4(result / float(count), 1.0);
}
