package loading;

import static loading.NomadRealmsLoadingInfo.SOURCE_PATH;

import context.visuals.lwjgl.Texture;
import engine.common.loader.loadtask.TextureLoadTask;

public class NomadsTextureLoadTask extends TextureLoadTask {

	public NomadsTextureLoadTask(String path) {
		super(SOURCE_PATH + path);
	}

	public NomadsTextureLoadTask(Texture texture, String path) {
		super(texture, SOURCE_PATH + path);
	}

}
