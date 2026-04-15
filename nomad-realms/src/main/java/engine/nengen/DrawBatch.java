package engine.nengen;

import static engine.common.colour.Colour.normalizedA;
import static engine.common.colour.Colour.normalizedB;
import static engine.common.colour.Colour.normalizedG;
import static engine.common.colour.Colour.normalizedR;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Matrix4f;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;

public class DrawBatch {

	private final List<Matrix4f> transforms = new ArrayList<>();
	private final List<Integer> colors = new ArrayList<>();

	private VertexArrayObject vao;
	private ShaderProgram shaderProgram;
	private GLContext glContext;

	public DrawBatch() {
	}

	public DrawBatch vao(VertexArrayObject vao) {
		this.vao = vao;
		return this;
	}

	public DrawBatch shaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		return this;
	}

	public DrawBatch glContext(GLContext glContext) {
		this.glContext = glContext;
		return this;
	}

	public void add(Matrix4f transform, int color) {
		transforms.add(transform);
		colors.add(color);
	}

	public void draw() {
		if (transforms.isEmpty()) {
			return;
		}

		int count = transforms.size();
		float[] transformData = new float[count * 16];
		float[] colorData = new float[count * 4];

		for (int i = 0; i < count; i++) {
			transforms.get(i).store(transformData, i * 16);
			int color = colors.get(i);
			colorData[i * 4 + 0] = normalizedR(color);
			colorData[i * 4 + 1] = normalizedG(color);
			colorData[i * 4 + 2] = normalizedB(color);
			colorData[i * 4 + 3] = normalizedA(color);
		}

		// Collect base VBOs from the original VAO
		List<VertexBufferObject> baseVbos = vao.vbos();

		// Create instanced VBOs
		VertexBufferObject tVbo = new VertexBufferObject()
				.index(2)
				.dimensions(4)
				.data(transformData)
				.stride(16 * Float.BYTES)
				.offset(0)
				.load();
		tVbo.divisor(1);

		VertexBufferObject tVbo2 = new VertexBufferObject()
				.index(3)
				.dimensions(4)
				.data(transformData)
				.stride(16 * Float.BYTES)
				.offset(4 * Float.BYTES)
				.load();
		tVbo2.divisor(1);

		VertexBufferObject tVbo3 = new VertexBufferObject()
				.index(4)
				.dimensions(4)
				.data(transformData)
				.stride(16 * Float.BYTES)
				.offset(8 * Float.BYTES)
				.load();
		tVbo3.divisor(1);

		VertexBufferObject tVbo4 = new VertexBufferObject()
				.index(5)
				.dimensions(4)
				.data(transformData)
				.stride(16 * Float.BYTES)
				.offset(12 * Float.BYTES)
				.load();
		tVbo4.divisor(1);

		VertexBufferObject cVbo = new VertexBufferObject()
				.index(6)
				.dimensions(4)
				.data(colorData)
				.load();
		cVbo.divisor(1);

		// Create a new VAO for this draw call
		VertexArrayObject instancedVao = new VertexArrayObject()
				.ebo(vao.ebo())
				.vbos(baseVbos.toArray(new VertexBufferObject[0]))
				.vbos(tVbo, tVbo2, tVbo3, tVbo4, cVbo)
				.load();

		shaderProgram.use(glContext);
		instancedVao.drawInstanced(glContext, count);

		// Cleanup to avoid leaking OpenGL resources
		tVbo.delete();
		tVbo2.delete();
		tVbo3.delete();
		tVbo4.delete();
		cVbo.delete();
		instancedVao.delete();
	}

}
