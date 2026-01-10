package engine.visuals.lwjgl.render.meta;

import static java.util.Collections.addAll;

import java.util.ArrayList;

import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexArrayObject;

public class DrawFunction {

	private ArrayList<Texture> textures = new ArrayList<>();
	private VertexArrayObject vao;
	private GLContext glContext;
	private int drawMode = -1;
	private int first = 0;
	private int count = -1;

	public DrawFunction() {
	}

	public DrawFunction vao(VertexArrayObject vao) {
		this.vao = vao;
		return this;
	}

	public DrawFunction drawMode(int drawMode) {
		this.drawMode = drawMode;
		return this;
	}

	public DrawFunction first(int first) {
		this.first = first;
		return this;
	}

	public DrawFunction count(int count) {
		this.count = count;
		return this;
	}

	public DrawFunction textures(Texture... textures){
		addAll(this.textures, textures);
		return this;
	}

	public DrawFunction glContext(GLContext glContext) {
		this.glContext = glContext;
		return this;
	}

	public void draw() {
		for (Texture texture : textures) {
			texture.bind();
		}
		if (drawMode != -1) {
			vao.drawArrays(glContext, drawMode, first, count);
		} else {
			vao.draw(glContext);
		}
	}

}
