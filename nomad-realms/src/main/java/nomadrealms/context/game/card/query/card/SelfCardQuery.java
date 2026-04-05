package nomadrealms.context.game.card.query.card;

import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.event.game.effect.EffectContext;

public class SelfCardQuery implements Query<WorldCard> {

	@Override
	public List<WorldCard> find(EffectContext context) {
		return singletonList(context.card());
	}

}
