package nomadrealms.server.storage;

import java.util.List;

/**
 * An abstraction over save storage. The server stores serialized {@link nomadrealms.context.game.GameState} blobs
 * addressed by world name; where the blobs end up is decided by the implementation. This class is platform-agnostic
 * on purpose: the local filesystem implementation backs the default experience, while remote implementations
 * (object storage) allow saves to survive on ephemeral hosting such as Render.
 *
 * @author Lunkle
 */
public abstract class SaveStorage {

	/**
	 * Saves the serialized game state blob of the given world, overwriting any previous save of the same world.
	 *
	 * @param worldName the name of the world to save
	 * @param data      the serialized game state blob
	 */
	public abstract void save(String worldName, byte[] data);

	/**
	 * Loads the serialized game state blob of the given world.
	 *
	 * @param worldName the name of the world to load
	 * @return the serialized game state blob, or null if no save exists for the world
	 */
	public abstract byte[] load(String worldName);

	/** @return metadata of every save in this storage */
	public abstract List<SaveMetadata> list();

	/**
	 * Deletes the save of the given world, if present.
	 *
	 * @param worldName the name of the world to delete the save of
	 * @return true if a save was deleted
	 */
	public abstract boolean delete(String worldName);

}
