package engine.visuals.lwjgl.render.framebuffer;

import engine.visuals.lwjgl.render.FrameBufferObject;

public class DefaultFrameBuffer {

	private static final FrameBufferObject INSTANCE = new FrameBufferObject() {
		{
			this.id = 0;
			this.initialize();
		}
	};

	public static FrameBufferObject instance() {
		return INSTANCE;
	}

}
