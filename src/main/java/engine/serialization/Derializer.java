package engine.serialization;

public abstract class Derializer<T> {

	public abstract byte[] serialize(T o);

	public abstract T deserialize(byte[] b);

}
