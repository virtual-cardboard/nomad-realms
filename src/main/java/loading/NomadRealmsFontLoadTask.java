package loading;

import static loading.NomadRealmsLoadingInfo.SOURCE_PATH;

import java.io.File;

import context.visuals.lwjgl.Texture;
import engine.common.loader.loadtask.FontLoadTask;

public class NomadRealmsFontLoadTask extends FontLoadTask {

	public NomadRealmsFontLoadTask(String fontPath, Texture fontTexture) {
		super(new File(SOURCE_PATH + fontPath), fontTexture);
	}

}
