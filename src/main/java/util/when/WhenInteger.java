package util.when;

import util.when.util.WhenKey;

public class WhenInteger {

	private final Integer value;

	public WhenInteger(Integer value) {
		this.value = value;
	}

	public WhenKey key() {
		return new WhenKey(value);
	}

}
