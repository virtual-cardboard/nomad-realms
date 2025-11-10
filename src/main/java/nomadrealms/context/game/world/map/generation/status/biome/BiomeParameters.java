package nomadrealms.context.game.world.map.generation.status.biome;

/**
 * Represents the parameters used to determine a biome.
 * <br><br>
 * The parameters are:
 * <ul>
 *     <li>Temperature</li>
 *     <li>Humidity</li>
 *     <li>Continentalness</li>
 *     <li>Erosion</li>
 *     <li>Weirdness</li>
 *     <li>Depth</li>
 *
 * @author Lunkle
 */
public class BiomeParameters {

    private final float temperature;
    private final float humidity;
    private final float continentalness;
    private final float erosion;
    private final float weirdness;
    private final float depth;

    public BiomeParameters(float temperature, float humidity, float continentalness, float erosion, float weirdness, float depth) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.weirdness = weirdness;
        this.depth = depth;
    }

    public float temperature() {
        return temperature;
    }

    public float humidity() {
        return humidity;
    }

    public float continentalness() {
        return continentalness;
    }

    public float erosion() {
        return erosion;
    }

    public float weirdness() {
        return weirdness;
    }

    public float depth() {
        return depth;
    }

    @Override
    public String toString() {
        return "BiomeParameters{" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", continentalness=" + continentalness +
                ", erosion=" + erosion +
                ", weirdness=" + weirdness +
                ", depth=" + depth +
                '}';
    }

}
