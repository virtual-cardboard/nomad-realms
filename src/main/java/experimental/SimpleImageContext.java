package experimental;

import engine.context.GameContext;
import engine.context.input.event.*;
import engine.visuals.lwjgl.render.Texture;
import nomadrealms.render.RenderingEnvironment;

import static engine.common.colour.Colour.rgb;

public class SimpleImageContext extends GameContext {

    private RenderingEnvironment re;

    @Override
    public void init() {
        re = new RenderingEnvironment(glContext(), config());
    }

    @Override
    public void update() {
    }

    @Override
    public void render(float alpha) {
        background(rgb(100, 100, 100));
        Texture nomadTexture = re.imageMap.get("nomad");
        if (nomadTexture != null) {
            re.textureRenderer.render(nomadTexture, 100, 100, 200, 200);
        }
    }

    @Override
    public void cleanUp() {
    }

    @Override
    public void input(KeyPressedInputEvent event) {
    }

    @Override
    public void input(KeyReleasedInputEvent event) {
    }

    @Override
    public void input(MouseScrolledInputEvent event) {
    }

    @Override
    public void input(MouseMovedInputEvent event) {
    }

    @Override
    public void input(MousePressedInputEvent event) {
    }

    @Override
    public void input(MouseReleasedInputEvent event) {
    }
}
