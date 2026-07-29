#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 size;          // original bounding box dimensions (w, h) in pixels
uniform vec2 paddedSize;    // padded bounding box dimensions (w + 2*brushRadius, h + 2*brushRadius) in pixels
uniform vec2 v1;            // vertex 1 relative to original BB top-left
uniform vec2 v2;            // vertex 2 relative to original BB top-left
uniform vec2 v3;            // vertex 3 relative to original BB top-left
uniform float brushRadius;  // brush radius R (diameter / 2) in pixels
uniform float hardness;     // brush hardness (e.g. 0.75)
uniform vec4 fillColor;     // fill color (rgba)
uniform vec4 borderColor;   // outline/border/brush color (rgba)

float sdTriangle( in vec2 p, in vec2 p0, in vec2 p1, in vec2 p2 )
{
    vec2 e0 = p1-p0, e1 = p2-p1, e2 = p0-p2;
    vec2 v0 = p -p0, v1 = p -p1, v2 = p -p2;
    vec2 pq0 = v0 - e0*clamp( dot(v0,e0)/dot(e0,e0), 0.0, 1.0 );
    vec2 pq1 = v1 - e1*clamp( dot(v1,e1)/dot(e1,e1), 0.0, 1.0 );
    vec2 pq2 = v2 - e2*clamp( dot(v2,e2)/dot(e2,e2), 0.0, 1.0 );
    float s = sign( e0.x*e2.y - e0.y*e2.x );
    vec2 d = min(min(vec2(dot(pq0,pq0), s*(v0.x*e0.y-v0.y*e0.x)),
                     vec2(dot(pq1,pq1), s*(v1.x*e1.y-v1.y*e1.x))),
                     vec2(dot(pq2,pq2), s*(v2.x*e2.y-v2.y*e2.x)));
    return -sqrt(d.x)*sign(d.y);
}

void main() {
    // Map texCoord across the padded quad to pixel space relative to original BB top-left.
    // Padded quad starts at (-brushRadius, -brushRadius) relative to original BB top-left.
    vec2 p_padded = vec2(texCoord.x, 1.0 - texCoord.y) * paddedSize;
    vec2 p = p_padded - vec2(brushRadius, brushRadius);

    float d = sdTriangle(p, v1, v2, v3);

    float distToCenter = abs(d);
    float brushAlpha = 1.0 - smoothstep(brushRadius * hardness, brushRadius, distToCenter);

    vec4 color;
    if (d <= 0.0) {
        // Inside shape: blend fill and border color based on brush opacity
        color = mix(fillColor, borderColor, brushAlpha);
    } else {
        // Outside shape: border color fading out to transparent
        color = borderColor;
        color.a *= brushAlpha;
    }

    if (color.a <= 0.0) {
        discard;
    }

    fragColor = color;
}
