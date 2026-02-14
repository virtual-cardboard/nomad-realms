import java.util.Random;
public class SeedCheck {
    public static void main(String[] args) {
        Random rng = new Random(123); // Simplified seed for illustration, actual seed in zone is derived from world seed
        System.out.println("numPoints: " + Math.round(rng.nextFloat() * 5));
    }
}
