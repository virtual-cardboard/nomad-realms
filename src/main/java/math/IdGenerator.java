package math;

/**
 * Generates ids. See {@link #genId()}
 */
public class IdGenerator {

	private long nextId = Long.MIN_VALUE;

	/**
	 * Creates an {@link IdGenerator} that starts counting from {@link Long#MIN_VALUE}.
	 * <p>
	 * Use this constructor for generating ids of NPCs.
	 */
	public IdGenerator() {
	}

	/**
	 * Creates an {@link IdGenerator} that starts counting from <code>idRange * 1000000000</code>.
	 * <p>
	 * Id ranges have size one billion.
	 * <p>
	 * Use this constructor for generating ids for player-generated actors (e.g. structures, creatures, effect chains)
	 *
	 * @param idRange the id range
	 */
	public IdGenerator(int idRange) {
		// Id ranges have size one billion
		this.nextId = idRange * 1000000000L;
	}

	public long genId() {
		return nextId++;
	}

	public void setNextId(long nextId) {
		this.nextId = nextId;
	}

}
