package context.connect;

import static context.data.user.LocalUser.LOCAL_USER;

import java.io.Serializable;

import common.event.NetworkEvent;
import common.source.GameSource;

public class PeerConnectResponseEvent extends NetworkEvent implements Serializable {

	private static final long serialVersionUID = -2559843323302358500L;

	public PeerConnectResponseEvent() {
		super(LOCAL_USER);
	}

	public PeerConnectResponseEvent(long time, GameSource source) {
		super(time, source);
	}

}
