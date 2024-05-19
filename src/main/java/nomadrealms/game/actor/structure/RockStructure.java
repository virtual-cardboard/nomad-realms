package nomadrealms.game.actor.structure;

import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

import static nomadrealms.game.world.map.tile.Tile.SCALE;

public class RockStructure extends Structure {

    public RockStructure() {
        super("rock", "rock_1", 1, 50);
    }

}
