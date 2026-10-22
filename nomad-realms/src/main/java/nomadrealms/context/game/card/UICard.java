package nomadrealms.context.game.card;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.common.math.Matrix4f;
import engine.common.math.UnitQuaternion;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import static engine.visuals.rendering.text.HorizontalAlign.CENTER;
import static engine.visuals.rendering.text.HorizontalAlign.RIGHT;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.VerticalAlign.MIDDLE;
import static engine.visuals.rendering.text.VerticalAlign.TOP;
import java.util.List;
import engine.visuals.lwjgl.render.ShaderProgram;
import engine.visuals.lwjgl.render.Texture;
import engine.visuals.rendering.text.TextFormat;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.card.target.TargetType;

import static engine.visuals.rendering.text.TextFormat.textFormat;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.card.CardPhysics;
import nomadrealms.render.ui.custom.card.CardTransform;
import nomadrealms.render.ui.custom.card.HolofoilSettings;

/**
 * UI cards are temporary objects that are used to display cards in the UI. They should be created and destroyed as
 * cards are added and removed from the UI.
 * <p>
 * This class describes how to render the card displayed on screen.
 *
 * @author Lunkle
 */
public class UICard implements Card {

	/**
	 * How quickly the streaks scroll when the card rotates (Holo Rotation Scroll Speed, clamped 0-30).
	 */
	private static final float HOLO_ROTATION_SCROLL_SPEED = 10f;

	/**
	 * The direction of the rainbow streaks, in tangent space (flush with the card surface).
	 */
	private static final Vector2f HOLO_DIRECTION = new Vector2f(1, 0).normalise();

	private final CardPhysics physics;

	private final WorldCard card;

	private HolofoilSettings holofoilSettings = new HolofoilSettings();

	public UICard(WorldCard card, ConstraintBox constraintBox) {
		this(card, new CardTransform(new UnitQuaternion(), constraintBox));
	}

	public UICard(WorldCard card, CardTransform transform) {
		this.card = card;
		physics = new CardPhysics(transform);
	}

	@Override
	public CardType type() {
		return card.type();
	}

	@Override
	public List<CardKeyword> keywords() {
		return card.keywords();
	}

	/**
	 * Returns whether this card needs a target to be played.
	 * <p>
	 * For a human player, the card will freeze in place when dragged out and the player needs to use the targeting
	 * arrow to select a valid target.
	 *
	 * @return whether this card needs a target to be played
	 */
	public boolean needsTarget() {
		return card.card().targetingInfo().targetType() != TargetType.NONE;
	}

	/**
	 * Returns the targeting info for this card.
	 *
	 * @return the targeting info for this card
	 * @see TargetingInfo
	 */
	public TargetingInfo targetingInfo() {
		return card.card().targetingInfo();
	}

	/**
	 * Returns the card that this UI card represents.
	 *
	 * @return the world card
	 */
	public WorldCard card() {
		return card;
	}

	public void render(RenderingEnvironment re) {
		if (isUpsideDown()) {
			re.textureRenderer.render(
					re.imageMap.get("card_back"),
					physics.cardTransform(
							re.glContext,
							new Vector3f(0, 0, 0), physics.cardBox().dimensions().vector())
			);
			return;
		}
		for (String cardComponent : new String[]{"card_white_backing", "card_text_banner", "card_bookmarks"}) {
			re.textureRenderer.render(
					re.imageMap.get(cardComponent),
					physics.cardTransform(
							re.glContext,
							new Vector3f(0, 0, 0), physics.cardBox().dimensions().vector())
			);
		}
		// The card base (the whole card face) is rendered through the foil program when enabled, so the
		// metallic sheen covers the rest of the card and not just the artwork box.
		renderFoiled(re, re.imageMap.get("card_base"),
				physics.cardTransform(
						re.glContext,
						new Vector3f(0, 0, 0), physics.cardBox().dimensions().vector()),
				true);
		renderArtwork(re);
		for (String cardComponent : new String[]{"card_title_banner", "card_separator"}) {
			re.textureRenderer.render(
					re.imageMap.get(cardComponent),
					physics.cardTransform(
							re.glContext,
							new Vector3f(0, 0, -5), physics.cardBox().dimensions().vector())
			);
		}
		re.textRenderer
				.render(
						textFormat()
								.text(card.card().title())
								.font(re.font)
								.fontSize((15f - card.card.title().length() / 4f) * physics.cardBox().w().get() / 150)
								.colour(rgb(0, 0, 0))
								.hAlign(CENTER)
								.vAlign(MIDDLE)
								.transform(physics.cardTransform(
										re.glContext,
										new Vector3f(
												physics.cardBox().w().multiply(0.5f).get(),
												physics.cardBox().w().multiply(0.18f).get(),
												0))));
		re.textRenderer
				.render(
						textFormat()
								.text(card.card().description())
								.lineWidth(physics.cardBox().w().multiply(0.74f).get())
								.font(re.font)
								.fontSize(11f * physics.cardBox().w().get() / 150)
								.colour(rgb(0, 0, 0))
								.hAlign(CENTER)
								.vAlign(TOP)
								.transform(physics.cardTransform(
										re.glContext,
										new Vector3f(
												physics.cardBox().w().multiply(0.56f).get(),
												physics.cardBox().h().multiply(0.72f).get(),
												0))));
		re.textRenderer.render(
				textFormat()
						.text(String.valueOf(card.card().resolutionTime()))
						.font(re.font)
						.fontSize(16f * physics.cardBox().w().get() / 150)
						.colour(rgb(0, 0, 0))
						.hAlign(RIGHT)
						.vAlign(TOP)
						.transform(physics.cardTransform(
								re.glContext,
								new Vector3f(
										physics.cardBox().w().multiply(0.94f).get(),
										physics.cardBox().h().multiply(0.01f).get(),
										0))));
		re.textRenderer.render(
				textFormat()
						.text(String.valueOf(((GameCard) card.card()).manaCost()))
						.font(re.font)
						.fontSize(16f * physics.cardBox().w().get() / 150)
						.colour(rgb(0, 0, 0))
						.hAlign(LEFT)
						.vAlign(TOP)
						.transform(physics.cardTransform(
								re.glContext,
								new Vector3f(
										physics.cardBox().w().multiply(0.06f).get(),
										physics.cardBox().h().multiply(0.01f).get(),
										0))));
	}

	/**
	 * Returns whether the card is upside down (i.e., rotated 180 degrees around the X axis). Determines if the card
	 * front or back should be rendered.
	 *
	 * @return whether the card is upside down
	 */
	private boolean isUpsideDown() {
		Vector3f up = new Vector3f(0, 0, 1);
		Vector3f rotatedUp = physics.currentTransform().orientation().rotateVector3f(up);
		return rotatedUp.z() < 0;
	}

	/**
	 * Renders the card artwork, using the holofoil shader program when the holographic finish is enabled.
	 * <p>
	 * The uniforms are a translation of the Shader Graph properties from the "Holofoil Cards" tutorial:
	 * the rainbow hue animates over time, the streaks shift as the card rotates relative to the camera, and
	 * an optional pattern mask restricts where the rainbow appears.
	 *
	 * @param re the rendering environment
	 */
	private void renderArtwork(RenderingEnvironment re) {
		Texture artwork = re.imageMap.get(card().card().artwork());
		Matrix4f transform = physics.cardTransform(
				re.glContext,
				new Vector3f(0, 10, 10),
				new Vector2f(
						physics.cardBox().w().get(),
						physics.cardBox().w().get()));
		renderFoiled(re, artwork, transform, false);
	}

	/**
	 * Renders a texture through the holofoil shader program when the holographic finish is enabled, or through
	 * the default textured program otherwise.
	 * <p>
	 * The uniforms are a translation of the Shader Graph properties from the "Holofoil Cards" tutorial:
	 * the rainbow hue animates over time, the streaks shift as the card rotates relative to the camera, and
	 * an optional pattern mask restricts where the rainbow appears.
	 *
	 * @param re        the rendering environment
	 * @param texture   the texture to render
	 * @param transform the transformation matrix for the texture
	 * @param isCardFace whether the texture is the card face itself (rather than the artwork box)
	 */
	private void renderFoiled(RenderingEnvironment re, Texture texture, Matrix4f transform, boolean isCardFace) {
		if (!holofoilSettings.anyEnabled()) {
			re.textureRenderer.render(texture, transform);
			return;
		}
		ShaderProgram program = re.holofoilShaderProgram;
		// The card face gets a subtler sheen than the artwork box, which takes the full foil strength.
		float strength = isCardFace ? 0.6f : 1.0f;
		float foil = holofoilSettings.foilEnabled() ? strength : 0;
		float light = holofoilSettings.lightEnabled() ? (isCardFace ? 0.5f : 1) : 0;
		float pattern = holofoilSettings.patternEnabled() ? 1 : 0;
		int style = holofoilSettings.style().ordinal();
		// gl_FragCoord is in framebuffer pixels, not window pixels (they differ on HiDPI displays).
		float fbW = re.glContext.fbWidth();
		float fbH = re.glContext.fbHeight();
		// The pivot point of the card in normalized framebuffer coordinates, used to compute how much the
		// streaks shift as the card rotates relative to the camera.
		Vector2f pivot = physics.cardBox().center().vector();
		// How much the card is tilted along the holo streak direction, acting as the rotation-driven
		// scroll offset from the tutorial (dot(Holo Direction, tangent(camera - pivot)) * scroll speed).
		Vector3f cardUp = physics.currentTransform().orientation().rotateVector3f(new Vector3f(0, 0, 1));
		float rotationOffset = cardUp.x() * HOLO_DIRECTION.x() + cardUp.y() * HOLO_DIRECTION.y();
		program
				.set("holoStrength", foil)
				.set("lightStrength", light)
				.set("patternStrength", pattern)
				.set("foilStyle", style)
				.set("holoRotationOffset", rotationOffset * HOLO_ROTATION_SCROLL_SPEED)
				.set("cameraPosScreen", pivot.scale(1 / fbW, 1 / fbH))
				.set("screenDim", new Vector2f(fbW, fbH));
		re.textureRenderer.render(texture, transform, program);
	}

	/**
	 * Returns the holographic finish settings for this card.
	 *
	 * @return the holofoil settings
	 */
	public HolofoilSettings holofoilSettings() {
		return holofoilSettings;
	}

	public void interpolate() {
		physics.interpolate();
	}

	public void move(float x, float y) {
		physics.targetCoord(physics.targetTransform().position().add(x, y));
	}

	public void tilt(Vector2f velocity) {
		physics.tilt(velocity);
	}

	public static ConstraintPair cardSize(float scale) {
		return new ConstraintPair(
				absolute(2.5f * 30 * scale),
				absolute(3.5f * 30 * scale)
		);
	}

	public ConstraintPair position() {
		return physics.position();
	}

	public ConstraintPair centerPosition() {
		return physics.centerPosition();
	}

	public CardPhysics physics() {
		return physics;
	}

}
