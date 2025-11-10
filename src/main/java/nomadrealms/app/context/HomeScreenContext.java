package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.timed.TimedConstraint.time;
import static java.lang.Math.PI;
import static java.lang.Math.random;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.HexagonParticle;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.ui.custom.home.HomeInterface;

public class HomeScreenContext extends GameContext {

	private RenderingEnvironment re;

	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	private HomeInterface homeInterface;
	private ParticlePool particlePool;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config());
		homeInterface = new HomeInterface(re, glContext(), inputCallbackRegistry);
		homeInterface.initStartGameButton(() -> {
			transition(new MainContext());
		});
		particlePool = new ParticlePool(glContext().screen);
	}

	@Override
	public void update() {
		if (particlePool != null) {
			float size = 20 + (float) (random() * 10);
			float speed = 20 + (float) (random() * 5);
			long lifetime = 5000 + (long) (random() * 5000);
			float startX = (float) (random() * glContext().screen.w().get());
			ConstraintBox box = new ConstraintBox(
					absolute(startX), glContext().screen.h(),
					absolute(size), absolute(size));
			float totalRotations = 1 + (float) (random() * 3);
			Constraint rotation = time().multiply(totalRotations * 2 * PI);
			int color = rgb(100 + (int) (random() * 155), 100 + (int) (random() * 155), 100 + (int) (random() * 155));
			particlePool.addParticle(new HexagonParticle(box, glContext(), rotation, color));
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(100, 100, 100));
		homeInterface.render(re);
		particlePool.render(re);
	}

	@Override
	public void terminate() {
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
		inputCallbackRegistry.triggerOnDrag(event);
	}

	@Override
	public void input(MousePressedInputEvent event) {
		inputCallbackRegistry.triggerOnPress(event);
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
	}

}