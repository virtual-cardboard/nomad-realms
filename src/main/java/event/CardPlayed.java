package event;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import common.source.GameSource;

public class CardPlayed extends SerializableEvent implements Serializable {

	private static final long serialVersionUID = -8409103019887443437L;

	private List<Integer> ids;

	public CardPlayed(long time, GameSource source, List<Integer> ids) {
		super(time, source);
		this.ids = ids;
	}

	public CardPlayed(GameSource source, List<Integer> ids) {
		super(source);
		this.ids = ids;
	}

	public Stream<Integer> getIds() {
		return ids.stream();
	}

}
