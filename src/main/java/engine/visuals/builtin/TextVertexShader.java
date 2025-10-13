package engine.visuals.builtin;

import engine.visuals.lwjgl.render.VertexShader;

public class TextVertexShader {

	private static VertexShader shader;

	private TextVertexShader() {
	}

	private static final String TEXTURED_TRANSFORMATION_VERTEX_SHADER_SOURCE = "#version 330 core\n"
			+ "layout (location = 0) in vec3 vertexPos;\n"
			+ "layout (location = 1) in vec2 textureCoord;\n"
			+ "layout (location = 2) in vec4 atlas;\n" // atlas: vec4(x, y, width, height)
			+ "layout (location = 3) in vec2 offset;\n"
			+ "\n"
			+ "out vec2 texCoord;\n"
			+ "\n"
			+ "uniform mat4 transform;\n"
			+ "uniform float fontSize;\n"
			+ "uniform vec2 textureDim;"
			+ "\n"
			+ "mat4 scale(vec3 v);\n"
			+ "mat4 translate(vec3 v);\n"
			+ "\n"
			+ "void main() {\n"
			+ "    mat4 translateMatrix = translate(vec3(offset, 0));\n"
			+ "    mat4 scaleMatrix = scale(vec3(fontSize * atlas.zw, 1));\n"
			+ "    gl_Position = transform * translateMatrix * scaleMatrix * vec4(vertexPos, 1);\n"
			+ "    \n"
			+ "    vec2 newAtlasPos = vec2(atlas.x, textureDim.y - atlas.w - atlas.y);\n" // flip y because font metadata is upside down
			+ "    texCoord = textureCoord * atlas.zw / textureDim + newAtlasPos / textureDim;\n"
			+ "}\n"
			+ "\n"
			+ "mat4 scale(vec3 v) {\n"
			+ "	   return mat4(\n"
			+ "		   vec4(v.x, 0, 0, 0),\n"
			+ "		   vec4(0, v.y, 0, 0),\n"
			+ "		   vec4(0, 0, v.z, 0),\n"
			+ "		   vec4(0, 0, 0, 1));\n"
			+ "}\n"
			+ "\n"
			+ "mat4 translate(vec3 v) {\n"
			+ "	   return mat4(\n"
			+ "		   vec4(1, 0, 0, 0),\n"
			+ "		   vec4(0, 1, 0, 0),\n"
			+ "		   vec4(0, 0, 1, 0),\n"
			+ "		   vec4(v.x, v.y, v.z, 1));\n"
			+ "}";

	public static VertexShader instance() {
		if (shader == null) {
			shader = new VertexShader()
					.source(TEXTURED_TRANSFORMATION_VERTEX_SHADER_SOURCE)
					.load();
		}
		return shader;
	}

}
