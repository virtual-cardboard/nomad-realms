package nomadrealms.game.card.intent;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.actor.structure.factory.StructureType;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

import static nomadrealms.game.actor.structure.factory.StructureFactory.createStructure;

public class CreateStructureIntent implements Intent {

    private final CardPlayer source;
    private final Tile tile;
    private final StructureType structureType;

    public CreateStructureIntent(CardPlayer source, Tile tile, StructureType structureType) {
        this.source = source;
        this.tile = tile;
        this.structureType = structureType;
    }

    @Override
    public void resolve(World world) {
        Structure newStructure = createStructure(structureType);
        newStructure.tile(tile);
        world.addActor(newStructure);
    }

}
