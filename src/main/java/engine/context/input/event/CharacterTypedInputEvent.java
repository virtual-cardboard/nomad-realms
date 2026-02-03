package engine.context.input.event;

/**
 * A character typed input event is a simple event that contains the Unicode code point of the character that was
 * typed.
 *
 * @author Lunkle
 */
public final class CharacterTypedInputEvent extends GameInputEvent {

	private final int codepoint;

	public CharacterTypedInputEvent(int codepoint) {
		this.codepoint = codepoint;
	}

	public int codepoint() {
		return codepoint;
	}

}
