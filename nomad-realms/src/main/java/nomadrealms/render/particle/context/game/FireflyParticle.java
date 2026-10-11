package nomadrealms.render.particle.context.game;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintBox;
import java.util.Random;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;

/**
 * A firefly particle that is a single yellowish-white pixel/point flying around in an
 * organic, unpredictable pattern before flickering out and disappearing.
 */
public class FireflyParticle extends Particle {

	private static final Random random = new Random();

	private final Vector2f startWorldPos;
	private final long startTime;
	private final float frequencyX;
	private final float frequencyY;
	private final float phaseX;
	private final float phaseY;
	private final float driftX;
	private final float driftY;

	/**
	 * Spawns a Firefly particle near a given tile.
	 *
	 * @param tile The tile where the particle originates.
	 */
	public FireflyParticle(Tile tile) {
		this(tile.pos().vector().add(
				(random.nextFloat() - 0.5f) * 40.0f,
				(random.nextFloat() - 0.5f) * 40.0f
		));
	}

	/**
	 * Spawns a Firefly particle at a given world position.
	 *
	 * @param worldPos The initial world position.
	 */
	public FireflyParticle(Vector2f worldPos) {
		super(1000 + random.nextInt(4000), // Lifetime between 1 and 5 seconds
				new ConstraintBox(absolute(worldPos.x()), absolute(worldPos.y()), absolute(2), absolute(2)),
				absolute(0));
		this.startWorldPos = new Vector2f(worldPos);
		this.startTime = System.currentTimeMillis();

		this.frequencyX = 1.5f + random.nextFloat() * 2.5f;
		this.frequencyY = 1.5f + random.nextFloat() * 2.5f;
		this.phaseX = random.nextFloat() * (float) (Math.PI * 2);
		this.phaseY = random.nextFloat() * (float) (Math.PI * 2);

		this.driftX = (random.nextFloat() - 0.5f) * 15.0f;
		this.driftY = (random.nextFloat() - 0.5f) * 15.0f;
	}

	@Override
	public void render(RenderingEnvironment re) {
		long now = System.currentTimeMillis();
		float elapsedSec = (now - startTime) / 1000.0f;
		float progress = elapsedSec / (lifetime() / 1000.0f);

		if (progress < 0 || progress > 1.0f) {
			return;
		}

		// Organic flight path using layered sine/cosine waves and slow drift
		float offsetX = (float) (Math.sin(elapsedSec * frequencyX + phaseX) * 20.0f
				+ Math.cos(elapsedSec * frequencyY * 1.7f + phaseY) * 10.0f) + driftX * elapsedSec;
		float offsetY = (float) (Math.cos(elapsedSec * frequencyY + phaseY) * 20.0f
				+ Math.sin(elapsedSec * frequencyX * 1.3f + phaseX) * 10.0f) + driftY * elapsedSec;

		Vector2f currentWorldPos = startWorldPos.add(offsetX, offsetY);

		// Calculate screen position
		Vector2f screenPos = currentWorldPos.sub(re.is.camera.position().vector()).scale(re.is.camera.zoom().get());

		// Particle bounding box update for culling check
		box(new ConstraintBox(absolute(screenPos.x()), absolute(screenPos.y()), absolute(2), absolute(2)));

		// Brightness flickering calculation
		float flicker = 0.6f + 0.4f * (float) (Math.sin(elapsedSec * 18.0 + phaseX) * Math.cos(elapsedSec * 27.0 + phaseY));

		// Fade out in the last 20% of lifetime
		float alpha = 1.0f;
		if (progress > 0.8f) {
			alpha = (1.0f - progress) / 0.2f;
		} else if (progress < 0.1f) {
			// Fade in during first 10%
			alpha = progress / 0.1f;
		}

		float intensity = Math.max(0.0f, Math.min(1.0f, flicker * alpha));
		if (intensity <= 0.01f) {
			return;
		}

		// Yellowish-white particle color, with high intensity
		int r = (int) (255 * intensity);
		int g = (int) (250 * intensity);
		int b = (int) (180 * intensity);
		int a = (int) (255 * alpha);

		int color = rgba(r, g, b, a);

		// Render a small pixel point on screen
		float pixelSize = Math.max(2.0f, 2.0f * re.is.camera.zoom().get());
		re.rectangleRenderer.render(screenPos.x(), screenPos.y(), pixelSize, pixelSize, 0, color);
	}

	@Override
	public Particle clone() {
		return new FireflyParticle(startWorldPos);
	}

}
