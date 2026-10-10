package engine.visuals.rendering.blur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GaussianBlurRendererTest {

	@Test
	public void testCalculateSampleRadius() {
		assertEquals(2, GaussianBlurRenderer.calculateSampleRadius(0.1f));
		assertEquals(4, GaussianBlurRenderer.calculateSampleRadius(2.5f));
		assertEquals(8, GaussianBlurRenderer.calculateSampleRadius(5.0f));
		assertEquals(12, GaussianBlurRenderer.calculateSampleRadius(8.0f));
		assertEquals(32, GaussianBlurRenderer.calculateSampleRadius(200.0f));
	}

	@Test
	public void testWeightsSumToOne() {
		float[] radii = { 1.0f, 5.0f, 8.0f, 15.0f, 50.0f };
		for (float radius : radii) {
			float[] weights = GaussianBlurRenderer.calculateWeights(radius);
			int sampleRadius = GaussianBlurRenderer.calculateSampleRadius(radius);

			assertEquals(33, weights.length);

			float sum = weights[0];
			for (int i = 1; i <= sampleRadius; i++) {
				sum += 2.0f * weights[i];
			}

			assertEquals(1.0f, sum, 1e-5f, "Weights sum should be 1.0 for radius " + radius);
		}
	}

	@Test
	public void testWeightsMonotonicDecreaseAndZeroPadding() {
		float radius = 5.0f;
		float[] weights = GaussianBlurRenderer.calculateWeights(radius);
		int sampleRadius = GaussianBlurRenderer.calculateSampleRadius(radius);

		for (int i = 1; i <= sampleRadius; i++) {
			assertTrue(weights[i] < weights[i - 1], "Weight at offset " + i + " should be less than offset " + (i - 1));
			assertTrue(weights[i] > 0, "Weight at offset " + i + " should be positive");
		}

		for (int i = sampleRadius + 1; i < weights.length; i++) {
			assertEquals(0.0f, weights[i], 1e-7f, "Weight beyond sample radius should be 0");
		}
	}

}
