package nomadrealms.game.actor.structure;

import common.math.Vector2f;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.event.ProcChain;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

import java.util.List;

import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

public class Structure implements Actor {

    private Tile tile;
    private Inventory inventory;

    private final String name;
    private final String image;
    private final int constructionTime;
    private final int maxHealth;
    private int health;

    public Structure(String name, String image, int constructionTime, int health) {
        this.name = name;
        this.image = image;
        this.constructionTime = constructionTime;
        this.maxHealth = health;
        this.health = health;
    }

    @Override
    public void render(RenderingEnvironment re) {
        float scale = 0.6f * TILE_RADIUS;
        DefaultFrameBuffer.instance().render(
                () -> {
                    Vector2f screenPosition = tile().getScreenPosition(re);
                    re.textureRenderer.render(
                            re.imageMap.get(image),
                            screenPosition.x() - 0.5f * scale,
                            screenPosition.y() - 0.7f * scale,
                            scale, scale
                    );
                }
        );
    }

    @Override
    public int health() {
        return health;
    }

    @Override
    public void health(int health) {
        this.health = health;
    }

    @Override
    public Inventory inventory() {
        return inventory;
    }

    @Override
    public Tile tile() {
        return tile;
    }

    @Override
    public void tile(Tile tile) {
        this.tile = tile;
    }

    public Intent modify(World world, Intent intent) {
        return intent;
    }

    public List<ProcChain> trigger(World world, Intent intent) {
        return List.of();
    }

}
