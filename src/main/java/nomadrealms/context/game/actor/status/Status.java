package nomadrealms.context.game.actor.status;

public class Status {

	private int[] stacks = new int[StatusEffect.values().length];

	public void add(StatusEffect effect, int count) {
		stacks[effect.ordinal()] += count;
	}

	public void remove(StatusEffect effect, int count) {
		stacks[effect.ordinal()] -= count;
	}

	public int count(StatusEffect effect) {
		return stacks[effect.ordinal()];
	}

}
