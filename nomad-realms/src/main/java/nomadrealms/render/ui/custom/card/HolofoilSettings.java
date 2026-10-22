package nomadrealms.render.ui.custom.card;

/**
 * Settings for the holographic card finish.
 * <p>
 * Translated from the "Holofoil Cards" tutorial:
 * <ul>
 * <li><b>Foil</b> - the holographic rainbow shine effect which moves as the card rotates.</li>
 * <li><b>Light</b> - the etched-lines effect, emulated with a heightmap-based normal.</li>
 * <li><b>Pattern</b> - a procedural holofoil pattern mask (like Cosmos) restricting where the
 * rainbow appears.</li>
 * </ul>
 *
 * @author Lunkle
 */
public class HolofoilSettings {

	private boolean foilEnabled;
	private boolean lightEnabled;
	private boolean patternEnabled;

	/**
	 * Returns whether any of the effects are enabled, in which case the card should be rendered through the
	 * holofoil shader program.
	 *
	 * @return whether any effect is enabled
	 */
	public boolean anyEnabled() {
		return foilEnabled || lightEnabled || patternEnabled;
	}

	public boolean foilEnabled() {
		return foilEnabled;
	}

	public HolofoilSettings foilEnabled(boolean foilEnabled) {
		this.foilEnabled = foilEnabled;
		return this;
	}

	public boolean lightEnabled() {
		return lightEnabled;
	}

	public HolofoilSettings lightEnabled(boolean lightEnabled) {
		this.lightEnabled = lightEnabled;
		return this;
	}

	public boolean patternEnabled() {
		return patternEnabled;
	}

	public HolofoilSettings patternEnabled(boolean patternEnabled) {
		this.patternEnabled = patternEnabled;
		return this;
	}

}
