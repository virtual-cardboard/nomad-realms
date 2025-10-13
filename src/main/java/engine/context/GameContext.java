package engine.context;

import static engine.common.colour.Colour.normalizedA;
import static engine.common.colour.Colour.normalizedB;
import static engine.common.colour.Colour.normalizedG;
import static engine.common.colour.Colour.normalizedR;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import engine.context.input.Mouse;
import engine.context.input.event.FrameResizedInputEvent;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.KeyRepeatedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.context.input.event.PacketReceivedInputEvent;
import engine.nengen.NengenConfiguration;
import engine.visuals.lwjgl.GLContext;

public class GameContext {

	private boolean initialized = false;
	private GameContextWrapper wrapper;

	protected void init() {
	}

	/**
	 * Renders the context with the given alpha value. The alpha value is the interpolation value between the previous
	 * and next frame. It is used to smooth out the rendering of the context.
	 *
	 * @param alpha
	 */
	protected void render(float alpha) {
	}

	protected void update() {
	}

	protected void terminate() {
	}

	protected void transition(GameContext nextContext) {
		terminate();
		wrapper.setContext(nextContext);
	}

	/**
	 * Sets the context wrapper for this context.
	 * <br>
	 * <br>
	 * This method is package-private because it should only be called by the wrapper.
	 *
	 * @param wrapper The context wrapper.
	 */
	void setWrapper(GameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public NengenConfiguration config() {
		return wrapper.config();
	}

	protected Mouse mouse() {
		return wrapper.mouse();
	}

	public void input(KeyPressedInputEvent event) {
	}

	public void input(KeyReleasedInputEvent event) {
	}

	public void input(KeyRepeatedInputEvent event) {
	}

	public void input(MouseMovedInputEvent event) {
	}

	public void input(MousePressedInputEvent event) {
	}

	public void input(MouseReleasedInputEvent event) {
	}

	public void input(MouseScrolledInputEvent event) {
	}

	public void input(PacketReceivedInputEvent event) {
	}

	public void input(FrameResizedInputEvent event) {
	}

	protected GLContext glContext() {
		return wrapper.glContext();
	}

	protected boolean initialized() {
		return initialized;
	}

	protected final void background(int colour) {
		glClearColor(normalizedR(colour), normalizedG(colour), normalizedB(colour), normalizedA(colour));
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Initializes the context and sets the initialized flag for this context. This method can only be called by the
	 * game engine and should only be called once.
	 */
	void doInit() {
		init();
		initialized = true;
	}

}
