package model.timeline;

public interface VersionDiff<T> {

	public VersionObject<T> applyTo(VersionObject<T> object);

}
