package model.card;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;

public class RandomAccessArrayDeque<E> extends ArrayDeque<E> {

	private static final long serialVersionUID = -8367255056386900106L;

	private Field elementsField;
	private Field headField;
	private Method deleteMethod;

	public RandomAccessArrayDeque() {
		try {
			elementsField = ArrayDeque.class.getDeclaredField("elements");
			elementsField.setAccessible(true);
			headField = ArrayDeque.class.getDeclaredField("head");
			headField.setAccessible(true);
			deleteMethod = ArrayDeque.class.getDeclaredMethod("delete", int.class);
			deleteMethod.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public E get(int index) {
		Object[] elements = null;
		int head = 0;
		try {
			elements = (Object[]) elementsField.get(this);
			head = (int) headField.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (E) elements[(head + index) % elements.length];
	}

	@SuppressWarnings("all")
	public boolean delete(int index) {
		boolean deleted = false;
		try {
			Object[] elements = (Object[]) elementsField.get(this);
			int head = (int) headField.get(this);
			deleted = (boolean) deleteMethod.invoke(this, (head + index) % elements.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}

}
