package nomadrealms.context.game.world.map.generation;

import static nomadrealms.context.game.card.GameCard.DASH;
import static nomadrealms.context.game.card.GameCard.MEANDER;
import static nomadrealms.context.game.card.GameCard.MOVE;

import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;

public class TutorialMapInitialization implements MapInitialization {

	@Override
	public void initialize(World world) {
		TileCoordinate spawnCoord = new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0),
				8, 7);
		world.nomad = new Nomad("Donny", world.getTile(spawnCoord));

		world.nomad.deckCollection().deck1().addCard(new WorldCard(world.nomad.deckCollection().deck1(), MOVE));
		world.nomad.deckCollection().deck1().addCard(new WorldCard(world.nomad.deckCollection().deck1(), MOVE));
		world.nomad.deckCollection().deck2().addCard(new WorldCard(world.nomad.deckCollection().deck2(), DASH));
		world.nomad.deckCollection().deck2().addCard(new WorldCard(world.nomad.deckCollection().deck2(), MEANDER));

		world.addActor(world.nomad, true);
	}

}
