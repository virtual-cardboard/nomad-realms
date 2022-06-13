package context.game.visuals.gui;

import static context.visuals.colour.Colour.rgb;
import static math.Quaternion.interpolate;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import engine.common.math.Matrix4f;
import engine.common.math.PosDim;
import engine.common.math.Vector2f;
import engine.common.math.Vector2i;
import engine.common.math.Vector3f;
import math.UnitQuaternion;
import model.card.GameCard;

/**
 * The visual representation of a card.
 *
 * @author Jay
 */
public abstract class CardGui extends Gui {

	private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;

	private GameFont font;
	private Texture banner;
	private Texture base;
	private Texture header;
	private Texture decoration;
	private Texture pictureFrame;
	private Texture ribbonLeft;
	private Texture ribbonRight;
	private Texture textBox;

	private Texture art;

	protected boolean hovered;
	protected boolean lockPos;
	protected boolean lockTargetPos;

	protected UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;
	protected Vector2f centerPos = new Vector2f();
	protected Vector2f targetPos = new Vector2f();
	protected float scale = 1;
	protected float targetScale = 1;

	public CardGui(ResourcePack resourcePack, GameCard card) {
		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		this.textRenderer = resourcePack.getRenderer("text", TextRenderer.class);

		banner = resourcePack.getTexture("card_banner");
		base = resourcePack.getTexture("card_base");
		decoration = resourcePack.getTexture("card_decoration_" + card.type.name);
		header = resourcePack.getTexture("card_header");
		pictureFrame = resourcePack.getTexture("card_picture_frame");
		ribbonLeft = resourcePack.getTexture("card_ribbon_left");
		ribbonRight = resourcePack.getTexture("card_ribbon_right");
		textBox = resourcePack.getTexture("card_text_box");

		art = resourcePack.getTexture("card_art_" + card.name().toLowerCase());
		font = resourcePack.getFont("langar");
		setPosX(new BiFunctionPositionConstraint((start, end) -> centerPos.x - width().get(start, end) * 0.5f));
		setPosY(new BiFunctionPositionConstraint((start, end) -> centerPos.y - height().get(start, end) * 0.5f));
		setWidth(new PixelDimensionConstraint(0));
		setHeight(new PixelDimensionConstraint(0));
	}

	public void render(GLContext glContext, NomadsSettings s, String name, String text, int cost) {
		currentOrientation = new UnitQuaternion(interpolate(currentOrientation, DEFAULT_ORIENTATION, 0.2f));
		Matrix4f rotation = currentOrientation.toRotationMatrix();
		Vector2i screenDim = glContext.windowDim();
		((PixelDimensionConstraint) width()).setPixels(s.cardWidth() * scale);
		((PixelDimensionConstraint) height()).setPixels(s.cardHeight() * scale);

		textureRenderer.render(base, makeMatrix(screenDim, rotation, centerPos, new Vector2f(640, 1024), 0, s.cardGuiScale));

		textureRenderer.render(decoration, makeMatrix(screenDim, rotation, centerPos, new Vector2f(768, 768), 4, s.cardGuiScale));
		textureRenderer.render(pictureFrame, makeMatrix(screenDim, rotation, centerPos.add(0, -106), new Vector2f(517, 571), 6, s.cardGuiScale));
		textureRenderer.render(art, makeMatrix(screenDim, rotation, centerPos.add(0, 100), new Vector2f(640, 1324), 8, s.cardGuiScale));
		textureRenderer.render(textBox, makeMatrix(screenDim, rotation, centerPos.add(0, 279), new Vector2f(544, 371), 10, s.cardGuiScale));
		textureRenderer.render(ribbonLeft, makeMatrix(screenDim, rotation, centerPos.add(-218, -217), new Vector2f(133, 174), 11, s.cardGuiScale));
		textureRenderer.render(ribbonRight, makeMatrix(screenDim, rotation, centerPos.add(210, -208), new Vector2f(117, 168), 11, s.cardGuiScale));
		textureRenderer.render(header, makeMatrix(screenDim, rotation, centerPos.add(0, -356), new Vector2f(593, 265), 14, s.cardGuiScale));
		textureRenderer.render(banner, makeMatrix(screenDim, rotation, centerPos.add(0, -375), new Vector2f(696, 156), 18, s.cardGuiScale));
		Matrix4f textTransform = new Matrix4f()
				.translate(centerPos)
				.scale(new Vector3f(1, 1, 0f))
				.multiply(rotation)
				.translate(s.cardDim().scale(-0.5f * scale));
		float w = s.cardWidth() * scale;
		float h = s.cardHeight() * scale;
		Matrix4f cardNameTransform = textTransform.copy().translate(w * 0.0f, h * 0.094f, 16);
		Matrix4f cardTextTransform = textTransform.copy().translate(w * 0.14f, h * 0.64f, 12);
		Matrix4f cardCostTransform = textTransform.copy().translate(w * 0.135f, h * 0.275f, 14);

		textRenderer.alignCenterHorizontal();
		textRenderer.render(cardNameTransform, name, w, font, w * 0.083f, rgb(28, 68, 124));
		textRenderer.alignLeft();
		textRenderer.render(cardTextTransform, text, w * 0.72f, font, w * 0.073f, rgb(28, 68, 124));
		textRenderer.render(cardCostTransform, Integer.toString(cost), 0, font, w * 0.083f, rgb(28, 68, 124));

		updatePosDim();
	}

	private Matrix4f makeMatrix(Vector2i windowDim, Matrix4f rotation, Vector2f center, Vector2f dim, float depth, float guiScale) {
		center = centerPos.add(center.sub(centerPos).scale(guiScale * scale));
		dim = dim.scale(guiScale * scale);
		return new Matrix4f().translate(new Vector2f(-1, 1)).scale(2f / windowDim.x, -2f / windowDim.y)
				.translate(centerPos)
				.scale(new Vector3f(1, 1, 0f))
				.multiply(rotation)
				.translate(center.sub(centerPos).sub(dim.scale(0.5f)))
				.scale(dim)
				.translate(0, 0, depth);
	}

	private void updatePosDim() {
		if (lockPos) {
			return;
		}
		Vector2f centerToTarget = targetPos.sub(centerPos);
		if (centerToTarget.lengthSquared() > 1) {
			centerPos = centerPos.add(centerToTarget.scale(0.3f));
		}
		scale = scale + (targetScale - scale) * 0.6f;
	}

	public Vector2f centerPos() {
		return centerPos;
	}

	public void setCenterPos(Vector2f pos) {
		this.centerPos = pos;
	}

	public Vector2f targetPos() {
		return targetPos;
	}

	public void setTargetPos(Vector2f targetPos) {
		if (lockTargetPos) {
			throw new RuntimeException("locked target pos.");
		}
		this.targetPos = targetPos;
	}

	public void setTargetPos(float x, float y) {
		setTargetPos(new Vector2f(x, y));
	}

	public boolean inPlace() {
		return centerPos.sub(targetPos).lengthSquared() < 1;
	}

	public PosDim posdim(NomadsSettings settings) {
		Vector2f dim = settings.cardDim().scale(scale);
		Vector2f topLeftPos = centerPos.sub(dim.scale(0.5f));
		return new PosDim(topLeftPos.x, topLeftPos.y, dim.x, dim.y);
	}

	public boolean hovered() {
		return hovered;
	}

	public boolean lockedPos() {
		return lockPos;
	}

	public void setLockPos(boolean lockPos) {
		this.lockPos = lockPos;
	}

	public boolean lockedTargetPos() {
		return lockTargetPos;
	}

	public void setLockTargetPos(boolean lockTargetPos) {
		this.lockTargetPos = lockTargetPos;
	}

	public UnitQuaternion currentOrientation() {
		return currentOrientation;
	}

	public void setCurrentOrientation(UnitQuaternion currentOrientation) {
		this.currentOrientation = currentOrientation;
	}

	@Override
	public abstract String toString();

}
