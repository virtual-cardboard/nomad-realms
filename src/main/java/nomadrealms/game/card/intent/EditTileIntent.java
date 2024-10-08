package nomadrealms.game.card.intent;

import static nomadrealms.game.world.map.tile.factory.TileFactory.createTile;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.tile.factory.TileType;

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
        Tile newTile = createTile(tileType, tile.chunk(), tile.coord());
        world.setTile(newTile);
    }

}
