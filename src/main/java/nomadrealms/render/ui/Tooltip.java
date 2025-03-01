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
import nomadrealms.render.ui.content.DynamicGridLayoutContainerContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.render.ui.tooltip.TooltipDeterminer;
import visuals.constraint.box.ConstraintCoordinate;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class Tooltip implements UI {

	private final TooltipDeterminer determiner;

	private boolean visible = false;

	private final RenderingEnvironment re;
	private final GameState state;
	private final Mouse mouse;

	private HasTooltip target;

	private final ContainerContent containerContent;

	public Tooltip(RenderingEnvironment re, ScreenContainerContent screenContainerContent,
				   GameState state, Mouse mouse,
				   List<Consumer<MousePressedInputEvent>> onClick,
				   List<Consumer<MouseMovedInputEvent>> onDrag,
				   List<Consumer<MouseReleasedInputEvent>> onDrop) {
		determiner = new TooltipDeterminer(this, re);
		this.re = re;
		this.state = state;
		this.mouse = mouse;
		onClick.add(this::handleRightClick);
		onDrag.add(this::handleMouseOff);
		containerContent = new DynamicGridLayoutContainerContent(
				screenContainerContent,
				new ConstraintCoordinate(mouse::x, mouse::y),
				2)
				.fill(rgb(255, 0, 0));
	}

	private void handleRightClick(MousePressedInputEvent event) {
		if (event.button() == GLFW_MOUSE_BUTTON_RIGHT) {
			target = state.getMouseHexagon(mouse, re.camera);
			visible = !visible;
		}
	}

	private void handleMouseOff(MouseMovedInputEvent event) {
		if (visible) {
			HasTooltip newTarget = state.getMouseHexagon(mouse, re.camera);
			if (newTarget != target) {
				visible = false;
				target = null;
			}
		}
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (visible) {
			DefaultFrameBuffer.instance().render(() -> {
				containerContent.render(re);
			});
			if (target != null) {
				containerContent.clearChildren();
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
