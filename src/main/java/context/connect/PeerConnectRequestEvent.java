package context.connect;

import static context.data.user.LocalUser.LOCAL_USER;

import java.io.Serializable;

import event.network.NomadRealmsNetworkEvent;

public class PeerConnectRequestEvent extends NomadRealmsNetworkEvent implements Serializable {

	private static final long serialVersionUID = 3967611254630803156L;

	public PeerConnectRequestEvent() {
		super(LOCAL_USER);
	}

	public PeerConnectRequestEvent(long time) {
		super(time, LOCAL_USER);
	}

}
