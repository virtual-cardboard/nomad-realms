package math;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class NthRandom {

	private static long worldKey = new Random(5).nextLong();

	public static void main(String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		for (int i = 0; i < 1; i++) {
			long currentTimeMillis = System.currentTimeMillis();
			for (int j = 0; j < 10; j++) {
				Random random = new Random((worldKey ^ i) * (j + 1));
				random.nextLong();
				System.out.println(0.5 + (double) (random.nextInt() >> 1) / Integer.MAX_VALUE);
			}
			System.out.println(System.currentTimeMillis() - currentTimeMillis);
		}
	}

}
