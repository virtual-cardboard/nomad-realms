package engine.context.input.networking.packet.cryption;

import static engine.context.input.networking.packet.cryption.EncryptionAlgorithmType.AES_CTR_PUBLIC_ENCRYPT;
import static engine.context.input.networking.packet.cryption.EncryptionAlgorithmType.AES_PUBLIC_ENCRYPT;
import static engine.context.input.networking.packet.cryption.EncryptionAlgorithmType.CLIENT_PRIVATE_ENCRYPT;
import static engine.context.input.networking.packet.cryption.EncryptionAlgorithmType.CLIENT_PUBLIC_ENCRYPT;
import static engine.context.input.networking.packet.cryption.EncryptionAlgorithmType.SERVER_PRIVATE_ENCRYPT;
import static engine.context.input.networking.packet.cryption.EncryptionAlgorithmType.SERVER_PUBLIC_ENCRYPT;

public enum DecryptionAlgorithmType {

	CLIENT_PUBLIC_DECRYPT(CLIENT_PUBLIC_ENCRYPT),
	CLIENT_PRIVATE_DECRYPT(CLIENT_PRIVATE_ENCRYPT),
	SERVER_PUBLIC_DECRYPT(SERVER_PUBLIC_ENCRYPT),
	SERVER_PRIVATE_DECRYPT(SERVER_PRIVATE_ENCRYPT),
	AES_PUBLIC_DECRYPT(AES_PUBLIC_ENCRYPT),
	AES_CTR_PUBLIC_DECRYPT(AES_CTR_PUBLIC_ENCRYPT);

	private final EncryptionAlgorithmType inverse;

	DecryptionAlgorithmType(EncryptionAlgorithmType inverse) {
		this.inverse = inverse;
	}

	public EncryptionAlgorithmType inverse() {
		return inverse;
	}

}
