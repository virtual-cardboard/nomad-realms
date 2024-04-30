package nomadrealms.game.event;

import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.world.World;

import java.util.ArrayList;
import java.util.List;

public class ProcChain {

    private final World world;
    private List<Intent> intents = new ArrayList<>();

    public ProcChain(World world, List<Intent> intents) {
        this.world = world;
        this.intents = new ArrayList<>(intents);
    }

    public void update() {
        Intent intent = intents.remove(0);
        intent.resolve(world);
    }

    public boolean empty() {
        return intents.isEmpty();
    }
}
