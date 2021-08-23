package math;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CTRRandom {

	public static long toLong(int a, int b) {
		return (long) a << 32 | b & 0xFFFFFFFFL;
	}

	public static int toInt1(long c) {
		return (int) (c >> 32);
	}

	public static int toInt2(long c) {
		return (int) c;
	}

	// 16 bytes IV
	public static byte[] getRandomNonce() {
		byte[] nonce = new byte[16];
		new SecureRandom().nextBytes(nonce);
		return nonce;
	}

	// 256 bits AES secret key
	public static SecretKey getAESKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256, SecureRandom.getInstanceStrong());
		return keyGen.generateKey();
	}

	// hex representation
	public static String hex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}

	// print hex with block size split
	public static String hexWithBlockSize(byte[] bytes, int blockSize) {
		String hex = hex(bytes);
		// one hex = 2 chars
		blockSize = blockSize * 2;
		// better idea how to print this?
		List<String> result = new ArrayList<>();
		int index = 0;
		while (index < hex.length()) {
			result.add(hex.substring(index, Math.min(index + blockSize, hex.length())));
			index += blockSize;
		}

		return result.toString();

	}

//	public static byte[] encrypt(byte[] pText, SecretKey secret, byte[] iv) throws Exception {
//		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
//		cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
//		byte[] encryptedText = cipher.doFinal(pText);
//		return encryptedText;
//	}

}
