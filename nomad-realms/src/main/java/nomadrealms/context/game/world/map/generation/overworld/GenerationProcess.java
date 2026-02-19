package nomadrealms.context.game.world.map.generation.overworld;

import static java.util.Arrays.asList;

import java.util.List;

import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.MapGenerationStrategy;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.PointsGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.villager.VillagerGenerationStep;

public class GenerationProcess {

	private final BiomeGenerationStep biome;
	private final PointsGenerationStep points;
	private final StructureGenerationStep structure;
	private final VillagerGenerationStep villager;


	/**
	 * No-arg constructor for serialization.
	 */
	protected GenerationProcess() {
		this.biome = null;
		this.points = null;
		this.structure = null;
		this.villager = null;
	}

	public GenerationProcess(Zone zone, MapGenerationStrategy strategy) {
		biome = new BiomeGenerationStep(zone, strategy);
		points = new PointsGenerationStep(zone, strategy);
		structure = new StructureGenerationStep(zone, strategy);
		villager = new VillagerGenerationStep(zone, strategy);
	}

	public List<GenerationStep> steps() {
		return asList(
				biome,
				points,
				structure,
				villager
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

	public VillagerGenerationStep villager() {
		return villager;
	}
}
