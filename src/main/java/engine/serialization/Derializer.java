package engine.serialization;

public interface Derializer<T> {

	byte[] serialize(T o);

	T deserialize(byte[] b);

}
