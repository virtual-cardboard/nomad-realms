package math;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class NthRandom {

	private static long worldKey = new Random(5).nextLong();

	public static void main(String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		for (int entityId = 0; entityId < 2; entityId++) {
			for (int frame = 0; frame < 100000; frame++) {
				for (int counter = 0; counter < 1; counter++) {
					Random random = new Random((worldKey ^ entityId) * (frame + 1));
					random.nextLong();
					System.out.println(((worldKey ^ entityId) * (frame + 1)) + " " + 0.5 + (double) (random.nextInt() >> 1) / Integer.MAX_VALUE);
				}
			}
			System.out.println("======");
		}
	}

}
