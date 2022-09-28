package networking.protocols;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;
import event.network.c2s.CreateWorldRequestEvent;
import event.network.c2s.DeleteWorldRequestEvent;
import event.network.c2s.JoinClusterRequestEvent;
import event.network.c2s.JoinClusterResponseEvent;
import event.network.c2s.JoinClusterSuccessEvent;

public enum NomadRealmsC2SNetworkProtocol implements DerealizerEnum {

	JOIN_CLUSTER_REQUEST_EVENT(JoinClusterRequestEvent.class),
	JOIN_CLUSTER_RESPONSE_EVENT(JoinClusterResponseEvent.class),
	JOIN_CLUSTER_SUCCESS_EVENT(JoinClusterSuccessEvent.class),
	CREATE_WORLD_REQUEST_EVENT(CreateWorldRequestEvent.class),
	DELETE_WORLD_REQUEST_EVENT(DeleteWorldRequestEvent.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	NomadRealmsC2SNetworkProtocol(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	NomadRealmsC2SNetworkProtocol(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
		this.objClass = objClass;
		this.derealizerEnum = derealizerEnum;
	}

	@Override
	public Class<? extends Derealizable> objClass() {
		return objClass;
	}

	@Override
	public Class<? extends DerealizerEnum> derealizerEnum() {
		return derealizerEnum;
	}

}
