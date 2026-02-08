package engine.common.java;

public class Triple<A, B, C> {

	public A a;
	public B b;
	public C c;

	public Triple(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public C c() {
		return c;
	}

	public A x() {
		return a;
	}

	public B y() {
		return b;
	}

	public C z() {
		return c;
	}

	public A first() {
		return a;
	}

	public B second() {
		return b;
	}

	public C third() {
		return c;
	}

	public A left() {
		return a;
	}

	public B middle() {
		return b;
	}

	public C right() {
		return c;
	}

}
