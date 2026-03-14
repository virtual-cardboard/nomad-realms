package engine.context.input.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InputCallbackRegistry {

	private final List<Consumer<MousePressedInputEvent>> onPressListeners;
	private final List<Consumer<MouseMovedInputEvent>> onDragListeners;
	private final List<Consumer<MouseReleasedInputEvent>> onDropListeners;

	public InputCallbackRegistry() {
		this.onPressListeners = new ArrayList<>();
		this.onDragListeners = new ArrayList<>();
		this.onDropListeners = new ArrayList<>();
	}

	public void registerOnPress(Consumer<MousePressedInputEvent> listener) {
		onPressListeners.add(listener);
	}

	public void registerOnDrag(Consumer<MouseMovedInputEvent> listener) {
		onDragListeners.add(listener);
	}

	public void registerOnDrop(Consumer<MouseReleasedInputEvent> listener) {
		onDropListeners.add(listener);
	}

	public void triggerOnPress(MousePressedInputEvent event) {
		for (Consumer<MousePressedInputEvent> listener : onPressListeners) {
			listener.accept(event);
		}
	}

	public void triggerOnDrag(MouseMovedInputEvent event) {
		for (Consumer<MouseMovedInputEvent> listener : onDragListeners) {
			listener.accept(event);
		}
	}

	public void triggerOnDrop(MouseReleasedInputEvent event) {
		for (Consumer<MouseReleasedInputEvent> listener : onDropListeners) {
			listener.accept(event);
		}
	}

}
