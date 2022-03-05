package model.hidden.objective;

import model.card.CardTag;

public enum ObjectiveType {

	VILLAGER_SURVIVE,
	BUILD_HOUSE,
	GATHER_WOOD,
	FIND_BUILD_HOUSE_LOCATION,
	ACTUALLY_BUILD_HOUSE,
	FIND_TREE(CardTag.FINDS_TREE),
	CUT_TREE(CardTag.CUTS_TREE, CardTag.DESTROY),
	GATHER_LOG(CardTag.GATHERS_ITEMS, CardTag.GATHERS_ITEM);

	private CardTag[] tags;

	private ObjectiveType(CardTag... tags) {
		this.tags = tags;
	}

	public CardTag[] tags() {
		return tags;
	}

}
