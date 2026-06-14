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
		int d = divisor == 0 ? 1 : divisor;
		if (dimensions <= 4) {
			glVertexAttribPointer(index, dimensions, GL_FLOAT, false, s, offset);
			glEnableVertexAttribArray(index);
			glVertexAttribDivisor(index, d);
		} else {
			// For mat4, we need to enable 4 attribute locations
			int numLocations = (dimensions + 3) / 4;
			for (int i = 0; i < numLocations; i++) {
				glEnableVertexAttribArray(index + i);
				glVertexAttribPointer(index + i, 4, GL_FLOAT, false, s, offset + i * 16);
				glVertexAttribDivisor(index + i, d);
			}
		}
	}

}
