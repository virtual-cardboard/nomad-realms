package engine.visuals.lwjgl.render;

/**
 * Contains a {@link VertexArrayObject} and a {@link Material}.
 */
public class Mesh {

	private final VertexArrayObject vao;
	private final Material material;

	public Mesh(VertexArrayObject vao, Material material) {
		this.vao = vao;
		this.material = material;
	}

	public VertexArrayObject vao() {
		return vao;
	}

	public Material material() {
		return material;
	}

}
