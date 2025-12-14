package engine.visuals.lwjgl.render;

import static java.util.Arrays.asList;
import static engine.nengen.EngineConfiguration.DEBUG;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static engine.visuals.lwjgl.render.shader.ShaderUniformData.fromType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import engine.visuals.lwjgl.ResourcePack;
import engine.visuals.lwjgl.render.shader.ShaderUniformData;

public abstract class Shader extends GLRegularObject {

	private final ShaderType shaderType;

	protected transient String source = null;

	protected Shader(ShaderType shaderType) {
		this.shaderType = shaderType;
	}

	protected List<ShaderUniformData<?>> parameters = new ArrayList<>();

	@Override
	public void genID() {
		this.id = glCreateShader(shaderType.type);
		initialize();
	}

	public void parseParameters() {
		if (source == null)
			throw new RuntimeException("Shader source cannot be null when parsing parameters");
		DEBUG("Parsing parameters for shader");
		String uniformRegex = "\\s*uniform\\s+(\\w+)\\s+([^;]+);";
		Matcher uniformMatcher = Pattern.compile(uniformRegex).matcher(source);
		Pattern declarationPattern = Pattern.compile("(\\w+)(\\[[^\\]]*\\])?");

		while (uniformMatcher.find()) {
			String type = uniformMatcher.group(1);
			String declarations = uniformMatcher.group(2);
			String[] declarationParts = declarations.split(",");
			List<String> parsedNames = new ArrayList<>();

			for (String declaration : declarationParts) {
				String trimmedDecl = declaration.trim();
				Matcher declarationMatcher = declarationPattern.matcher(trimmedDecl);

				if (declarationMatcher.find()) {
					String name = declarationMatcher.group(1);
					boolean isArray = declarationMatcher.group(2) != null;
					String finalType = isArray ? type + "[]" : type;

					parameters.add(fromType(finalType, name));
					parsedNames.add(name);
				}
			}

			if (!parsedNames.isEmpty()) {
				DEBUG("Found " + type + " parameters: " + String.join(", ", parsedNames));
			}
		}
	}

	protected void doLoad() {
		this.id = glCreateShader(shaderType.type);
		parseParameters();
		glShaderSource(id, source);
		glCompileShader(id);
		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(id, 500));
			throw new RuntimeException("Could not compile shader source:\n" + source);
		}
		if (!DEBUG) {
			// If we're not debugging, we don't need the source anymore.
			// This frees up some memory.
			source = null;
		}
		initialize();
	}

	public void delete() {
		glDeleteShader(id);
	}

	public int id() {
		return id;
	}

	@Override
	public void putInto(String name, ResourcePack resourcePack) {
	}

	public List<ShaderUniformData<?>> uniforms() {
		return parameters;
	}

}
