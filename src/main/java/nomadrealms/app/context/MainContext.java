package nomadrealms.app.context;

import static common.NengenFileUtil.loadFont;
import static common.NengenFileUtil.loadImage;
import static common.NengenFileUtil.readFileAsString;

import context.GameContext;
import context.input.event.KeyPressedInputEvent;
import context.input.event.KeyReleasedInputEvent;
import context.input.event.MouseScrolledInputEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.world.actor.Nomad;
import nomadrealms.world.map.Map;

public class MainContext extends GameContext {

	RenderingEnvironment re;
	Map map;
	Nomad nomad = new Nomad(0, 0);

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext());
		map = new Map();
	}

	@Override
	public void update() {
	}

	@Override
	public void render(float alpha) {
		map.render(re);
		nomad.render(re);
	}

	@Override
	public void terminate() {
		System.out.println("second context terminate");
	}

	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		System.out.println("second context key pressed: " + key);
	}

	public void input(KeyReleasedInputEvent event) {
		int key = event.code();
		System.out.println("second context key released: " + key);
	}

	public void input(MouseScrolledInputEvent event) {
		float amount = event.yAmount();
		System.out.println("second context mouse scrolled: " + amount);
	}

}
