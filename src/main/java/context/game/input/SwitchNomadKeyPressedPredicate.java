package context.game.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

import java.util.function.Predicate;

import context.input.event.KeyPressedInputEvent;

public class SwitchNomadKeyPressedPredicate implements Predicate<KeyPressedInputEvent> {

	@Override
	public boolean test(KeyPressedInputEvent t) {
		return t.code() == GLFW_KEY_TAB;
	}

}
