package loading;

import static loading.NomadRealmsShaderLoadTask.SOURCE_PATH;

import common.loader.loadtask.TextureLoadTask;
import context.visuals.lwjgl.Texture;

public class NomadsTextureLoadTask extends TextureLoadTask {

	public NomadsTextureLoadTask(int textureUnit, String path) {
		super(textureUnit, SOURCE_PATH + path);
	}

	public NomadsTextureLoadTask(Texture texture, String path) {
		super(texture, SOURCE_PATH + path);
	}

}