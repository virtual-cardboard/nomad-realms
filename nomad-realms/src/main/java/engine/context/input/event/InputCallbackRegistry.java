package engine.context.input.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nomadrealms.context.game.interaction.InteractionState;

public class InputCallbackRegistry {

	private final List<BiConsumer<MousePressedInputEvent, InteractionState>> onPressListeners;
	private final List<BiConsumer<MouseMovedInputEvent, InteractionState>> onDragListeners;
	private final List<BiConsumer<MouseReleasedInputEvent, InteractionState>> onDropListeners;

	public InputCallbackRegistry() {
		this.onPressListeners = new ArrayList<>();
		this.onDragListeners = new ArrayList<>();
		this.onDropListeners = new ArrayList<>();
	}

	public void registerOnPress(BiConsumer<MousePressedInputEvent, InteractionState> listener) {
		onPressListeners.add(listener);
	}

	public void registerOnDrag(BiConsumer<MouseMovedInputEvent, InteractionState> listener) {
		onDragListeners.add(listener);
	}

	public void registerOnDrop(BiConsumer<MouseReleasedInputEvent, InteractionState> listener) {
		onDropListeners.add(listener);
	}

	public void triggerOnPress(MousePressedInputEvent event, InteractionState is) {
		for (BiConsumer<MousePressedInputEvent, InteractionState> listener : onPressListeners) {
			listener.accept(event, is);
		}
	}

	public void triggerOnDrag(MouseMovedInputEvent event, InteractionState is) {
		for (BiConsumer<MouseMovedInputEvent, InteractionState> listener : onDragListeners) {
			listener.accept(event, is);
		}
	}

	public void triggerOnDrop(MouseReleasedInputEvent event, InteractionState is) {
		for (BiConsumer<MouseReleasedInputEvent, InteractionState> listener : onDropListeners) {
			listener.accept(event, is);
		}
	}

}
