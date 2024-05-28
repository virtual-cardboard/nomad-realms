package nomadrealms.game.event;

import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.world.World;

import java.util.ArrayList;
import java.util.List;

public class ProcChain {

    private List<Intent> intents = new ArrayList<>();

    public ProcChain(List<Intent> intents) {
        this.intents = new ArrayList<>(intents);
    }

    public void update(World world) {
        Intent intent = intents.remove(0);
        for(Structure structure : world.structures) {
            intent = structure.modify(world, intent);
        }
        for(Structure structure : world.structures) {
            for (ProcChain procChain : structure.trigger(world, intent)) {
                world.proc(procChain);
            }
        }
        intent.resolve(world);
    }

    public boolean empty() {
        return intents.isEmpty();
    }
}
