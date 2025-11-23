package nomadrealms.user.data;

/**
 * A data container for all game data that is not crucial to the live game. This includes saves, settings, etc.
 * <p>
 * Please use appropriate subclasses to organise different types of data, and appropriately release resources for
 * garbage collection.
 *
 * @author Lunkle
 * @see SavesData
 */
public class GameData {

	public static final String STORAGE_FOLDER = "NomadRealms";

	private SavesData savesData;
	private PreferencesData preferencesData;

	public GameData() {
		String userHome = System.getProperty("user.home");
		savesData = new SavesData(userHome);
	}

	public SavesData saves() {
		return savesData;
	}


}
