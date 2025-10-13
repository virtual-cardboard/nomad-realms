package engine.visuals.lwjgl.render;

import java.util.List;

import engine.visuals.lwjgl.GLContext;

/**
 * A 3D model consisting of one or more meshes.
 */
public class Model {

	private final List<Mesh> meshes;

	public Model(List<Mesh> meshes) {
		this.meshes = meshes;
	}

	public void draw(GLContext glContext) {

	}

	public List<Mesh> meshes() {
		return meshes;
	}

}
