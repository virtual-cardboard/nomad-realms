package nomadrealms.particles;

import engine.common.math.Vector2f;
import engine.common.math.Vector4f;
import engine.visuals.lwjgl.render.ElementBufferObject;
import engine.visuals.lwjgl.render.VertexArrayObject;
import engine.visuals.lwjgl.render.VertexBufferObject;
import nomadrealms.render.RenderingEnvironment;

public class TestParticle extends BaseParticle {

    private static final float[] POSITIONS = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,
    };

    private static final int[] INDICES = {
            0, 1, 2,
            2, 3, 0,
    };

    private static VertexArrayObject vao;

    public TestParticle(float lifetime, float x, float y, float vx, float vy, float ax, float ay) {
        super(lifetime, x, y, vx, vy, ax, ay);
        if (vao == null) {
            VertexBufferObject vbo = new VertexBufferObject().index(0).dimensions(2).data(POSITIONS).load();
            ElementBufferObject ebo = new ElementBufferObject().indices(INDICES).load();
            vao = new VertexArrayObject().vbos(vbo).ebo(ebo).load();
        }
    }

    @Override
    public void render(RenderingEnvironment re) {
		re.quadShaderProgram.use(re.glContext);
		re.quadShaderProgram.set("dimensions", new Vector2f(10, 10));
		re.quadShaderProgram.set("pos", new Vector2f(x, y));
		re.quadShaderProgram.set("color", new Vector4f(1, 1, 1, 1));
        vao.draw(re.glContext);
    }

}
