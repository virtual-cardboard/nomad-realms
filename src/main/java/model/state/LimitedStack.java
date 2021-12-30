package model.state;

import java.util.ArrayDeque;

public class LimitedStack<E> extends ArrayDeque<E> {

	private static final long serialVersionUID = -8494196510151718151L;
	private int limit;

	public LimitedStack(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean add(E o) {
		super.add(o);
		while (size() > limit) {
			super.remove();
		}
		return true;
	}

}
