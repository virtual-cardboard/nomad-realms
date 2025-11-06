package nomadrealms.render.ui.custom.particle;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.List;
import java.util.function.Consumer;

import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.GLContext;
import nomadrealms.game.GameState;
import nomadrealms.particles.ParticleEngine;
import nomadrealms.particles.TestParticle;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;

public class ParticleDebugTab {

	private final ButtonUIContent spawnParticleButton;
	private final ParticleEngine particleEngine;

	public ParticleDebugTab(GameState gameState, GLContext glContext, ScreenContainerContent screen,
							List<Consumer<MousePressedInputEvent>> onClick,
							List<Consumer<MouseMovedInputEvent>> onDrag,
							List<Consumer<MouseReleasedInputEvent>> onDrop) {
		this.particleEngine = gameState.particleEngine;
		spawnParticleButton = new ButtonUIContent(screen, "Spawn Particle",
				new ConstraintBox(absolute(100), absolute(100), absolute(200), absolute(50)),
				() -> {
					particleEngine.spawnWorldParticle(new TestParticle(1, 400, 300, 10, 10, 0, 10));
				},
				onClick, onDrag, onDrop);
	}

	public void render(RenderingEnvironment re) {
		spawnParticleButton.render(re);
	}

}
