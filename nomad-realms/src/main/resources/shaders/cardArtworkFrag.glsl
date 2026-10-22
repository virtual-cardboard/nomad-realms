#version 330 core

// Holofoil card artwork fragment shader.
// A GLSL translation of the "Holofoil Cards" Shader Graph from
// https://danielilett.com/2025-10-13-tut11-01-holofoil-cards/
//
// Section mapping (Shader Graph node -> GLSL):
//   Simple Noise + Time * Holo Anim Speed  -> valueNoise() + animClock
//   Hue node (Normalized range)            -> rainbow()
//   View Vector dot Holo Direction         -> streak calculation
//   Holo Mask texture                      -> patternMask() (procedural stand-in)
//   Heightmap -> Normal From Height        -> normalFromHeight() (procedural stand-in)
//   Base Color -> Lerp -> Base Color       -> mix() at the bottom

in vec2 texCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec4 color = vec4(1, 1, 1, 1);

// Time in seconds, used to animate the rainbow hue.
uniform float time = 0.0;

// How tightly packed the holo noise blotches are (Scale input of Simple Noise).
uniform float holoNoiseScale = 8.0;

// How quickly the shader scrolls through the rainbow hue (Holo Anim Speed).
uniform float holoAnimSpeed = 0.15;

// Global multiplier for the holographic color (HDR color property).
uniform vec3 holoColor = vec3(1, 1, 1);

// Direction of the rainbow streaks, in tangent space (flush with the card).
uniform vec2 holoDirection = vec2(1, 0);

// Neutral resting position of the streaks (Holo Offset, slider 0-1).
uniform float holoOffset = 0.0;

// How stretched the streaks are across the surface (Holo Density, slider).
uniform float holoDensity = 8.0;

// Precomputed on the CPU: dot(holoDirection, tangent(cameraPos - cardPivot))
// multiplied by Holo Rotation Scroll Speed. The vector is identical across the
// whole surface, so it can be a single scalar uniform.
uniform float holoRotationOffset = 0.0;

// Camera position and screen dimensions in normalized screen coordinates, used
// to reconstruct the per-pixel View Vector (the vector between the camera and
// the pixel being rendered).
uniform vec2 cameraPosScreen = vec2(0.5, 0.5);
uniform vec2 screenDim = vec2(1, 1);

// Master toggle for the holographic rainbow (0 = off, 1 = full strength).
uniform float holoStrength = 0.0;

// Strength of the procedural holofoil pattern mask (0 = uniform, 1 = cosmos).
uniform float patternStrength = 0.0;

// Strength of the heightmap-based lighting (the etched-lines effect).
uniform float lightStrength = 0.0;

// Light direction in tangent space, for the heightmap lighting.
uniform vec3 lightDirection = vec3(0.3, 0.5, 1);

/**
 * Hash function for the value noise below.
 */
float hash(vec2 p) {
	return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

/**
 * 2D value noise, standing in for the Simple Noise node.
 */
float valueNoise(vec2 p) {
	vec2 i = floor(p);
	vec2 f = fract(p);
	vec2 u = f * f * (3.0 - 2.0 * f);
	float a = hash(i);
	float b = hash(i + vec2(1, 0));
	float c = hash(i + vec2(0, 1));
	float d = hash(i + vec2(1, 1));
	return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

/**
 * Fractional Brownian motion over the value noise, used for the holofoil
 * pattern mask and the heightmap.
 */
float fbm(vec2 p) {
	float sum = 0.0;
	float amplitude = 0.5;
	for (int i = 0; i < 4; i++) {
		sum += valueNoise(p) * amplitude;
		p = p * 2.0 + vec2(13.7, 7.3);
		amplitude *= 0.5;
	}
	return sum;
}

/**
 * Cycles a value from 0 to 1 through the full rainbow hue range, like the Hue
 * node set to Normalized range: red -> orange -> yellow -> green -> blue -> ...
 */
vec3 rainbow(float h) {
	float x = fract(h) * 6.0;
	return vec3(
			clamp(abs(x - 3.0) - 1.0, 0.0, 1.0),
			clamp(2.0 - abs(x - 2.0), 0.0, 1.0),
			clamp(2.0 - abs(x - 4.0), 0.0, 1.0)
	);
}

/**
 * Procedural stand-in for the Holo Mask texture: encodes the holographic
 * parts of the card as values near 1 and the non-holo parts as values near 0.
 * The specks are star-like (the Cosmos holofoil pattern) with nebula-like
 * blotches underneath.
 */
float patternMask(vec2 uv) {
	float specks = smoothstep(0.75, 0.95, valueNoise(uv * 12.0));
	float clouds = smoothstep(0.45, 0.75, fbm(uv * 3.0));
	return clamp(specks + clouds * 0.8, 0.0, 1.0);
}

/**
 * Procedural stand-in for the Heightmap texture, encoding raised parts of the
 * surface as high values.
 */
float heightAt(vec2 uv) {
	return fbm(uv * 6.0);
}

/**
 * Procedural stand-in for the Normal From Height node: converts the height
 * field into a tangent-space normal using finite differences.
 */
vec3 normalFromHeight(vec2 uv) {
	float epsilon = 0.01;
	float hL = heightAt(uv - vec2(epsilon, 0));
	float hR = heightAt(uv + vec2(epsilon, 0));
	float hD = heightAt(uv - vec2(0, epsilon));
	float hU = heightAt(uv + vec2(0, epsilon));
	vec2 slope = vec2(hR - hL, hU - hD) / (2.0 * epsilon);
	// Normal From Height is quite sensitive, hence the small scale factor
	// (the article's Heightmap Strength slider is multiplied by 0.002).
	return normalize(vec3(slope * lightStrength * 0.02, 1.0));
}

void main() {
	vec4 texColor = texture(textureSampler, texCoord);
	if (texColor.a < 0.1) {
		discard;
	}

	// --- Holo Noise Pattern (Simple Noise node + Time * Holo Anim Speed) ---
	// The noise uses the card's object-space position (its UV), so the rainbow
	// pattern unfolds over the card surface rather than in world space.
	float animClock = time * holoAnimSpeed;
	float holoNoise = valueNoise(texCoord * holoNoiseScale);
	float hueBase = holoNoise + animClock;

	// --- Holographic Streaks (View Vector dot Holo Direction) ---
	// Reconstruct the per-pixel view vector in normalized screen space, then
	// flatten it into tangent space with a z-component of 0 since the streak
	// direction is flush with the card surface.
	// gl_FragCoord has a bottom-left origin, but the engine's screen
	// coordinates have a top-left origin, hence the y flip.
	vec2 pixelScreen = gl_FragCoord.xy / screenDim;
	pixelScreen.y = 1.0 - pixelScreen.y;
	vec2 viewVectorScreen = pixelScreen - cameraPosScreen;
	vec3 viewVectorTangent = normalize(vec3(viewVectorScreen, 0));
	vec2 holoDir = normalize(holoDirection);
	float streak = dot(holoDir, viewVectorTangent.xy) + holoOffset;
	streak *= holoDensity;
	streak += holoRotationOffset;
	streak = clamp(sin(streak), 0.0, 1.0);

	// --- Holo Mask (procedural) ---
	// Multiply the streak pattern by the mask so rainbows only appear on the
	// holographic parts of the card.
	float mask = mix(1.0, patternMask(texCoord), patternStrength);
	streak *= mask * holoStrength;

	// --- Rainbow generation (Hue node, Normalized range) ---
	// The streak value (0-1) is added to the hue, so each streak cycles the
	// full hue range from red to red as you cross it.
	vec3 rainbowColor = rainbow(hueBase + streak) * holoColor;

	// --- Base Color ---
	vec3 baseColor = texColor.rgb * color.rgb;

	// --- Compose (Lerp node: A = Base Color, B = rainbow, T = holo mask) ---
	vec3 result = mix(baseColor, rainbowColor, clamp(streak, 0.0, 1.0));

	// --- Heightmap lighting (Normal From Height -> Normal output) ---
	// Emulate etched lines by shading the surface with the procedural normal.
	if (lightStrength > 0.0) {
		vec3 n = normalFromHeight(texCoord);
		float diffuse = max(dot(n, normalize(lightDirection)), 0.0);
		float shade = mix(1.0, 0.4 + 0.6 * diffuse, lightStrength);
		result *= shade;
	}

	fragColor = vec4(result, texColor.a * color.a);
}
