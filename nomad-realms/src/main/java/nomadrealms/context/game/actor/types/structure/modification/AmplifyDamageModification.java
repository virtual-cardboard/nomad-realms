package nomadrealms.context.game.actor.types.structure.modification;

import nomadrealms.context.game.card.effect.DamageEffect;
import nomadrealms.context.game.card.effect.Effect;

public class AmplifyDamageModification extends Modification<DamageEffect> {

	private int additionalDamage;

	public AmplifyDamageModification(int additionalDamage) {
		this.additionalDamage = additionalDamage;
	}


	@Override
	public Effect modify(DamageEffect effect) {
		effect.damage(effect.damage() + additionalDamage);
		return effect;
	}
}
