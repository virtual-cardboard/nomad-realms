package loading;

import static loading.NomadRealmsLoadingInfo.SOURCE_PATH;

import common.loader.loadtask.AudioBufferLoadTask;

public class NomadRealmsAudioBufferLoadTask extends AudioBufferLoadTask {

	public NomadRealmsAudioBufferLoadTask(String path) {
		super(SOURCE_PATH + path);
	}

}
