package nomadrealms.game;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import common.math.Vector2f;
import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.ai.CardPlayerAI;
import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.card.Card;
import nomadrealms.game.card.CardMemory;
import nomadrealms.game.card.action.Action;
import nomadrealms.game.card.action.scheduler.CardPlayerActionScheduler;
import nomadrealms.game.event.InputEventFrame;
import nomadrealms.game.item.Inventory;
import nomadrealms.game.item.Item;
import nomadrealms.game.item.ItemTag;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.GameMap;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Chunk;
import nomadrealms.game.world.map.area.Region;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.area.coordinate.Coordinate;
import nomadrealms.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.game.world.map.generation.status.GenerationStep;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.game.world.map.generation.status.biome.noise.BiomeNoiseGenerator;
import nomadrealms.game.world.map.generation.status.biome.noise.BiomeNoiseGeneratorCluster;
import nomadrealms.game.world.map.generation.status.biome.nomenclature.BiomeVariantType;
import nomadrealms.game.world.map.generation.status.points.point.PointOfInterest;
import nomadrealms.game.zone.Deck;
import nomadrealms.game.zone.DeckCollection;
import nomadrealms.game.zone.CardQueue;
import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class GameStateSerializer {

	private final Kryo kryo = new Kryo();

	public GameStateSerializer() {
		kryo.setReferences(true);
		registerClasses();
	}

	private void registerClasses() {
		kryo.register(GameState.class);
		kryo.register(InputEventFrame.class);
		kryo.register(World.class);
		kryo.register(Deck[].class);
		kryo.register(Deck.class);
		kryo.register(DeckCollection.class);
		kryo.register(CardMemory.class);
		kryo.register(Inventory.class);
		kryo.register(WorldItem.class);
		kryo.register(Item.class);
		kryo.register(ItemTag.class);
		kryo.register(Tile[][].class);
		kryo.register(Tile[].class);
		kryo.register(Tile.class);
		kryo.register(Chunk.class);
		kryo.register(Chunk[].class);
		kryo.register(Chunk[][].class);
		kryo.register(Zone.class);
		kryo.register(Zone[].class);
		kryo.register(Zone[][].class);
		kryo.register(Region.class);
		kryo.register(Region[].class);
		kryo.register(GameMap.class);
		kryo.register(Vector2f.class);
		kryo.register(ArrayList.class);
		kryo.register(LinkedList.class);
		kryo.register(HashMap.class);
		kryo.register(HashSet.class);
		kryo.register(BiomeVariantType.class);
		kryo.register(BiomeVariantType[].class);
		kryo.register(BiomeVariantType[][].class);
		kryo.register(GenerationStepStatus.class);
		kryo.register(BiomeNoiseGenerator.class);
		kryo.register(BiomeNoiseGeneratorCluster.class);
		kryo.register(PointOfInterest.class);
		kryo.register(LayeredNoise.class);
		kryo.register(NoiseOctave[].class);
		kryo.register(NoiseOctave.class);
		kryo.register(OpenSimplexNoise.class);
		kryo.register(short[].class);
		kryo.register(CardPlayerActionScheduler.class);
		kryo.register(CardQueue.class);

		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("nomadrealms"));
		List<Class<?>> superclasses = Arrays.asList(
				Actor.class,
				Structure.class,
				CardPlayerAI.class,
				Tile.class,
				Coordinate.class,
				MapGenerationStrategy.class,
				Card.class,
				Action.class,
				GenerationStep.class);
		for (Class<?> superclass : superclasses) {
			for (Class<?> clazz : reflections.getSubTypesOf(superclass)) {
				kryo.register(clazz);
			}
		}
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
