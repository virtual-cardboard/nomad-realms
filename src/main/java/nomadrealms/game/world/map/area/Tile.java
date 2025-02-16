package nomadrealms.game.world.map.area;

import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector3f;
import nomadrealms.game.actor.HasTooltip;
import nomadrealms.game.event.Target;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.UIContent;
import nomadrealms.render.ui.tooltip.TooltipDeterminer;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import java.util.ArrayList;
import java.util.List;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

/**
 * A tile is a hexagon-shaped smallest unit of the map. It contains items and can be walked on, as well as being
 * targetable by the player.
 */
public class Tile implements Target, HasTooltip {

    public static final float TILE_RADIUS = 40;
    private static final float ITEM_SIZE = TILE_RADIUS * 0.6f;
    public static final float TILE_HORIZONTAL_SPACING = TILE_RADIUS * SIDE_LENGTH * 1.5f;
    public static final float TILE_VERTICAL_SPACING = TILE_RADIUS * HEIGHT * 2;

    private transient Chunk chunk;
    private TileCoordinate coord;

    private final List<WorldItem> items = new ArrayList<>();
    private WorldItem buried;

    protected int color = rgb(126, 200, 80);

    /**
     * No-arg constructor for serialization.
     */
    protected Tile() {
    }

    public Tile(Chunk chunk, TileCoordinate coord) {
        this.chunk = chunk;
        this.coord = coord;
    }

    /**
     * The position of thIS tile relative to the first tile of the chunk.
     *
     * @return
     */
    private Vector2f indexPosition() {
        Vector2f toCenter = new Vector2f(TILE_RADIUS * SIDE_LENGTH, TILE_RADIUS * HEIGHT);
        Vector2f base = new Vector2f(coord.x() * TILE_HORIZONTAL_SPACING, coord.y() * TILE_VERTICAL_SPACING);
        Vector2f columnOffset = new Vector2f(0, (coord.x() % 2 == 0) ? 0 : TILE_RADIUS * HEIGHT);
        return toCenter.add(base).add(columnOffset);
    }

    /**
     * Renders the tile at the given camera position.
     * <p>
     * Note for future self: it may be necessary to get the difference between the camera position and the tile position
     * in a way that does not cause floating point errors, e.g. by gettng Coordinate difference first before converting
     * to Vector2f.
     *
     * @param re rendering environment
     */
    public void render(RenderingEnvironment re) {
        Vector2f position = chunk.pos().add(indexPosition());
        Vector2f screenPosition = position.sub(re.camera.position());
        render(re, screenPosition, 1);
        if (re.showDebugInfo) {
            re.textRenderer.alignCenterHorizontal();
            re.textRenderer.alignCenterVertical();
            re.textRenderer.render(screenPosition.x(), screenPosition.y(), coord.x() + ", " + coord.y(), 0, re.font, 0.35f * TILE_RADIUS, rgb(255, 255, 255));
            re.textRenderer.alignLeft();
            re.textRenderer.alignTop();
        }
    }

    /**
     * Renders the tile at the given screen position.
     * <br><br>
     * Most of the time, you should use {@link #render(RenderingEnvironment)} instead.
     *
     * @param re             rendering environment
     * @param screenPosition the screen position to render the tile at
     * @param scale          the scale of the tile // TODO: not implemented
     */
    public void render(RenderingEnvironment re, Vector2f screenPosition, float scale) {
        render(re, screenPosition, scale, 0);
    }

    /**
     * Renders the tile at the given screen position with a rotation.
     * <br><br>
     * Most of the time, you should use {@link #render(RenderingEnvironment)} instead.
     *
     * @param re             rendering environment
     * @param screenPosition the screen position to render the tile at
     * @param scale          the scale of the tile // TODO: not implemented
     */
    public void render(RenderingEnvironment re, Vector2f screenPosition, float scale, float radians) {
        DefaultFrameBuffer.instance().render(() -> {
            re.defaultShaderProgram.set("color", toRangedVector(color)).set("transform", new Matrix4f(screenPosition.x(), screenPosition.y(), TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f, TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f, re.glContext).rotate(radians, new Vector3f(0, 0, 1))).use(new DrawFunction().vao(HexagonVao.instance()).glContext(re.glContext));
            for (WorldItem item : items) {
                re.textureRenderer.render(re.imageMap.get(item.item().image()), screenPosition.x() - ITEM_SIZE * 0.5f, screenPosition.y() - ITEM_SIZE * 0.5f, ITEM_SIZE, ITEM_SIZE);
            }
        });
    }

    public void addItem(WorldItem item) {
        items.add(item);
        item.tile(this);
    }

    public void removeItem(WorldItem item) {
        items.remove(item);
        item.tile(null);
    }

    public void buryItem(WorldItem item) {
        buried = item;
        item.tile(this);
        item.buried(true);
    }

    @Override
    public String toString() {
        return "Tile{" + "x=" + coord.x() + ", " + "y=" + coord.y() + '}';
    }

    public List<WorldItem> items() {
        return items;
    }

    public Chunk chunk() {
        return chunk;
    }

    public Zone zone() {
        return chunk.zone();
    }

    public Tile ul(World world) {
        return world.getTile(coord.ul());
    }

    public Tile um(World world) {
        return world.getTile(coord.um());
    }

    public Tile ur(World world) {
        return world.getTile(coord.ur());
    }

    public Tile dl(World world) {
        return world.getTile(coord.dl());
    }

    public Tile dm(World world) {
        return world.getTile(coord.dm());
    }

    public Tile dr(World world) {
        return world.getTile(coord.dr());
    }

    public TileCoordinate coord() {
        return coord;
    }

    public Vector2f getScreenPosition(RenderingEnvironment re) {
        return chunk.pos().add(indexPosition()).sub(re.camera.position());
    }

    @Override
    public UIContent tooltip(TooltipDeterminer determiner) {
        return determiner.visit(this);
    }

}
