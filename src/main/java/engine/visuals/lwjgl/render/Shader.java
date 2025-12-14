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

	private void parseParameters() {
		if (source == null)
			throw new RuntimeException("Shader source cannot be null when parsing parameters");
		DEBUG("Parsing parameters for shader");
		String regex = "\\s*uniform\\s+(\\w+)\\s+([^;]+);";
		Matcher matcher = Pattern.compile(regex).matcher(source);
		while (matcher.find()) {
			String type = matcher.group(1);
			String declarations = matcher.group(2);
			String[] declarationParts = declarations.split(",");
			List<String> parsedNames = new ArrayList<>();
			for (String declaration : declarationParts) {
				String trimmedDecl = declaration.trim();
				String name = trimmedDecl.split("[\\[=]")[0].trim();
				if (name.isEmpty()) {
					continue;
				}
				parsedNames.add(name);
				boolean isArray = trimmedDecl.contains("[");
				String finalType = isArray ? type + "[]" : type;
				parameters.add(fromType(finalType, name));
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
