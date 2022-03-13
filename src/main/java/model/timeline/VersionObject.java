package model.timeline;

public abstract class VersionObject<T> {

	protected T object;

	public VersionObject(T object) {
		this.object = object;
	}

	public T get() {
		return object;
	}

	public abstract VersionDiff<T> diff(T other);

}
