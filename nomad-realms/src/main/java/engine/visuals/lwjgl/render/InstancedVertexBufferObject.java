package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstancedVertexBufferObject extends VertexBufferObject {

	@Override
	protected void enableVertexAttribArray() {
		vboData.bind();
		int s = stride == 0 ? dimensions * Float.BYTES : stride;
		glVertexAttribPointer(index, dimensions, GL_FLOAT, false, s, offset);
		glEnableVertexAttribArray(index);
		glVertexAttribDivisor(index, 1);
	}

}
