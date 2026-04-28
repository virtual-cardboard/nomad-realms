package nomadrealms.render.ui.custom.join;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.colour.Colour;
import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.rendering.text.TextFormat;
import java.util.List;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;
import nomadrealms.user.Player;

public class JoinWorldInterface {

	private final ScreenContainerContent joinWorldScreen;
	private final ButtonUIContent returnHomeButton;

	public JoinWorldInterface(RenderingEnvironment re, GLContext glContext, InputCallbackRegistry registry) {

		joinWorldScreen = new ScreenContainerContent(re);

		ConstraintBox screen = glContext.screen;
		ConstraintPair dimensions = new ConstraintPair(absolute(200), absolute(50));

		returnHomeButton = new ButtonUIContent(joinWorldScreen, "Return to Home",
				new ConstraintBox(
						new ConstraintPair(screen.left().add(absolute(20)), screen.bottom().add(absolute(-70))),
						dimensions
				), null);
		returnHomeButton.registerCallbacks(registry);
	}

	public void initHomeButton(Runnable onClick) {
		returnHomeButton.setCallbacks(onClick);
	}

	public void render(RenderingEnvironment re, List<Player> onlinePlayers) {
		joinWorldScreen.render(re);

		float startX = 20;
		float startY = 20;
		float width = 300;
		float height = 50 + (onlinePlayers.size() * 30);

		// Draw background box
		re.rectangleRenderer.render(startX, startY, width, height, 10, Colour.rgba(0, 0, 0, 180));

		// Draw Title
		re.textRenderer.render(startX + 10, startY + 10,
				TextFormat.textFormat()
						.text("Online Players: " + onlinePlayers.size())
						.font(re.font)
						.fontSize(20)
						.colour(Colour.rgb(255, 255, 255)));

		// Draw Player List
		float yOffset = startY + 40;
		for (Player p : onlinePlayers) {
			re.textRenderer.render(startX + 10, yOffset,
					TextFormat.textFormat()
							.text(p.name() + " (" + p.address() + ")")
							.font(re.font)
							.fontSize(16)
							.colour(Colour.rgb(200, 200, 200)));
			yOffset += 30;
		}
	}
}
