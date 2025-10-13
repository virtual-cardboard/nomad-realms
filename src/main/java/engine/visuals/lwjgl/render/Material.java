package engine.visuals.lwjgl.render;

/**
 * Describes what a {@link Mesh} looks like.
 */
public class Material {

	private final ShaderProgram shaderProgram;

	public Texture diffuse;
	public Texture specular;
//	public Texture ambient;
//	public Texture emissive;
//	public Texture height;
//	public Texture normals;
//	public Texture shininess;
//	public Texture opacity;
//	public Texture displacement;

	public Material(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	public ShaderProgram shaderProgram() {
		return shaderProgram;
	}

}
