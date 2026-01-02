package nomadrealms.context.game.world.map.generation.overworld;

import static java.util.Arrays.asList;

import java.util.List;

import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.item.ItemGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.PointsGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationStep;

public class GenerationProcess {

	private final BiomeGenerationStep biome;
	private final PointsGenerationStep points;
	private final StructureGenerationStep structure;
	private final ItemGenerationStep item;


	/**
	 * No-arg constructor for serialization.
	 */
	protected GenerationProcess() {
		this.biome = null;
		this.points = null;
		this.structure = null;
		this.item = null;
	}

	public GenerationProcess(Zone zone, MapGenerationStrategy strategy) {
		biome = new BiomeGenerationStep(zone, strategy);
		points = new PointsGenerationStep(zone, strategy);
		structure = new StructureGenerationStep(zone, strategy);
		item = new ItemGenerationStep(zone, strategy);
	}

	public List<GenerationStep> steps() {
		return asList(
				biome,
				points,
				structure,
				item
		);
	}

	public BiomeGenerationStep biome() {
		return biome;
	}

	public PointsGenerationStep points() {
		return points;
	}

	public StructureGenerationStep structure() {
		return structure;
	}

	public ItemGenerationStep item() {
		return item;
	}

}
