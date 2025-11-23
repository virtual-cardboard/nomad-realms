package nomadrealms.render.ui;

import static engine.common.colour.Colour.rgb;
import java.nio.ByteBuffer;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.rendering.texture.Image;
import nomadrealms.render.RenderingEnvironment;

public class RulerUI {

    private boolean showRuler = false;
    private final Texture whiteTexture;
    private final GLContext glContext;

    public RulerUI(GLContext glContext) {
        this.glContext = glContext;
        ByteBuffer whitePixel = ByteBuffer.allocateDirect(4);
        whitePixel.put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255);
        whitePixel.flip();
        Image whiteImage = new Image().data(whitePixel).width(1).height(1);
        whiteTexture = new Texture().image(whiteImage).load();
    }

    public void toggle() {
        showRuler = !showRuler;
    }

    public void render(RenderingEnvironment re) {
        if (showRuler) {
            re.textureRenderer.setDiffuse(rgb(255, 255, 255));
            // Top ruler
            re.textureRenderer.render(whiteTexture, 0, 0, glContext.width(), 2);
            for (int i = 100; i < glContext.width(); i += 100) {
                re.textureRenderer.render(whiteTexture, i, 0, 2, 10);
            }
            // Left ruler
            re.textureRenderer.render(whiteTexture, 0, 0, 2, glContext.height());
            for (int i = 100; i < glContext.height(); i += 100) {
                re.textureRenderer.render(whiteTexture, 0, i, 10, 2);
            }
            re.textureRenderer.resetDiffuse();
        }
    }

}
