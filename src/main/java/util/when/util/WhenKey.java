package util.when.util;

import util.when.util.key.WhenKeyPressed;
import util.when.util.key.WhenKeyReleased;
import util.when.util.key.WhenKeyRepeated;

public class WhenKey {

	private final int key;

	public WhenKey(int key) {
		this.key = key;
	}

	public WhenKeyPressed isPressed() {
		return new WhenKeyPressed(key);
	}

	public WhenKeyReleased isReleased() {
		return new WhenKeyReleased(key);
	}

	public WhenKeyRepeated isRepeated() {
		return new WhenKeyRepeated(key);
	}

}
