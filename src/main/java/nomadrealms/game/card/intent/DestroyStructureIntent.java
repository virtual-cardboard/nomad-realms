package nomadrealms.game.card.intent;

import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

public class DestroyStructureIntent implements Intent {

    private final Tile tile;

    public DestroyStructureIntent(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void resolve(World world) {
        Structure structure = (Structure) tile.actor();
        if (structure != null) {
            world.removeActor(structure);
        }
    }
}
