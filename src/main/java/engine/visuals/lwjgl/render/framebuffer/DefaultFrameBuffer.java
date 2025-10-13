package engine.visuals.lwjgl.render.framebuffer;

import engine.visuals.lwjgl.render.FrameBufferObject;

public class DefaultFrameBuffer {

	public static FrameBufferObject instance() {
		return new FrameBufferObject();
	}

}
