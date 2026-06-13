package experimental.spritesheet;

import engine.context.GameContext;
import engine.context.input.event.*;
import engine.visuals.lwjgl.render.CroppedTexture;
import engine.visuals.rendering.texture.SpriteSheet;
import nomadrealms.render.RenderingEnvironment;

import static engine.common.colour.Colour.rgb;

public class SpriteSheetContext extends GameContext {

	private RenderingEnvironment re;
	private SpriteSheet spriteSheet;
	private String[] frames = {
			"small_gold_coin_0",
			"small_gold_coin_1",
			"small_gold_coin_2",
			"small_gold_coin_3",
			"small_gold_coin_4",
			"small_gold_coin_5"
	};
	private int currentFrame = 0;
	private int timer = 0;
	private final int FRAME_DELAY = 5; // ticks per frame

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		spriteSheet = SpriteSheet.load("/images/particles/particles.png", "/images/particles/particles.txt");
	}

	@Override
	public void update() {
		timer++;
		if (timer >= FRAME_DELAY) {
			currentFrame = (currentFrame + 1) % frames.length;
			timer = 0;
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		CroppedTexture ct = spriteSheet.get(frames[currentFrame]);
		if (ct != null) {
			re.textureRenderer.render(ct, 300, 200, 200, 200);
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
