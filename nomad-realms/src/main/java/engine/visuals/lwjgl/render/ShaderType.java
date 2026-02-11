package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public enum ShaderType {

	VERTEX(GL_VERTEX_SHADER),

	FRAGMENT(GL_FRAGMENT_SHADER),

	GEOMETRY(GL_GEOMETRY_SHADER);

	public final int type;

	ShaderType(int type) {
		this.type = type;
	}

}
