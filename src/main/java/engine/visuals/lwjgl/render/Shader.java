package engine.visuals.lwjgl.render;

import static engine.nengen.EngineConfiguration.DEBUG;
import static engine.visuals.lwjgl.render.shader.ShaderUniformData.fromType;
import static java.util.Arrays.asList;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

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
		String prefixRegex = "\\s*uniform";
		String typeRegex = "\\s+(\\w+)";
		String firstNameRegex = "\\s+(\\w+)(?:\\s+=\\s+[^;]+)?";
		String subsequentNamesRegex = "(,\\s+\\w+)*";
		String regex = prefixRegex + typeRegex + firstNameRegex + subsequentNamesRegex + ";";
		Matcher matcher = Pattern.compile(regex).matcher(source);
		while (matcher.find()) {
			String type = matcher.group(1);
			List<String> names = new ArrayList<>();
			names.add(matcher.group(2));
			String subsequentNames = matcher.group(3);
			if (subsequentNames != null) {
				String[] split = subsequentNames.split(",\\s+");
				names.addAll(asList(split).subList(1, split.length));
			}
			DEBUG("Found " + type + " parameters: " + names.stream().reduce((a, b) -> a + ", " + b).orElse(""));
			for (String name : names) {
				parameters.add(fromType(type, name));
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
