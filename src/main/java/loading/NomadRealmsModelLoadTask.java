package loading;

import static loading.NomadRealmsLoadingInfo.SOURCE_PATH;

import context.ResourcePack;
import engine.common.loader.loadtask.ModelLoadTask;

public class NomadRealmsModelLoadTask extends ModelLoadTask {

	public NomadRealmsModelLoadTask(String path, ResourcePack modelTextureResourcePack) {
		super(SOURCE_PATH + path, modelTextureResourcePack);
	}

}
