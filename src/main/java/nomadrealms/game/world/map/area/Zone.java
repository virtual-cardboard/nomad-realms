package nomadrealms.game.world.map.area;

import common.math.Vector2f;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.game.world.map.generation.status.biome.BiomeGenerationStep;
import nomadrealms.game.world.map.generation.status.points.PointsGenerationStep;
import nomadrealms.game.world.map.generation.status.points.point.PointOfInterest;
import nomadrealms.render.RenderingEnvironment;

import java.util.Random;

import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.chunkCoordinateOf;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;
import static nomadrealms.game.world.map.generation.status.GenerationStepStatus.EMPTY;

/**
 * A zone is a 16x16 grid of chunks. This is the optimal size for getting good layer-based map generation results, you
 * will see this a lot in the map generation code.
 * <br><br>
 * Some math:
 * <br>
 * Zone Dimensions = ({@link Tile#TILE_HORIZONTAL_SPACING TILE_X}, {@link Tile#TILE_VERTICAL_SPACING TILE_Y}) *
 * {@link ChunkCoordinate#CHUNK_SIZE CHUNK SIZE} = (30, 34.64) * 16 * 16 = (7680, 8867.84)
 * <br>
 */
public class Zone {

    private final transient Region region;
    private final ZoneCoordinate coord;

    private final Chunk[][] chunks;

    private GenerationStepStatus generationStatus = EMPTY;
    private BiomeGenerationStep biomeGenerationStep;
    private PointsGenerationStep pointsGenerationStep;

    private transient Random rng;
    private int rngCounter = 0;

    /**
     * No-arg constructor for serialization.
     */
    protected Zone() {
        this.region = null;
        this.coord = null;
        this.chunks = null;
    }


    /**
     * Initializes the random number generator for this zone. This is necessary after deserialization, since the RNG
     * cannot be serialized.
     */
    public void initRNG() {
        this.rng = new Random(coord.rngSeed());
        for (int i = 0; i < rngCounter; i++) {
            rng.nextInt();
        }
    }

    public Zone(World world, ZoneCoordinate coord, MapGenerationStrategy strategy) {
        this.region = world.getRegion(coord.region());
        this.coord = coord;
        
        initRNG();

        biomeGenerationStep = new BiomeGenerationStep(this, world.seed());
        pointsGenerationStep = new PointsGenerationStep(this, world.seed());

        this.chunks = strategy.generateZone(world, this);
    }

    public float nextRandomFloat() {
        return rng.nextFloat();
    }

    public void render(RenderingEnvironment re, Vector2f origin) {
        ChunkCoordinate chunkCoord = chunkCoordinateOf(origin);
        getChunk(chunkCoord).render(re);
        region.world().getChunk(chunkCoord.up()).render(re);
        region.world().getChunk(chunkCoord.down()).render(re);
        region.world().getChunk(chunkCoord.left()).render(re);
        region.world().getChunk(chunkCoord.right()).render(re);
        region.world().getChunk(chunkCoord.down().right()).render(re);
        region.world().getChunk(chunkCoord.right().right()).render(re);
        region.world().getChunk(chunkCoord.down().down()).render(re);

        if (re.showDebugInfo) {
            for (PointOfInterest poi : pointsGenerationStep.points()) {
                poi.render(this, re);
            }
        }
    }

    Chunk getChunk(ChunkCoordinate chunkCoord) {
        assert chunkCoord.zone().equals(coord);
        return chunks[chunkCoord.x()][chunkCoord.y()];
    }

    public void setChunk(int x, int y, Chunk chunk) {
        chunks[x][y] = chunk;
    }

    public Zone[][] getSurroundingZones(World world, int range) {
        //		Zone[][] zones = new Zone[range * 2 + 1][range * 2 + 1];
        //		for (int x = -range; x <= range; x++) {
        //			for (int y = -range; y <= range; y++) {
        //				ZoneCoordinate surroundingCoord = coord();
        //				for (int i = 0; i < Math.abs(x); i++) {
        //					surroundingCoord = x > 0 ? surroundingCoord.right() : surroundingCoord.left();
        //				}
        //				for (int i = 0; i < Math.abs(y); i++) {
        //					surroundingCoord = y > 0 ? surroundingCoord.down() : surroundingCoord.up();
        //				}
        //				zones[x + range][y + range] = world.getZone(surroundingCoord);
        //			}
        //		}
        return new Zone[0][0];
    }

    public BiomeGenerationStep biomeGenerationStep() {
        return biomeGenerationStep;
    }

    public PointsGenerationStep pointsGenerationStep() {
        return pointsGenerationStep;
    }

    private Vector2f indexPosition() {
        return new Vector2f(coord.x() * TILE_HORIZONTAL_SPACING, coord.y() * TILE_VERTICAL_SPACING).scale(ZONE_SIZE * CHUNK_SIZE);
    }

    /**
     * Returns the absolute position of the top left corner of this zone.
     *
     * @return the absolute position of the top left corner of this zone
     */
    public Vector2f pos() {
        return region.pos().add(indexPosition());
    }

    public Tile getTile(TileCoordinate tile) {
        assert tile.zone().equals(coord);
        return chunks[tile.chunk().x()][tile.chunk().y()].getTile(tile);
    }

    public ZoneCoordinate coord() {
        return coord;
    }

}
