package nomadrealms.server.storage;

/**
 * Metadata about a single save, used for listing saves without loading their contents.
 *
 * @author Lunkle
 */
public class SaveMetadata {

	private final String worldName;
	private final long sizeBytes;
	private final long lastModifiedMillis;

	/**
	 * Creates a new <code>SaveMetadata</code>.
	 *
	 * @param worldName           the name of the world this save belongs to
	 * @param sizeBytes           the size of the serialized save in bytes
	 * @param lastModifiedMillis  the time of the last write, in milliseconds since the epoch
	 */
	public SaveMetadata(String worldName, long sizeBytes, long lastModifiedMillis) {
		this.worldName = worldName;
		this.sizeBytes = sizeBytes;
		this.lastModifiedMillis = lastModifiedMillis;
	}

	/** @return the name of the world this save belongs to */
	public String worldName() {
		return worldName;
	}

	/** @return the size of the serialized save in bytes */
	public long sizeBytes() {
		return sizeBytes;
	}

	/** @return the time of the last write, in milliseconds since the epoch */
	public long lastModifiedMillis() {
		return lastModifiedMillis;
	}

}
