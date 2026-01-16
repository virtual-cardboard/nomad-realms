package nomadrealms.context.game;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import engine.common.math.UnitQuaternion;
import engine.common.math.Vector2f;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.ai.CardPlayerAI;
import nomadrealms.context.game.actor.status.Status;
import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.card.Card;
import nomadrealms.context.game.card.CardMemory;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.card.action.scheduler.CardPlayerActionScheduler;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.ItemTag;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.GameMap;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Region;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.area.coordinate.Coordinate;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.GenerationProcess;
import nomadrealms.context.game.world.map.generation.overworld.GenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeParameters;
import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGenerator;
import nomadrealms.context.game.world.map.generation.overworld.biome.noise.BiomeNoiseGeneratorCluster;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeCategory;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.BiomeVariantType;
import nomadrealms.context.game.world.map.generation.overworld.biome.nomenclature.ContinentType;
import nomadrealms.context.game.world.map.generation.overworld.points.point.PointOfInterest;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationConfig;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.context.game.world.weather.Weather;
import nomadrealms.context.game.zone.CardStack;
import nomadrealms.context.game.zone.CardStackEntry;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.DeckCollection;
import nomadrealms.event.Event;
import nomadrealms.event.game.cardzone.CardZoneEventChannel;
import nomadrealms.event.game.cardzone.CardZoneListener;
import nomadrealms.math.generation.map.LayeredNoise;
import nomadrealms.math.generation.map.NoiseOctave;
import nomadrealms.math.generation.map.OpenSimplexNoise;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.custom.card.CardPhysics;
import nomadrealms.render.ui.custom.card.CardTransform;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

/**
 * Serializes and deserializes the {@link GameState}. For now, this uses Kryo which serializes the entire object. In the
 * future, a more robust and efficient solution may be needed to handle versioning and backwards compatibility as well
 * as performance/bandwidth optimizations.
 *
 * @author Lunkle
 */
public class GameStateSerializer {

	private final Kryo kryo = new Kryo();

	public GameStateSerializer() {
		kryo.setReferences(true);
		kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
		registerClasses();
	}

	private void registerClasses() {
		kryo.register(GameState.class);
		kryo.register(InputEventFrame.class);
		kryo.register(ParticlePool.class);
		kryo.register(Particle[].class);
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
		kryo.register(TreeMap.class);
		kryo.register(BiomeVariantType.class);
		kryo.register(BiomeVariantType[].class);
		kryo.register(BiomeVariantType[][].class);
		kryo.register(BiomeCategory.class);
		kryo.register(BiomeCategory[].class);
		kryo.register(BiomeCategory[][].class);
		kryo.register(ContinentType.class);
		kryo.register(ContinentType[].class);
		kryo.register(ContinentType[][].class);
		kryo.register(BiomeParameters.class);
		kryo.register(BiomeParameters[].class);
		kryo.register(BiomeParameters[][].class);
		kryo.register(TileType.class);
		kryo.register(TileType[].class);
		kryo.register(TileType[][].class);
		kryo.register(Weather.class);
		kryo.register(GenerationProcess.class);
		kryo.register(BiomeNoiseGenerator.class);
		kryo.register(BiomeNoiseGeneratorCluster.class);
		kryo.register(PointOfInterest.class);
		kryo.register(LayeredNoise.class);
		kryo.register(NoiseOctave[].class);
		kryo.register(NoiseOctave.class);
		kryo.register(OpenSimplexNoise.class);
		kryo.register(CardPlayerActionScheduler.class);
		kryo.register(CardStack.class);
		kryo.register(CardStackEntry.class);
		kryo.register(Status.class);
		kryo.register(StatusEffect.class);
		kryo.register(ArrayDeque.class);
		kryo.register(CardPhysics.class);
		kryo.register(CardTransform.class);
		kryo.register(UnitQuaternion.class);
		kryo.register(ConstraintPair.class);
		kryo.register(ProcChain.class);
		kryo.register(DropItemEvent.class);
		kryo.register(CardZoneEventChannel.class);
		kryo.register(CardZoneListener.class);
		kryo.register(Structure[].class);
		kryo.register(Structure[][].class);
		kryo.register(short[].class);
		kryo.register(long[].class);
		kryo.register(int[].class);

		Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("nomadrealms"));
		List<Class<?>> superclasses = Arrays.asList(
				Event.class,
				CardExpression.class,
				Effect.class,
				Actor.class,
				CardPlayerAI.class,
				Tile.class,
				Coordinate.class,
				MapGenerationStrategy.class,
				GenerationStep.class,
				StructureGenerationConfig.class,
				Card.class,
				Action.class,
				Constraint.class);
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
