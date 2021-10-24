package context.connect;

import java.io.Serializable;

import common.event.NetworkEvent;
import common.source.GameSource;

public class PeerConnectRequestEvent extends NetworkEvent implements Serializable {

	private static final long serialVersionUID = 3967611254630803156L;

	public PeerConnectRequestEvent(GameSource source) {
		super(source);
	}

	public PeerConnectRequestEvent(long time, GameSource source) {
		super(time, source);
	}

}
