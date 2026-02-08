package nomadrealms.render.ui.custom.console;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class Console implements UI {

	private final List<String> history = new ArrayList<>();
	private String currentInput = "";
	private boolean active = false;
	private final ConstraintBox screen;

	public Console(ConstraintBox screen) {
		this.screen = screen;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (!active) {
			return;
		}

		float consoleHeight = 300;
		float inputHeight = 40;
		ConstraintBox consoleBox = new ConstraintBox(
				absolute(0),
				screen.h().add(absolute(-consoleHeight)),
				screen.w(),
				absolute(consoleHeight)
		);

		// Render background
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(0, 0, 0, 150)))
				.set("transform", new Matrix4f(consoleBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		// Render input box background
		ConstraintBox inputBox = new ConstraintBox(
				absolute(0),
				screen.h().add(absolute(-inputHeight)),
				screen.w(),
				absolute(inputHeight)
		);
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(30, 30, 30, 200)))
				.set("transform", new Matrix4f(inputBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		// Render current input
		re.textRenderer.alignLeft().alignBottom();
		re.textRenderer.render(
				10, screen.h().get() - 5,
				"> " + currentInput,
				screen.w().get() - 20,
				re.font, 30, rgba(255, 255, 255, 255));

		// Render history
		re.textRenderer.alignLeft().alignBottom();
		float y = screen.h().get() - inputHeight - 5;
		for (int i = history.size() - 1; i >= 0; i--) {
			String line = history.get(i);
			re.textRenderer.render(
					10, y,
					line,
					screen.w().get() - 20,
					re.font, 24, rgba(200, 200, 200, 255));
			y -= 30; // Assuming line height
			if (y < screen.h().get() - consoleHeight) {
				break;
			}
		}
	}

	public boolean active() {
		return active;
	}

	public void active(boolean active) {
		this.active = active;
	}

	public void handleKey(int key) {
		if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
			if (!currentInput.isEmpty()) {
				history.add("> " + currentInput);
				String output = processCommand(currentInput);
				if (output != null) {
					history.add(output);
				}
				currentInput = "";
			} else {
				active = false;
			}
		} else if (key == GLFW_KEY_ESCAPE) {
			active = false;
		} else if (key == GLFW_KEY_BACKSPACE) {
			if (!currentInput.isEmpty()) {
				currentInput = currentInput.substring(0, currentInput.length() - 1);
			}
		}
	}

	public void handleChar(int codepoint) {
		currentInput += (char) codepoint;
	}

	private String processCommand(String command) {
		if (command.equalsIgnoreCase("HELLO")) {
			return "Hello, Nomad!";
		} else if (command.equalsIgnoreCase("CLEAR")) {
			history.clear();
			return null;
		} else if (command.equalsIgnoreCase("PING")) {
			return "PONG!";
		} else if (command.equalsIgnoreCase("HELP")) {
			return "Commands: HELLO, CLEAR, PING, HELP";
		}
		return "Unknown command: " + command;
	}

}
