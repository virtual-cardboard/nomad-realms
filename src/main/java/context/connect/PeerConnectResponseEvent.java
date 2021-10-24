package context.connect;

import java.io.Serializable;

import common.event.NetworkEvent;
import common.source.GameSource;

public class PeerConnectResponseEvent extends NetworkEvent implements Serializable {

	private static final long serialVersionUID = -2559843323302358500L;

	public PeerConnectResponseEvent(GameSource source) {
		super(source);
	}

	public PeerConnectResponseEvent(long time, GameSource source) {
		super(time, source);
	}

}
