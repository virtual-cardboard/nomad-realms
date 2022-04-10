package loading;

import static loading.NomadRealmsLoadingInfo.SOURCE_PATH;

import engine.common.loader.loadtask.AudioClipLoadTask;

public class NomadRealmsAudioBufferLoadTask extends AudioClipLoadTask {

	public NomadRealmsAudioBufferLoadTask(String path) {
		super(SOURCE_PATH + path);
	}

}
