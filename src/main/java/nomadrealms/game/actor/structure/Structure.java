package nomadrealms.game.actor.structure;

import nomadrealms.game.actor.Actor;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

import static nomadrealms.game.world.map.tile.Tile.SCALE;

public class Structure implements Actor {

    private Tile tile;
    private Inventory inventory;

    private String name;
    private String image;
    private int constructionTime = 0;
    private int health;

    public Structure(String name, String image, int constructionTime, int health) {
        this.name = name;
        this.image = image;
        this.constructionTime = constructionTime;
        this.health = health;
    }

    @Override
    public void render(RenderingEnvironment re) {
        float scale = 0.6f * SCALE;
        DefaultFrameBuffer.instance().render(
                () -> {
                    re.textureRenderer.render(
                            re.imageMap.get(image),
                            tile().getScreenPosition().x() - 0.5f * scale,
                            tile().getScreenPosition().y() - 0.7f * scale,
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

}
