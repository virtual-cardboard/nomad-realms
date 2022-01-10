package model.world.layer.finallayer;

import static model.world.Biome.DESERT;
import static model.world.Biome.OCEAN;
import static model.world.Biome.PLAINS;

import common.math.Vector2i;
import graphics.noise.OpenSimplexNoise;
import model.state.GameState;
import model.world.Biome;
import model.world.Seed;
import model.world.Tile;
import model.world.TileChunk;
import model.world.TileType;
import model.world.layer.actorcluster.ActorClusterChunk;

public class FinalLayerChunk extends ActorClusterChunk {

	private static final double MOISTURE_SCALE = 50;
	private static final double ELEVATION_SCALE = 20;

	private Tile[][] tiles;

	public FinalLayerChunk(Vector2i pos) {
		super(pos);
	}

	public static FinalLayerChunk create(Vector2i pos, ActorClusterChunk prev, TileChunk[][] neighbours, long worldSeed) {
		OpenSimplexNoise moistureNoise = new OpenSimplexNoise(Seed.moisture(worldSeed));
		OpenSimplexNoise elevNoise = new OpenSimplexNoise(Seed.elevation(worldSeed));

		TileType[][] tileTypes = new TileType[16][16];
		Biome biomeType;
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				double moisture = moistureNoise.eval((x + pos.x * 16) / MOISTURE_SCALE,
						(y + (x % 2) * 0.5 + pos.y * 16) / MOISTURE_SCALE);
				double elevation = elevNoise.eval((x + pos.x * 16) / ELEVATION_SCALE,
						(y + (x % 2) * 0.5 + pos.y * 16) / ELEVATION_SCALE);
				if (moisture > 0.7 && elevation <= 0.5) {
					biomeType = OCEAN;
				} else if (moisture <= 0.3 && elevation > 0.5) {
					biomeType = DESERT;
				} else {
					biomeType = PLAINS;
				}
				tileTypes[y][x] = biomeType.tileTypeFunction.apply(elevation, moisture);
			}
		}

		FinalLayerChunk c = new FinalLayerChunk(pos);
		c.nodes = prev.nodes();
		c.relocatedNodes = prev.nodes();
		c.actors = prev.actors();
		c.tiles = new Tile[CHUNK_SIDE_LENGTH][CHUNK_SIDE_LENGTH];
		for (int row = 0; row < CHUNK_SIDE_LENGTH; row++) {
			for (int col = 0; col < CHUNK_SIDE_LENGTH; col++) {
				c.tiles[row][col] = new Tile(col, row, tileTypes[row][col], c);
			}
		}

		return c;
	}

	@Override
	public int layer() {
		return 3;
	}

	public Tile tile(int x, int y) {
		return tiles[y][x];
	}

	public Tile tile(Vector2i coords) {
		return tiles[coords.y][coords.x];
	}

	public void addTo(GameState state) {
		for (int i = 0; i < actors.length; i++) {
			state.add(actors[i]);
		}
	}

}
