package engine.common.java;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;

import java.util.Map;

public class JavaUtil {

	public static <A, B> Iterable<Pair<A, B>> pairs(Map<A, B> map) {
		return new PairIterable<>(map);
	}

	public static <A, B> Pair<A, B> pair(A a, B b) {
		return new Pair<>(a, b);
	}

	public static <A, B, C> Triple<A, B, C> triple(A a, B b, C c) {
		return new Triple<>(a, b, c);
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = copyOf(first, first.length + second.length);
		arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static <T> T[] flatten(T[][] array) {
		int length = 0;
		for (T[] subArray : array) {
			length += subArray.length;
		}
		T[] result = copyOf(array[0], length);
		int offset = array[0].length;
		for (int i = 1; i < array.length; i++) {
			arraycopy(array[i], 0, result, offset, array[i].length);
			offset += array[i].length;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> Pair<Integer, T>[] enumerate(T[] array) {
		Pair<Integer, T>[] result = new Pair[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Pair<>(i, array[i]);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> Triple<Integer, Integer, T>[] enumerate(T[][] array) {
		Triple<Integer, Integer, T>[] result = new Triple[array.length * array[0].length];
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				result[index++] = new Triple<>(i, j, array[i][j]);
			}
		}
		return result;
	}

}
