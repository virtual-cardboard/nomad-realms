package loading;

import static loading.NomadRealmsShaderLoadTask.SOURCE_PATH;

import common.loader.loadtask.TextureLoadTask;
import context.visuals.lwjgl.Texture;

public class NomadRealmsTextureLoadTask extends TextureLoadTask {

	public NomadRealmsTextureLoadTask(int textureUnit, String path) {
		super(textureUnit, SOURCE_PATH + path);
	}

	public NomadRealmsTextureLoadTask(Texture texture, String path) {
		super(texture, SOURCE_PATH + path);
	}

}
