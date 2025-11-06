package nomadrealms.game.world.map.generation.status.biome.nomenclature;

import java.util.HashMap;
import java.util.Map;

import engine.common.math.Vector2i;

/**
 * Represents the overall categorization of a biome, based on the 7 categories defined by NASA plus "Acquatic". Perhaps
 * eventually fictional categories will be added.
 * <br><br>
 * Reference: <a href="https://earthobservatory.nasa.gov/biome">NASA Earth Observatory</a>
 * <br><br>
 * The 7 non-fictional categories are:
 * <ul>
 *     <li>Rainforest</li>
 *     <li>Grassland</li>
 *     <li>Coniferous Forest</li>
 *     <li>Temperate Deciduous Forest</li>
 *     <li>Desert</li>
 *     <li>Tundra</li>
 *     <li>Shrubland</li>
 * </ul>
 * Plus Aquatic.
 * <ul>
 *     <li>Aquatic</li>
 * </ul>
 *
 * @author Lunkle
 */
public enum BiomeCategory {

	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/rainforest.jpg"/>
	 */
	RAINFOREST,
	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/grassland.jpg"/>
	 */
	GRASSLAND,
	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/conifForest.jpg"/>
	 */
	CONIFEROUS_FOREST,
	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/tempDecidForest.jpg"/>
	 */
	TEMPERATE_DECIDUOUS_FOREST,
	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/desert.jpg"/>
	 */
	DESERT,
	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/tundra.jpg"/>
	 */
	TUNDRA,
	/**
	 * <img src="https://earthobservatory.nasa.gov/img/biome/shrubland.jpg"/>
	 */
	SHRUBLAND,
	AQUATIC,
	;

	/**
	 * Biome distribution <a href="https://www.desmos.com/calculator/tbzpxzn6hf">visualized</a>
	 */
	public static final Map<BiomeCategory, Vector2i> TEMPERATURE_HUMIDITY_VALUES = new HashMap<>();

	static {
		TEMPERATURE_HUMIDITY_VALUES.put(RAINFOREST, new Vector2i(22, 2000));
		TEMPERATURE_HUMIDITY_VALUES.put(GRASSLAND, new Vector2i(5, 700));
		TEMPERATURE_HUMIDITY_VALUES.put(CONIFEROUS_FOREST, new Vector2i(-10, 600));
		TEMPERATURE_HUMIDITY_VALUES.put(TEMPERATE_DECIDUOUS_FOREST, new Vector2i(0, 1100));
		TEMPERATURE_HUMIDITY_VALUES.put(DESERT, new Vector2i(17, 250));
		TEMPERATURE_HUMIDITY_VALUES.put(TUNDRA, new Vector2i(-11, 200));
		TEMPERATURE_HUMIDITY_VALUES.put(SHRUBLAND, new Vector2i(12, 600));
	}

	public static final int TEMPERATURE_FLOOR = -16;
	public static final int TEMPERATURE_CEIL = 24;
	public static final int HUMIDITY_FLOOR = -500;
	public static final int HUMIDITY_CEIL = 2500;

}
