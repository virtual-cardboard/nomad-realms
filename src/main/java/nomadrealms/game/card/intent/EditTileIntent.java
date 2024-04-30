package nomadrealms.game.card.intent;

import nomadrealms.game.actor.CardPlayer;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.game.world.map.tile.factory.TileType;

import static nomadrealms.game.world.map.tile.factory.TileFactory.createTile;

public class EditTileIntent implements Intent {

    private final CardPlayer source;
    private final Tile tile;
    private final TileType tileType;

    public EditTileIntent(CardPlayer source, Tile tile, TileType tileType) {
        this.source = source;
        this.tile = tile;
        this.tileType = tileType;
    }

    @Override
    public void resolve(World world) {
        Tile newTile = createTile(tileType, tile.y(), tile.x());
        world.setTile(newTile);
    }

}
