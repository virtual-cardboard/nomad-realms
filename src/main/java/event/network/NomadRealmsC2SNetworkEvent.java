package event.network;

import derealizer.Derealizable;
import engine.common.networking.packet.HttpRequestModel;
import engine.common.networking.packet.RequestMethod;
import event.NomadRealmsGameEvent;

public abstract class NomadRealmsC2SNetworkEvent extends NomadRealmsGameEvent implements Derealizable {

	public NomadRealmsC2SNetworkEvent() {
	}

	public HttpRequestModel toHttpRequestModel(String urlPath) {
		return new HttpRequestModel(serialize(), urlPath);
	}

	public HttpRequestModel toHttpRequestModel(String urlPath, RequestMethod requestMethod) {
		return new HttpRequestModel(serialize(), urlPath, requestMethod);
	}

}
