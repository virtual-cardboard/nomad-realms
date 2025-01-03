package nomadrealms.render.ui;

import static common.colour.Colour.rgb;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

import java.util.List;
import java.util.function.Consumer;

import context.input.Mouse;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import nomadrealms.game.GameState;
import nomadrealms.game.actor.HasTooltip;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ContainerContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.tooltip.TooltipDeterminer;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Tooltip implements UI {

	private final TooltipDeterminer determiner;

	private boolean visible = false;

	private final GameState state;
	private final Mouse mouse;

	private HasTooltip target;

	private ContainerContent containerContent;

	public Tooltip(RenderingEnvironment re, ScreenContainerContent screenContainerContent,
	               GameState state, Mouse mouse,
	               List<Consumer<MousePressedInputEvent>> onClick,
	               List<Consumer<MouseMovedInputEvent>> onDrag,
	               List<Consumer<MouseReleasedInputEvent>> onDrop) {
		determiner = new TooltipDeterminer(this, re);
		this.state = state;
		this.mouse = mouse;
		onClick.add(event -> {
			if (event.button() == GLFW_MOUSE_BUTTON_RIGHT) {
				target = state.getMouseHexagon(mouse, re.camera);
				visible = !visible;
			}
		});
		onDrag.add(event -> {
			if (visible) {
				HasTooltip newTarget = state.getMouseHexagon(mouse, re.camera);
				if (newTarget != target) {
					visible = false;
					target = null;
				}
			}
		});
		containerContent = new ContainerContent(
				screenContainerContent,
				new ConstraintBox(
						mouse::x,
						mouse::y,
						() -> 400,
						() -> 100
				))
				.fill(rgb(255, 0, 0));
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (visible) {
			DefaultFrameBuffer.instance().render(() -> {
				containerContent.render(re);
			});
			if (target != null) {
				target.tooltip(determiner).render(re);
			}
		}
	}

	public void target(HasTooltip target) {
		this.target = target;
	}

	public ContainerContent uiContainer() {
		return containerContent;
	}

}
