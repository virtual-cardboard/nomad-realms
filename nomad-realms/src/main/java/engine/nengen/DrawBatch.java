package engine.nengen;

import static engine.common.colour.Colour.normalizedA;
import static engine.common.colour.Colour.normalizedB;
import static engine.common.colour.Colour.normalizedG;
import static engine.common.colour.Colour.normalizedR;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Matrix4f;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferData;
import engine.visuals.lwjgl.render.VertexBufferObject;

/**
 * A DrawBatch collects instance data (transforms and colors) and renders them in a single instanced draw call.
 * It reuses OpenGL resources (VAO and VBOs) across frames to minimize overhead.
 * <p>
 * Use {@link #add(Matrix4f, int)} to add instances, then {@link #draw()} to render them.
 * Call {@link #clear()} before each frame's data collection.
 */
public class DrawBatch {

	private final List<Matrix4f> transforms = new ArrayList<>();
	private final List<Integer> colors = new ArrayList<>();

	private VertexArrayObject vao;
	private ShaderProgram shaderProgram;
	private GLContext glContext;

	private VertexArrayObject instancedVao;
	private VertexBufferObject tVbo;
	private VertexBufferObject cVbo;

	private int lastCount = -1;

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

	public void clear() {
		transforms.clear();
		colors.clear();
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

		if (instancedVao == null) {
			VertexBufferData tVboData = new VertexBufferData()
					.data(transformData)
					.usage(GL_STREAM_DRAW)
					.load();

			tVbo = new VertexBufferObject()
					.buffer(tVboData)
					.index(2)
					.dimensions(4)
					.stride(16 * Float.BYTES)
					.offset(0);
			tVbo.divisor(1);

			VertexBufferObject tVbo2 = new VertexBufferObject()
					.buffer(tVboData)
					.index(3)
					.dimensions(4)
					.stride(16 * Float.BYTES)
					.offset(4 * Float.BYTES);
			tVbo2.divisor(1);

			VertexBufferObject tVbo3 = new VertexBufferObject()
					.buffer(tVboData)
					.index(4)
					.dimensions(4)
					.stride(16 * Float.BYTES)
					.offset(8 * Float.BYTES);
			tVbo3.divisor(1);

			VertexBufferObject tVbo4 = new VertexBufferObject()
					.buffer(tVboData)
					.index(5)
					.dimensions(4)
					.stride(16 * Float.BYTES)
					.offset(12 * Float.BYTES);
			tVbo4.divisor(1);

			VertexBufferData cVboData = new VertexBufferData()
					.data(colorData)
					.usage(GL_STREAM_DRAW)
					.load();

			cVbo = new VertexBufferObject()
					.buffer(cVboData)
					.index(6)
					.dimensions(4);
			cVbo.divisor(1);

			instancedVao = new VertexArrayObject()
					.ebo(vao.ebo())
					.vbos(vao.vbos().toArray(new VertexBufferObject[0]))
					.vbos(tVbo, tVbo2, tVbo3, tVbo4, cVbo)
					.load();
			lastCount = count;
		} else {
			tVbo.data(transformData);
			cVbo.data(colorData);
			if (count > lastCount) {
				tVbo.reallocate();
				cVbo.reallocate();
				lastCount = count;
			} else {
				tVbo.updateData();
				cVbo.updateData();
			}
		}

		shaderProgram.use(glContext);
		instancedVao.drawInstanced(glContext, count);
	}

}
