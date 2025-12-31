package nomadrealms.context.game.world.map.generation.status.structure;

import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import nomadrealms.context.game.actor.structure.TreeStructure;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.generation.status.GenerationStep;
import nomadrealms.context.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.context.game.world.map.tile.GrassTile;

public class StructureGenerationStep extends GenerationStep {

	private static final float TREE_NOISE_SCALE = 0.05f;
	private static final double TREE_THRESHOLD = 0.6;

	/**
	 * No-arg constructor for serialization.
	 */
	protected StructureGenerationStep() {
		super(null, 0);
	}

	public StructureGenerationStep(Zone zone, long seed) {
		super(zone, seed);
	}

	@Override
	public GenerationStepStatus status() {
		return GenerationStepStatus.STRUCTURES;
	}

	@Override
	public void generate(Zone[][] surrounding) {
		StructureNoiseGenerator noise = new StructureNoiseGenerator(worldSeed, TREE_NOISE_SCALE);
		for (int zx = 0; zx < ZONE_SIZE; zx++) {
			for (int zy = 0; zy < ZONE_SIZE; zy++) {
				ChunkCoordinate chunkCoord = zone.coord().chunkCoordinates()[zx][zy];
				Chunk chunk = zone.getChunk(chunkCoord);
				if (chunk == null) {
					continue;
				}
				for (int cx = 0; cx < CHUNK_SIZE; cx++) {
					for (int cy = 0; cy < CHUNK_SIZE; cy++) {
						TileCoordinate tileCoord = chunkCoord.tileCoordinates()[cx][cy];
						Tile tile = chunk.getTile(tileCoord);
						if (tile instanceof GrassTile && tile.actor() == null) {
							float value = noise.noise().eval(tile.coord());
							if (value > TREE_THRESHOLD) {
								TreeStructure tree = new TreeStructure();
								tree.tile(tile);
								zone.world().addActor(tree);
							}
						}
					}
				}
			}
		}
	}
}