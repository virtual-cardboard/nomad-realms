package nomadrealms.context.game.world.map.generation.overworld;

import static java.util.Arrays.asList;

import java.util.List;

import nomadrealms.context.game.world.map.area.Zone;
import nomadrealms.context.game.world.map.generation.overworld.biome.BiomeGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.points.PointsGenerationStep;
import nomadrealms.context.game.world.map.generation.overworld.structure.StructureGenerationStep;

public class GenerationProcess {

	private final BiomeGenerationStep biome;
	private final PointsGenerationStep points;
	private final StructureGenerationStep structure;


	/**
	 * No-arg constructor for serialization.
	 */
	protected GenerationProcess() {
		this.biome = null;
		this.points = null;
		this.structure = null;
	}

	public GenerationProcess(Zone zone, long seed) {
		biome = new BiomeGenerationStep(zone, seed);
		points = new PointsGenerationStep(zone, seed);
		structure = new StructureGenerationStep(zone, seed);
	}

	public List<GenerationStep> steps() {
		return asList(
				biome,
				points,
				structure
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
}
