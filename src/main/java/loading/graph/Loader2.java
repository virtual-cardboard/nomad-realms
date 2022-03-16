package loading.graph;

import java.util.concurrent.Future;

public interface Loader2<A, B> {

	public void load(Future<A> a, Future<B> b);

	// GraphLoader g
	// VertexShaderFile v_file
	// FragmentShaderFile f_file
	// Pending<VertexShader> v_future = g.load(new VertexLoader(), v_file, "v");
	// Pending<FragmentShader> f_future = g.load(new FragmentLoader(), f_file, "f");
	// Pending<ShaderProgram> s_future = g.load(new ShaderProgramLoader(), v_future,
	// f_future, "s");
	//
	// g.loadAll(resourcePack);
	//
	// ...elsewhewre
	//
	// VertexShader v = resourcePack.get("v");

}
