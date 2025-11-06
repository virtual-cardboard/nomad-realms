package engine.common.misc;

import static engine.nengen.EngineConfiguration.DEBUG;

public class Data<T> {

	protected String name;
	protected T value;

	private Class<T> clazz;

	public Data(String name, Class<T> clazz) {
		this.name = name;
		if (DEBUG) {
			this.clazz = clazz;
		}
	}

	public Data(String name, T value, Class<T> clazz) {
		this(name, clazz);
		safeSet(value);
	}

	public String name() {
		return name;
	}

	public Object get() {
		return value;
	}

	public void safeSet(T value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public void set(Object value) {
		try {
			this.value = clazz == null ? (T) value : clazz.cast(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(value.getClass().getName() + " is an invalid type for " + name + " of type " + clazz.getName());
		}
	}

	@Override
	public String toString() {
		String valueString = value == null ? "" : (" = " + value);
		String typeString = clazz == null ? "" : (" (" + clazz.getName() + ")");
		return "[" + name + ":" + typeString + valueString + "]";
	}

}
