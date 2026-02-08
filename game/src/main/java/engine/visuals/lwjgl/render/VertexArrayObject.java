package engine.visuals.lwjgl.render;

import static java.util.Collections.addAll;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.ResourcePack;

/**
 * An object that contains data about a group of vertices in OpenGL. Learn more about them here: <a href=
 * "https://stackoverflow.com/questions/11821336/what-are-vertex-array-objects">
 * https://stackoverflow.com/questions/11821336/what-are-vertex-array-objects</a>
 *
 * @author Jay
 * @see VertexBufferObject
 * @see ElementBufferObject
 */
public class VertexArrayObject extends GLContainerObject {

	private final List<VertexBufferObject> vbos = new ArrayList<>();
	private ElementBufferObject ebo;

	@Override
	public void genID() {
		this.id = glGenVertexArrays();
		initialize();
	}

	/**
	 * Initializes the VAO by binding it and enabling all attached VBOs.
	 *
	 * @return this
	 */
	public VertexArrayObject load() {
		this.id = glGenVertexArrays();
		glBindVertexArray(id);
		initialize();
		enableVertexAttribArrays();
		return this;
	}

	public void draw(GLContext glContext) {
		bind(glContext);
		ebo.bind(glContext);
		glDrawElements(GL_TRIANGLES, ebo.size(), GL_UNSIGNED_INT, 0);
	}

	public void drawInstanced(GLContext glContext, int count) {
		bind(glContext);
		ebo.bind(glContext);
		glDrawElementsInstanced(GL_TRIANGLES, ebo.size(), GL_UNSIGNED_INT, 0, count);
	}

	public void enableVertexAttribArrays() {
		for (VertexBufferObject vbo : vbos) {
			vbo.enableVertexAttribArray();
		}
	}

	/**
	 * Tells OpenGL to enable the attached VBOs. Should only be called once after attaching all necessary VBOs.
	 *
	 * @see VertexBufferObject
	 */
	public void enableVertexAttribArrays(GLContext glContext) {
		bind(glContext);
		for (VertexBufferObject vbo : vbos) {
			vbo.enableVertexAttribArray();
		}
	}

	protected void bind(GLContext glContext) {
		verifyInitialized();
		if (glContext.vertexArrayID == id) {
			return;
		}
		glBindVertexArray(id);
		glContext.vertexArrayID = id;
	}

	public VertexArrayObject vbos(VertexBufferObject... vbo) {
		addAll(vbos, vbo);
		return this;
	}

	public VertexArrayObject ebo(ElementBufferObject ebo) {
		this.ebo = ebo;
		return this;
	}

	public void delete() {
		glDeleteVertexArrays(id);
	}

	@Override
	public void putInto(String name, ResourcePack resourcePack) {
		resourcePack.putVAO(name, this);
	}

}
