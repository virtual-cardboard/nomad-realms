package engine.common.math;

public final class Gaussian {

    private Gaussian() {
    }

    public static float[] generate1DGaussianKernel(int radius, float sigma) {
        float[] weights = new float[radius + 1];
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            float x = i;
            weights[i] = (float) (1 / (Math.sqrt(2 * Math.PI) * sigma) * Math.exp(-x * x / (2 * sigma * sigma)));
            if (i == 0) {
                sum += weights[i];
            } else {
                sum += 2 * weights[i];
            }
        }

        // Normalize the weights
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sum;
        }
        return weights;
    }
}
