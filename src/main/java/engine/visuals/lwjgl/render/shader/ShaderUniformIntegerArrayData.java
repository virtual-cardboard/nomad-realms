package engine.visuals.lwjgl.render.shader;

import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformIntegerArrayData extends ShaderUniformData<Integer[]> {

    public ShaderUniformIntegerArrayData(String name) {
        super(name, Integer[].class);
    }

    @Override
    public void setForProgram(ShaderProgram program) {
        if (value != null) {
            int[] primitive = new int[value.length];
            for (int i = 0; i < value.length; i++) {
                primitive[i] = value[i];
            }
            program.set(name, primitive);
        }
    }
}
