package engine.serialization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;

/**
 * Tracks object identity for one serialization stream. The Derializer serializes by value, but game state graphs
 * contain cycles (a map referencing its world) and aliases (the nomad referenced both by the world and by the tile
 * it stands on). By writing a reference id the first time an object is encountered and only a back-reference id on
 * later occurrences, cycles terminate and deserialization rebuilds one shared instance per object instead of
 * duplicating it.
 * <p>
 * A fresh context is created for every top-level serialization or deserialization, so objects, ids, and
 * back-references never cross between separate streams; ids are only meaningful within one.
 *
 * @author Lunkle
 */
public class DerializerContext {

	/** Object to id mapping for the serialize side, compared by identity rather than equality. */
	private final IdentityHashMap<Object, Integer> idByObject = new IdentityHashMap<>();

	/** Id to object mapping for the deserialize side. */
	private final HashMap<Integer, Object> objectById = new HashMap<>();

	/** Ids that were referenced but whose body has not been written yet. */
	private final java.util.HashSet<Integer> pendingBodies = new HashSet<>();

	private int nextId = 0;

	/**
	 * Returns the reference id of the object for the serialize side. Null serializes to -1. The first occurrence
	 * of an object assigns a fresh id and registers it; the writer is then responsible for writing the object body
	 * (and revoking the pending state) immediately afterwards, before any nested references can be written.
	 *
	 * @param o the object to reference, may be null
	 * @return the reference id of the object, or -1 if it is null
	 */
	public int ref(Object o) {
		if (o == null) {
			return -1;
		}
		Integer existing = idByObject.get(o);
		if (existing != null) {
			return existing;
		}
		int id = nextId++;
		idByObject.put(o, id);
		pendingBodies.add(id);
		return id;
	}

	/** @return true if a body still has to be written for the object with the given id */
	public boolean bodyPending(int id) {
		return pendingBodies.contains(id);
	}

	/**
	 * Marks the body of the object with the given id as written, revoking the pending state so that nested
	 * references back to this object during the body write do not recurse into it again.
	 *
	 * @param id the reference id of the object whose body is being written
	 */
	public void bodyWritten(int id) {
		pendingBodies.remove(id);
	}

	/**
	 * Returns the deserialized instance registered under the given id, or null if this is the first occurrence of
	 * the id, in which case the caller creates the instance and registers it before reading its body.
	 *
	 * @param id the reference id to look up
	 * @return the instance registered under the id, or null if unknown
	 */
	public Object instance(int id) {
		return objectById.get(id);
	}

	/**
	 * Registers the instance for the given id on the deserialize side. Must happen before the object body is read
	 * so that back-references within the body resolve to the same instance while it is still being filled.
	 *
	 * @param id   the reference id the body was written with
	 * @param view the instance to register
	 */
	public void register(int id, Object o) {
		objectById.put(id, o);
	}

}
