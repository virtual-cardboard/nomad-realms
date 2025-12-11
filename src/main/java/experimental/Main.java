package experimental;

import engine.nengen.Nengen;

public class Main {

    public static void main(String[] args) {
        SimpleImageContext context = new SimpleImageContext();

        Nengen nengen = new Nengen();
        nengen.configure()
                .setWindowDim(800, 600)
                .setWindowName("Experimental Image Render")
                .setFrameRate(60)
                .setTickRate(10);
        nengen.startNengen(context);
    }

}
