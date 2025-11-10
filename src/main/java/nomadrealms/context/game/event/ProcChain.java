package nomadrealms.context.game.event;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.actor.structure.Structure;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.world.World;

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
            List<ProcChain> newProcChains = structure.trigger(world, intent);
            world.addAllProcChains(newProcChains);
        }
        intent.resolve(world);
    }

    public boolean empty() {
        return intents.isEmpty();
    }
}
