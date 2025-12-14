package engine.visuals.lwjgl.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import engine.visuals.lwjgl.render.shader.ShaderUniformData;
import engine.visuals.lwjgl.render.shader.ShaderUniformFloatArrayData;
import engine.visuals.lwjgl.render.shader.ShaderUniformFloatData;
import engine.visuals.lwjgl.render.shader.ShaderUniformIntegerData;

public class ShaderTest {

    private Shader shader;

    @BeforeEach
    public void setUp() {
        shader = new TestShader();
    }

    @Test
    public void testParseSimpleUniform() {
        shader.source = "uniform float test;";
        shader.parseParameters();
        List<ShaderUniformData<?>> parameters = shader.uniforms();
        assertEquals(1, parameters.size());
        assertTrue(parameters.get(0) instanceof ShaderUniformFloatData);
        assertEquals("test", parameters.get(0).name());
    }

    @Test
    public void testParseMultipleUniforms() {
        shader.source = "uniform int test1, test2;";
        shader.parseParameters();
        List<ShaderUniformData<?>> parameters = shader.uniforms();
        assertEquals(2, parameters.size());
        assertTrue(parameters.get(0) instanceof ShaderUniformIntegerData);
        assertEquals("test1", parameters.get(0).name());
        assertTrue(parameters.get(1) instanceof ShaderUniformIntegerData);
        assertEquals("test2", parameters.get(1).name());
    }

    @Test
    public void testParseUniformArray() {
        shader.source = "uniform float weights[10];";
        shader.parseParameters();
        List<ShaderUniformData<?>> parameters = shader.uniforms();
        assertEquals(1, parameters.size());
        assertTrue(parameters.get(0) instanceof ShaderUniformFloatArrayData);
        assertEquals("weights", parameters.get(0).name());
    }

    private class TestShader extends Shader {
        protected TestShader() {
            super(ShaderType.VERTEX);
        }

        @Override
        public void parseParameters() {
            super.parseParameters();
        }
    }
}
