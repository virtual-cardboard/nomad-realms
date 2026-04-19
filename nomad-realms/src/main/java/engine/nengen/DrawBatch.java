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
import engine.visuals.lwjgl.render.VertexBufferObject;

public class DrawBatch {

	private final List<Matrix4f> transforms = new ArrayList<>();
	private final List<Integer> colors = new ArrayList<>();

	private VertexArrayObject vao;
	private ShaderProgram shaderProgram;
	private GLContext glContext;

	private VertexArrayObject instancedVao;
	private VertexBufferObject tVbo;
	private VertexBufferObject tVbo2;
	private VertexBufferObject tVbo3;
	private VertexBufferObject tVbo4;
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
			tVbo = new VertexBufferObject()
					.index(2)
					.dimensions(4)
					.data(transformData)
					.stride(16 * Float.BYTES)
					.offset(0)
					.usage(GL_STREAM_DRAW)
					.load();
			tVbo.divisor(1);

			tVbo2 = new VertexBufferObject()
					.index(3)
					.dimensions(4)
					.data(transformData)
					.stride(16 * Float.BYTES)
					.offset(4 * Float.BYTES)
					.usage(GL_STREAM_DRAW)
					.load();
			tVbo2.divisor(1);

			tVbo3 = new VertexBufferObject()
					.index(4)
					.dimensions(4)
					.data(transformData)
					.stride(16 * Float.BYTES)
					.offset(8 * Float.BYTES)
					.usage(GL_STREAM_DRAW)
					.load();
			tVbo3.divisor(1);

			tVbo4 = new VertexBufferObject()
					.index(5)
					.dimensions(4)
					.data(transformData)
					.stride(16 * Float.BYTES)
					.offset(12 * Float.BYTES)
					.usage(GL_STREAM_DRAW)
					.load();
			tVbo4.divisor(1);

			cVbo = new VertexBufferObject()
					.index(6)
					.dimensions(4)
					.data(colorData)
					.usage(GL_STREAM_DRAW)
					.load();
			cVbo.divisor(1);

			instancedVao = new VertexArrayObject()
					.ebo(vao.ebo())
					.vbos(vao.vbos().toArray(new VertexBufferObject[0]))
					.vbos(tVbo, tVbo2, tVbo3, tVbo4, cVbo)
					.load();
			lastCount = count;
		} else {
			tVbo.data(transformData);
			tVbo2.data(transformData);
			tVbo3.data(transformData);
			tVbo4.data(transformData);
			cVbo.data(colorData);
			if (count > lastCount) {
				tVbo.reallocate();
				tVbo2.reallocate();
				tVbo3.reallocate();
				tVbo4.reallocate();
				cVbo.reallocate();
				lastCount = count;
			} else {
				tVbo.updateData();
				tVbo2.updateData();
				tVbo3.updateData();
				tVbo4.updateData();
				cVbo.updateData();
			}
		}

		shaderProgram.use(glContext);
		instancedVao.drawInstanced(glContext, count);
	}

}
