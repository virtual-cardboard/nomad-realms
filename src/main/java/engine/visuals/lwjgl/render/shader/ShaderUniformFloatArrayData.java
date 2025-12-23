package engine.visuals.lwjgl.render.shader;

import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformFloatArrayData extends ShaderUniformData<Float[]> {

    public ShaderUniformFloatArrayData(String name) {
        super(name, Float[].class);
    }

    @Override
    public void setForProgram(ShaderProgram program) {
        if (value != null) {
            float[] primitive = new float[value.length];
            for (int i = 0; i < value.length; i++) {
                primitive[i] = value[i];
            }
            program.set(name, primitive);
        }
    }
}
