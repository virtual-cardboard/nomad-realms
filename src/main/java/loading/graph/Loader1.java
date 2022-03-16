package loading.graph;

import java.util.concurrent.Future;

public interface Loader1<A> {

	public void load(Future<A> a);

}
