package context.mainmenu;

import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.input.mouse.GameCursor;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.gui.ClickableGui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import engine.common.event.GameEvent;

public class StartButton extends ClickableGui {

	private final Texture startButtonOutline;
	private final TextureRenderer textureRenderer;
	private final TextRenderer textRenderer;
	private final GameFont font;
	private final RectangleRenderer rectangleRenderer;
	private final MainMenuLogic logic;
	private final MainMenuData data;

	public StartButton(ResourcePack resourcePack, MainMenuLogic logic, MainMenuData data) {
		startButtonOutline = resourcePack.getTexture("gui_start_button_outline");
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		font = resourcePack.getFont("langar");
		rectangleRenderer = resourcePack.getRenderer("rectangle", RectangleRenderer.class);
		this.logic = logic;
		this.data = data;
		setWidth(new PixelDimensionConstraint(startButtonOutline.width() * 0.2f));
		setHeight(new PixelDimensionConstraint(startButtonOutline.height() * 0.2f));
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		GameCursor cursor = data.context().input().cursor();
		int buttonColour = pressed() ? 0xa19575FF : (this.containsPoint(cursor.pos()) ? 0xb3a582FF : 0xb5a885FF);
		rectangleRenderer.render(x + width * 0.08f, y + height * 0.1f, width * 0.84f, height * 0.76f, buttonColour);
		textureRenderer.render(startButtonOutline, x, y, width, height);
		textRenderer.alignCenterVertical();
		textRenderer.alignCenterHorizontal();
		textRenderer.render(x, y + height / 2, "Start", width, font, 40, 0xFFFFFFFF);
	}

	public GameEvent doClickEffect() {
		logic.transition();
		return null;
	}

}
