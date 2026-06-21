package engine.nengen;

import static engine.common.colour.Colour.normalizedA;
import static engine.common.colour.Colour.normalizedB;
import static engine.common.colour.Colour.normalizedG;
import static engine.common.colour.Colour.normalizedR;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

import java.util.ArrayList;
import java.util.List;

import engine.common.math.Matrix4f;
import engine.common.math.Vector4f;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
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
	private final List<Vector4f> crops = new ArrayList<>();

	private VertexArrayObject vao;
	private ShaderProgram shaderProgram;
	private GLContext glContext;
	private Texture texture;

	private VertexArrayObject instancedVao;
	private VertexBufferObject tVbo;
	private VertexBufferObject cVbo;
	private VertexBufferObject cropVbo;

	private int lastCount = -1;

	public DrawBatch() {
	}

	public DrawBatch texture(Texture texture) {
		this.texture = texture;
		return this;
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
		add(transform, color, new Vector4f(0, 0, 1, 1));
	}

	public void add(Matrix4f transform, int color, Vector4f crop) {
		transforms.add(transform);
		colors.add(color);
		crops.add(crop);
	}

	public void clear() {
		transforms.clear();
		colors.clear();
		crops.clear();
	}

	public void draw() {
		if (transforms.isEmpty()) {
			return;
		}

		int count = transforms.size();
		float[] transformData = new float[count * 16];
		float[] colorData = new float[count * 4];
		float[] cropData = new float[count * 4];

		for (int i = 0; i < count; i++) {
			transforms.get(i).store(transformData, i * 16);
			int color = colors.get(i);
			colorData[i * 4 + 0] = normalizedR(color);
			colorData[i * 4 + 1] = normalizedG(color);
			colorData[i * 4 + 2] = normalizedB(color);
			colorData[i * 4 + 3] = normalizedA(color);
			Vector4f crop = crops.get(i);
			cropData[i * 4 + 0] = crop.x();
			cropData[i * 4 + 1] = crop.y();
			cropData[i * 4 + 2] = crop.z();
			cropData[i * 4 + 3] = crop.w();
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

			VertexBufferData cropVboData = new VertexBufferData()
					.data(cropData)
					.usage(GL_STREAM_DRAW)
					.load();

			cropVbo = new VertexBufferObject()
					.buffer(cropVboData)
					.index(7)
					.dimensions(4);
			cropVbo.divisor(1);

			instancedVao = new VertexArrayObject()
					.ebo(vao.ebo())
					.vbos(vao.vbos().toArray(new VertexBufferObject[0]))
					.vbos(tVbo, tVbo2, tVbo3, tVbo4, cVbo, cropVbo)
					.load();
			lastCount = count;
		} else {
			tVbo.data(transformData);
			cVbo.data(colorData);
			cropVbo.data(cropData);
			if (count > lastCount) {
				tVbo.reallocate();
				cVbo.reallocate();
				cropVbo.reallocate();
				lastCount = count;
			} else {
				tVbo.updateData();
				cVbo.updateData();
				cropVbo.updateData();
			}
		}

		shaderProgram.use(glContext);
		if (texture != null) {
			texture.bind(glContext);
		}
		instancedVao.drawInstanced(glContext, count);
	}

}
