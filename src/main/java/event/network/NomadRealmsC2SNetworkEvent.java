package event.network;

import derealizer.format.SerializationPojo;
import engine.common.networking.packet.HttpRequestModel;
import engine.common.networking.packet.RequestMethod;
import event.NomadRealmsGameEvent;
import networking.protocols.NomadRealmsC2SNetworkProtocol;

public abstract class NomadRealmsC2SNetworkEvent extends NomadRealmsGameEvent implements SerializationPojo<NomadRealmsC2SNetworkProtocol> {

	public NomadRealmsC2SNetworkEvent() {
	}

	public HttpRequestModel toHttpRequestModel(String urlPath) {
		return new HttpRequestModel(serialize(), urlPath);
	}

	public HttpRequestModel toHttpRequestModel(String urlPath, RequestMethod requestMethod) {
		return new HttpRequestModel(serialize(), urlPath, requestMethod);
	}

}
