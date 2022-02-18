package context.game.visuals.gui;

import static context.visuals.colour.Colour.rgb;
import static math.Quaternion.interpolate;

import app.NomadsSettings;
import common.math.Matrix4f;
import common.math.PosDim;
import common.math.Vector2f;
import common.math.Vector2i;
import common.math.Vector3f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import math.UnitQuaternion;
import model.card.WorldCard;
import model.state.GameState;

/**
 * The visual representation of a card.
 * 
 * @author Jay
 *
 */
public class CardGui {

	public static final float WIDTH = 240;
	public static final float HEIGHT = 384;
	private static final Vector2f DEFAULT_DIM = new Vector2f(WIDTH, HEIGHT);
	public static final float SIZE_FACTOR = 1.2f;
	private static final UnitQuaternion DEFAULT_ORIENTATION = new UnitQuaternion(new Vector3f(0, 0, 1), 0);

	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;

	private Texture banner;
	private Texture base;
	private Texture header;
	private Texture decoration;
	private Texture pictureFrame;
	private Texture ribbonLeft;
	private Texture ribbonRight;
	private Texture textBox;

	private GameFont font;
	private boolean hovered;
	private boolean lockPos;
	private boolean lockTargetPos;

	private UnitQuaternion currentOrientation = DEFAULT_ORIENTATION;

	private Vector2f centerPos = new Vector2f();
	private Vector2f targetPos = new Vector2f();
	private Vector2f dim = DEFAULT_DIM;
	private Vector2f targetDim = DEFAULT_DIM;
	private Texture art;
	private long cardID;

	public CardGui(WorldCard card, ResourcePack resourcePack) {
		cardID = card.id();
		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		this.textRenderer = resourcePack.getRenderer("text", TextRenderer.class);

		banner = resourcePack.getTexture("card_banner");
		base = resourcePack.getTexture("card_base");
		decoration = resourcePack.getTexture("card_decoration_" + card.type().name);
		header = resourcePack.getTexture("card_header");
		pictureFrame = resourcePack.getTexture("card_picture_frame");
		ribbonLeft = resourcePack.getTexture("card_ribbon_left");
		ribbonRight = resourcePack.getTexture("card_ribbon_right");
		textBox = resourcePack.getTexture("card_text_box");

		art = resourcePack.getTexture(card.name().toLowerCase().replace(' ', '_'));
		font = resourcePack.getFont("langar");
	}

	public void render(GLContext glContext, NomadsSettings s, GameState state) {
		currentOrientation = new UnitQuaternion(interpolate(currentOrientation, DEFAULT_ORIENTATION, 0.001f));
		Matrix4f rotation = currentOrientation.toRotationMatrix();
//		Matrix4f copy = new Matrix4f().translate(new Vector2f(-1, 1)).scale(2 / screenDim.x, -2 / screenDim.y)
//				.translate(centerPos)
//				.scale(new Vector3f(1, 1, 0f))
//				.multiply(rotation)
//				.translate(dim.scale(0.5f).negate()).scale(dim);
		Vector2i screenDim = glContext.windowDim();
		textureRenderer.render(base, makeMatrix(screenDim, rotation, centerPos, new Vector2f(640, 1024), 0, s.guiScale));

		textureRenderer.render(decoration, makeMatrix(screenDim, rotation, centerPos, new Vector2f(768, 768), 4, s.guiScale));
		textureRenderer.render(pictureFrame, makeMatrix(screenDim, rotation, centerPos.add(0, -106), new Vector2f(517, 571), 8, s.guiScale));
		textureRenderer.render(art, makeMatrix(screenDim, rotation, centerPos.add(0, 100), new Vector2f(640, 1324), 8, s.guiScale));
		textureRenderer.render(textBox, makeMatrix(screenDim, rotation, centerPos.add(0, 279), new Vector2f(544, 371), 10, s.guiScale));
		textureRenderer.render(ribbonLeft, makeMatrix(screenDim, rotation, centerPos.add(-218, -217), new Vector2f(133, 174), 11, s.guiScale));
		textureRenderer.render(ribbonRight, makeMatrix(screenDim, rotation, centerPos.add(210, -208), new Vector2f(117, 168), 11, s.guiScale));
		textureRenderer.render(header, makeMatrix(screenDim, rotation, centerPos.add(0, -356), new Vector2f(593, 265), 14, s.guiScale));
		textureRenderer.render(banner, makeMatrix(screenDim, rotation, centerPos.add(0, -375), new Vector2f(696, 156), 18, s.guiScale));
		Matrix4f textTransform = new Matrix4f()
				.translate(centerPos)
				.scale(new Vector3f(1, 1, 0f))
				.multiply(rotation)
				.translate(-384 * 0.375f, -1024 * 0.365f);
		float w = 768 * 0.375f;
		float h = 1024 * 0.375f;
		Matrix4f cardNameTransform = textTransform.copy().translate(w * 0.13f, h * 0.458f, 16);
		Matrix4f cardTextTransform = textTransform.copy().translate(w * 0.22f, h * 0.53f, 12);
		Matrix4f cardCostTransform = textTransform.copy().translate(w * 0.45f, h * 0.18f, 14);

		WorldCard card = state.card(cardID);
		textRenderer.alignCenter();
		textRenderer.render(cardNameTransform, card.name(), w * 0.76f, font, w * 0.073f, rgb(28, 68, 124));
		textRenderer.alignLeft();
		textRenderer.render(cardTextTransform, card.text(), w * 0.56f, font, w * 0.06f, rgb(28, 68, 124));
		textRenderer.render(cardCostTransform, card.cost() + "", 0, font, w * 0.08f, rgb(28, 68, 124));
	}

	private Matrix4f makeMatrix(Vector2i windowDim, Matrix4f rotation, Vector2f center, Vector2f dim, float depth, float scale) {
		center = centerPos.add(center.sub(centerPos).scale(scale));
		dim = dim.scale(scale);
		return new Matrix4f().translate(new Vector2f(-1, 1)).scale(2f / windowDim.x, -2f / windowDim.y)
				.translate(centerPos)
				.scale(new Vector3f(1, 1, 0f))
				.multiply(rotation)
				.translate(center.sub(centerPos).sub(dim.scale(0.5f)))
				.scale(dim)
				.translate(0, 0, depth);
	}

	public void updatePosDim() {
		if (lockPos) {
			return;
		}
		Vector2f centerToTarget = targetPos.sub(centerPos);
		if (centerToTarget.lengthSquared() > 1) {
			centerPos = centerPos.add(centerToTarget.scale(0.3f));
		}
		Vector2f dimDiff = targetDim.sub(dim);
		if (dimDiff.lengthSquared() > 1) {
			dim = dim.add(dimDiff.scale(0.6f));
		}
	}

	public void hover() {
		if (!hovered) {
			centerPos = centerPos.add(0, -HEIGHT * (SIZE_FACTOR - 1) * 0.5f);
			setTargetPos(targetPos.x, targetPos.y - HEIGHT * 0.3f);
			targetDim = new Vector2f(WIDTH * SIZE_FACTOR, HEIGHT * SIZE_FACTOR);
			hovered = true;
		}
	}

	public void unhover() {
		if (hovered) {
			centerPos = centerPos.add(0, HEIGHT * (SIZE_FACTOR - 1) * 0.5f);
			targetDim = DEFAULT_DIM;
			hovered = false;
		}
	}

	public Vector2f centerPos() {
		return centerPos;
	}

	public void setCenterPos(Vector2f pos) {
		this.centerPos = pos;
	}

	public void setTargetPos(Vector2f targetPos) {
		setTargetPos(targetPos.x, targetPos.y);
	}

	public void setTargetPos(float x, float y) {
		if (lockTargetPos) {
			throw new RuntimeException("locked target pos.");
		}
		targetPos = new Vector2f(x, y);
	}

	public boolean inPlace() {
		return centerPos.sub(targetPos).lengthSquared() < 1;
	}

	public Vector2f dim() {
		return dim;
	}

	public PosDim posdim() {
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

	public long cardID() {
		return cardID;
	}

}
