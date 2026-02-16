package nomadrealms.context.game.card.effect;

import static nomadrealms.context.game.world.map.tile.factory.TileFactory.createTile;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;

public class EditTileEffect extends Effect {

	private final Tile tile;
	private final TileType tileType;

	public EditTileEffect(CardPlayer source, Tile tile, TileType tileType) {
		super(source);
		this.tile = tile;
		this.tileType = tileType;
	}

	@Override
	public void resolve(World world) {
		Tile newTile = createTile(tileType, tile.chunk(), tile.coord());
		world.setTile(newTile);
	}

}
