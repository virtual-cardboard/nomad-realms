package event.network;

import common.event.NetworkEvent;
import common.source.GameSource;

public abstract class NomadRealmsNetworkEvent extends NetworkEvent {

	private static final long serialVersionUID = 8361879443286050547L;

	public NomadRealmsNetworkEvent(GameSource source) {
		super(source);
	}

	public NomadRealmsNetworkEvent(long time, GameSource source) {
		super(time, source);
	}

}
