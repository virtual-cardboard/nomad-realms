package nomadrealms.game;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.ai.FeralMonkeyAI;
import nomadrealms.game.actor.cardplayer.Farmer;
import nomadrealms.game.actor.cardplayer.FeralMonkey;
import nomadrealms.game.actor.cardplayer.Nomad;
import nomadrealms.game.actor.structure.ElectrostaticZapperStructure;
import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.card.CardMemory;
import nomadrealms.game.card.GameCard;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.item.Item;
import nomadrealms.game.item.ItemTag;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Region;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.game.world.map.tile.GrassTile;
import nomadrealms.game.zone.Deck;
import nomadrealms.game.zone.DeckCollection;

public class GameStateSerializer {

	private final Kryo kryo = new Kryo();

	public GameStateSerializer() {
		kryo.setReferences(true);
		registerClasses();
	}

	private void registerClasses() {
		kryo.register(ArrayList.class);
		kryo.register(LinkedList.class);
		kryo.register(GameState.class);
		kryo.register(InputEventFrame.class);
		kryo.register(World.class);
		kryo.register(Actor.class);
		kryo.register(Farmer.class);
		kryo.register(FeralMonkey.class);
		kryo.register(FeralMonkeyAI.class);
		kryo.register(Nomad.class);
		kryo.register(Structure.class);
		kryo.register(ElectrostaticZapperStructure.class);
		kryo.register(Deck[].class);
		kryo.register(Deck.class);
		kryo.register(WorldCard.class);
		kryo.register(GameCard.class);
		kryo.register(DeckCollection.class);
		kryo.register(CardMemory.class);
		kryo.register(Structure.class);
		kryo.register(Inventory.class);
		kryo.register(HashSet.class);
		kryo.register(WorldItem.class);
		kryo.register(Item.class);
		kryo.register(ItemTag.class);
		kryo.register(GrassTile.class);
		kryo.register(Tile[][].class);
		kryo.register(Tile[].class);
		kryo.register(Tile.class);
		kryo.register(TileCoordinate.class);
		kryo.register(Chunk.class);
		kryo.register(ChunkCoordinate.class);
		kryo.register(Zone.class);
		kryo.register(Zone[].class);
		kryo.register(ZoneCoordinate.class);
		kryo.register(Region.class);
		kryo.register(Region[].class);
		kryo.register(RegionCoordinate.class);

		kryo.setRegistrationRequired(false);
	}

	public void serialize(GameState gameState, String filePath) throws IOException {
		Output output = new Output(newOutputStream(Paths.get(filePath)));
		kryo.writeObject(output, gameState);
		output.close();
	}

	public GameState deserialize(String filePath) throws IOException, ClassNotFoundException {
		Input input = new Input(newInputStream(Paths.get(filePath)));
		GameState gameState = kryo.readObject(input, GameState.class);
		input.close();
		return gameState;
	}

}
