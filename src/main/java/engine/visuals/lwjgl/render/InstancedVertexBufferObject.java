package engine.visuals.lwjgl.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstancedVertexBufferObject extends VertexBufferObject {

	@Override
	protected void enableVertexAttribArray() {
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glVertexAttribPointer(index, dimensions, GL_FLOAT, false, dimensions * Float.BYTES, 0);
		glEnableVertexAttribArray(index);
		glVertexAttribDivisor(index, 1);
	}

}
