package networking.packet.cryption;

import java.security.PublicKey;

public class CryptionContext {

	private PublicKey serverPublicKey;

	public PublicKey getServerPublicKey() {
		return serverPublicKey;
	}

	public void setServerPublicKey(PublicKey serverPublicKey) {
		this.serverPublicKey = serverPublicKey;
	}

}
