package engine.visuals.lwjgl.render.shader;

import java.util.function.Consumer;

import engine.common.misc.DataList;
import engine.common.misc.InputDataList;
import engine.visuals.lwjgl.render.ShaderProgram;

public class ShaderUniformInputList extends InputDataList<ShaderUniformData<?>, ShaderProgram> {

	public ShaderUniformInputList(DataList<ShaderUniformData<?>> dataList, ShaderProgram orginal, Consumer<DataList<ShaderUniformData<?>>> onCompletion) {
		super(dataList, orginal, onCompletion);
	}

	public ShaderUniformInputList set(String name, Object value) {
		super.set(name, value);
		return this;
	}

}
