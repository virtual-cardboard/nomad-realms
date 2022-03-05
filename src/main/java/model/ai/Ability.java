package model.ai;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import model.card.CardEffect;
import model.card.CardTag;

public enum Ability {

	;

	public final CardEffect effect;

	public final List<CardTag> tags;

	private Ability(CardEffect effect) {
		this.effect = effect;
		this.tags = unmodifiableList(effect.getTags());
	}

}
