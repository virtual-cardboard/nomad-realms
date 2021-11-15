package context.game.visuals.gui;

import static context.visuals.colour.Colour.rgb;
import static math.Quaternion.interpolate;

import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector3f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import math.UnitQuaternion;
import model.card.GameCard;

public class CardGui extends Gui {

	public static final float WIDTH = 256;
	public static final float HEIGHT = 512;
	public static final float SIZE_FACTOR = 1.2f;
	private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

	private GameCard card;
	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;
	private Texture base;
	private Texture decoration;
	private Texture front;
	private Texture banner;
	private GameFont font;
	private boolean hovered;
	private boolean lockPos;
	private boolean lockTargetPos;

	private UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;

	private Vector2f pos = new Vector2f();
	private Vector2f targetPos = new Vector2f();

	public CardGui(GameCard card, ResourcePack resourcePack) {
		this.card = card;
		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		this.textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		base = resourcePack.getTexture("card_base");
		decoration = resourcePack.getTexture("card_decoration_" + card.type().name);
		front = resourcePack.getTexture("card_front");
		banner = resourcePack.getTexture("card_banner");
		font = resourcePack.getFont("baloo2");
		setWidth(new PixelDimensionConstraint(WIDTH));
		setHeight(new PixelDimensionConstraint(HEIGHT));
		setPosX(new BiFunctionPositionConstraint((start, end) -> pos.x));
		setPosY(new BiFunctionPositionConstraint((start, end) -> pos.y));
	}

	@Override
	public void render(GLContext glContext, Vector2f screenDim, float x, float y, float width, float height) {
		currentOrientation = new UnitQuaternion(interpolate(currentOrientation, DEFAULT_ORIENTATION, 0.05f));
		Matrix4f rotation = currentOrientation.toRotationMatrix();
		Matrix4f copy = rectToPixelMatrix4f(screenDim).translate(x + width * 0.5f, y + height * 0.5f).scale(new Vector3f(1, 1, 0f)).multiply(rotation)
				.translate(-width * 0.5f, -height * 0.5f).scale(width, height);
		textureRenderer.render(glContext, base, copy);
		textureRenderer.render(glContext, decoration, copy.copy().translate(0, 0, 8));
		textureRenderer.render(glContext, front, copy.copy().translate(0, 0, 12));
		textureRenderer.render(glContext, banner, copy.copy().translate(0, 0, 16));
		textureRenderer.render(glContext, card.texture(), copy.copy().translate(0, 0, 20));
		Matrix4f textTransform = new Matrix4f().translate(x + width * 0.5f, y + height * 0.5f).scale(new Vector3f(1, 1, 0f)).multiply(rotation)
				.translate(-width * 0.5f, -height * 0.5f);
		textRenderer.render(glContext, screenDim, textTransform.copy().translate(width * 0.3f, height * 0.45f, 16), card.name(), -1, font,
				width * 0.07f, rgb(28, 68, 124));
		textRenderer.render(glContext, screenDim, textTransform.copy().translate(width * 0.2f, height * 0.55f, 12), card.text(), width * 0.7f, font,
				width * 0.07f, rgb(28, 68, 124));
	}

	public void updatePos() {
		if (lockPos) {
			return;
		}
		float width = ((PixelDimensionConstraint) getWidth()).getPixels();
		float height = ((PixelDimensionConstraint) getHeight()).getPixels();
		float centerX = pos.x + width * 0.5f;
		float centerY = pos.y + height * 0.5f;
		if (Math.abs(targetPos.x - centerX) <= 1 && Math.abs(targetPos.y - centerY) <= 1) {
			pos.set(targetPos.x - width * 0.5f, targetPos.y - height * 0.5f);
			return;
		}
		pos.set(centerX - width * 0.5f + (targetPos.x - centerX) * 0.2f, centerY - height * 0.5f + (targetPos.y - centerY) * 0.2f);
	}

	public void hover() {
		if (!hovered) {
			pos.add(-WIDTH * (SIZE_FACTOR - 1) * 0.5f, -HEIGHT * (SIZE_FACTOR - 1) * 0.5f);
			((PixelDimensionConstraint) getWidth()).setPixels(WIDTH * SIZE_FACTOR);
			((PixelDimensionConstraint) getHeight()).setPixels(HEIGHT * SIZE_FACTOR);
			hovered = true;
		}
	}

	public void unhover() {
		if (hovered) {
			pos.add(WIDTH * (SIZE_FACTOR - 1) * 0.5f, HEIGHT * (SIZE_FACTOR - 1) * 0.5f);
			((PixelDimensionConstraint) getWidth()).setPixels(WIDTH);
			((PixelDimensionConstraint) getHeight()).setPixels(HEIGHT);
			hovered = false;
		}
	}

	public void setPos(Vector2f pos) {
		this.pos.set(pos);
	}

	public void setTargetPos(Vector2f targetPos) {
		setTargetPos(targetPos.x, targetPos.y);
	}

	public void setTargetPos(float x, float y) {
		if (lockTargetPos) {
			throw new RuntimeException("locked target pos.");
		}
		targetPos.set(x, y);
	}

	public boolean inPlace() {
		return centerPos().equals(targetPos);
	}

	public Vector2f pos() {
		return pos;
	}

	public Vector2f centerPos() {
		float width = ((PixelDimensionConstraint) getWidth()).getPixels();
		float height = ((PixelDimensionConstraint) getHeight()).getPixels();
		return pos.copy().add(width * 0.5f, height * 0.5f);
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

	public GameCard card() {
		return card;
	}

	public UnitQuaternion currentOrientation() {
		return currentOrientation;
	}

	public void setCurrentOrientation(UnitQuaternion currentOrientation) {
		this.currentOrientation = currentOrientation;
	}

}
