package nomadrealms.context.game.actor.status;

import java.util.ArrayList;
import java.util.List;

public class Status {

	private int[] stacks = new int[StatusEffect.values().length];

	public void add(StatusEffect effect, int count) {
		stacks[effect.ordinal()] += count;
	}

	public void remove(StatusEffect effect, int count) {
		stacks[effect.ordinal()] -= count;
	}

	public int getStacks(StatusEffect effect) {
		return stacks[effect.ordinal()];
	}

	public List<StatusEffect> activeStatusEffects() {
		List<StatusEffect> active = new ArrayList<>();
		for (StatusEffect effect : StatusEffect.values()) {
			if (stacks[effect.ordinal()] > 0) {
				active.add(effect);
			}
		}
		return active;
	}

}
