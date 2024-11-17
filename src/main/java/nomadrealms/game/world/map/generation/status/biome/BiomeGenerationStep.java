package nomadrealms.game.world.map.generation.status.biome;

import static nomadrealms.game.world.map.generation.status.GenerationStepStatus.BIOMES;

import nomadrealms.game.world.map.area.Zone;
import nomadrealms.game.world.map.generation.status.GenerationStep;
import nomadrealms.game.world.map.generation.status.GenerationStepStatus;
import nomadrealms.game.world.map.generation.status.biome.generator.TemperatureGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.HumidityGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.ContinentalnessGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.ErosionGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.WeirdnessGenerator;
import nomadrealms.game.world.map.generation.status.biome.generator.DepthGenerator;

public class BiomeGenerationStep extends GenerationStep {

    public BiomeGenerationStep(Zone zone, long worldSeed) {
        super(zone, worldSeed);
    }

    @Override
    public GenerationStepStatus status() {
        return BIOMES;
    }

    @Override
    public void generate(Zone[][] surrounding) {
        TemperatureGenerator temperatureGenerator = new TemperatureGenerator(worldSeed);
        HumidityGenerator humidityGenerator = new HumidityGenerator(worldSeed);
        ContinentalnessGenerator continentalnessGenerator = new ContinentalnessGenerator(worldSeed);
        ErosionGenerator erosionGenerator = new ErosionGenerator(worldSeed);
        WeirdnessGenerator weirdnessGenerator = new WeirdnessGenerator(worldSeed);
        DepthGenerator depthGenerator = new DepthGenerator(worldSeed);

        for (int x = 0; x < zone.coord().x(); x++) {
            for (int y = 0; y < zone.coord().y(); y++) {
                float temperature = temperatureGenerator.generateTemperature(zone.coord().region().x() + x, zone.coord().region().y() + y);
                float humidity = humidityGenerator.generateHumidity(zone.coord().region().x() + x, zone.coord().region().y() + y);
                float continentalness = continentalnessGenerator.generateContinentalness(zone.coord().region().x() + x, zone.coord().region().y() + y);
                float erosion = erosionGenerator.generateErosion(zone.coord().region().x() + x, zone.coord().region().y() + y);
                float weirdness = weirdnessGenerator.generateWeirdness(zone.coord().region().x() + x, zone.coord().region().y() + y);
                float depth = depthGenerator.generateDepth(zone.coord().region().x() + x, zone.coord().region().y() + y);

                // Assign biomes based on the generated parameter values
                // This is a placeholder for the actual biome assignment logic
                // You need to implement the logic to assign biomes based on the generated parameter values
            }
        }
    }
}
