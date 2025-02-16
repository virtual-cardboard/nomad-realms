package nomadrealms.game.world.map.generation.status.points.point;

import common.math.Matrix4f;
import common.math.Vector2f;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.generation.status.points.PointsGenerationStep;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static nomadrealms.game.world.map.area.Tile.TILE_HORIZONTAL_SPACING;
import static nomadrealms.game.world.map.area.Tile.TILE_VERTICAL_SPACING;
import static nomadrealms.game.world.map.area.coordinate.ChunkCoordinate.CHUNK_SIZE;
import static nomadrealms.game.world.map.area.coordinate.ZoneCoordinate.ZONE_SIZE;

/**
 * Represents a point of interest in the zone.
 *
 * @author Lunkle
 * @see PointsGenerationStep
 */
public class PointOfInterest {

    private Vector2f position;
    private int rarity;
    private int size;

    public PointOfInterest(Vector2f position, int rarity, int size) {
        this.position = position;
        this.rarity = rarity;
        this.size = size;
    }

    /**
     * The position of this point of interest relative to the top left corner of the zone. x and y are in the range [0,
     * 1].
     */
    public Vector2f position() {
        return position;
    }

    /**
     * The rarity of this point of interest.
     *
     * @return an integer representing the rarity level.
     */
    public int rarity() {
        return rarity;
    }

    /**
     * The size of this point of interest.
     *
     * @return an integer representing the size of the point.
     */
    public int size() {
        return size;
    }

    public void render(Zone zone, RenderingEnvironment re) {
        float zoneSizeHorizontal = ZONE_SIZE * CHUNK_SIZE * TILE_HORIZONTAL_SPACING;
        float zoneSizeVertical = ZONE_SIZE * CHUNK_SIZE * TILE_VERTICAL_SPACING;
        Vector2f screenPos = position.scale(zoneSizeHorizontal, zoneSizeVertical).add(zone.pos()).sub(re.camera.position());
        System.out.println("Rendering point of interest [" + position + "] at " + screenPos + " for zone " + zone.coord() + "currently at " + re.camera.position());
        DefaultFrameBuffer.instance().render(() -> {
            re.circleShaderProgram
                    .set("color", toRangedVector(rgb(100, 0, 0)))
                    .set("transform", new Matrix4f(screenPos.x(), screenPos.y(), 100, 100, re.glContext))
                    .use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
        });
    }
}
