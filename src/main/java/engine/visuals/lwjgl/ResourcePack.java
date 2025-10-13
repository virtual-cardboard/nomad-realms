package engine.visuals.lwjgl;

import java.util.HashMap;
import java.util.Map;

import engine.visuals.lwjgl.render.FrameBufferObject;
import engine.visuals.lwjgl.render.GLObject;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.lwjgl.render.VertexArrayObject;

/**
 * A nice leather pack of {@link VertexArrayObject VertexArrayObjects}, {@link ShaderProgram ShaderPrograms}, and other
 * objects that are likely to be used many times in many contexts.
 *
 * @author Jay
 */
public final class ResourcePack {

	private final Map<String, VertexArrayObject> vaos = new HashMap<>(4);
	private final Map<String, FrameBufferObject> fbos = new HashMap<>(2);
	private final Map<String, ShaderProgram> shaderPrograms = new HashMap<>(8);
	private final Map<String, Texture> textures = new HashMap<>(16);
	//	private final Map<String, GameFont> fonts = new HashMap<>(1);
//	private final Map<String, AudioClip> audioClips = new HashMap<>(0);
	private ShaderProgram defaultSP;

	private final GLContext glContext;

	public ResourcePack(GLContext glContext) {
		this.glContext = glContext;
	}

	public void init(ShaderProgram defaultSP) {
		this.defaultSP = defaultSP;
	}

	public VertexArrayObject getVAO(String name) {
		return vaos.get(name);
	}

	public void putVAO(String name, VertexArrayObject vao) {
		vaos.put(name, vao);
	}

	public FrameBufferObject getFBO(String name) {
		return fbos.get(name);
	}

	public void putFBO(String name, FrameBufferObject fbo) {
		fbos.put(name, fbo);
	}

	public ShaderProgram getShaderProgram(String name) {
		return shaderPrograms.get(name);
	}

	public <T extends ShaderProgram> T getShaderProgram(String name, Class<T> clazz) {
		return clazz.cast(shaderPrograms.get(name));
	}

	public void putShaderProgram(String name, ShaderProgram shaderProgram) {
		shaderPrograms.put(name, shaderProgram);
	}

	public Texture getTexture(String name) {
		return textures.get(name);
	}

	public void putTexture(String name, Texture texture) {
		textures.put(name, texture);
	}

//	public GameFont getFont(String name) {
//		return fonts.get(name);
//	}
//
//	public void putFont(String name, GameFont font) {
//		fonts.put(name, font);
//	}
//
//	public AudioClip getAudioClip(String name) {
//		return audioClips.get(name);
//	}
//
//	public void putAudioClip(String name, AudioClip audioClip) {
//		audioClips.put(name, audioClip);
//	}

	public ShaderProgram defaultShaderProgram() {
		return defaultSP;
	}

	public void terminate() {
		vaos.values().forEach(VertexArrayObject::delete);
		shaderPrograms.values().forEach(ShaderProgram::delete);
		textures.values().forEach(Texture::delete);
//		fonts.values().forEach(GameFont::delete);
		fbos.values().forEach(FrameBufferObject::delete);
//		audioClips.values().forEach(AudioClip::delete);
		defaultSP.delete();
	}

	public void put(String name, GLObject object) {
		object.putInto(name, this);
	}

}
