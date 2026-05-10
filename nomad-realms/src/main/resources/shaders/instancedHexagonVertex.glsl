#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uv;
layout(location = 2) in mat4 instanceTransform;
layout(location = 6) in vec4 instanceColor;

out vec2 texCoord;
out vec4 color;
out vec2 size;

void main() {
    gl_Position = instanceTransform * vec4(position, 1.0);
    texCoord = uv;
    color = instanceColor;

    // Calculate size in pixels from the transformation matrix
    // We assume the transform is mostly scale and translation
    float w = length(vec3(instanceTransform[0][0], instanceTransform[0][1], instanceTransform[0][2]));
    float h = length(vec3(instanceTransform[1][0], instanceTransform[1][1], instanceTransform[1][2]));
    // instanceTransform maps from unit quad [0, 1] to screen coordinates [-1, 1]
    // We need pixel dimensions for SDF.
    // Screen coords range is 2. So we multiply by 0.5 to get normalized screen size,
    // then we'll need to multiply by screen dimensions in fragment shader or here.
    // Alternatively, just pass it through and use screen size in frag.
    size = vec2(w, h);
}
