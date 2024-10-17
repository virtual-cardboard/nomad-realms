package nomadrealms.game.event;

import nomadrealms.game.actor.structure.Structure;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.PlayCardEndIntent;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

import java.util.ArrayList;
import java.util.List;

public class ProcChain {

    private List<Intent> intents = new ArrayList<>();

    public ProcChain(List<Intent> intents) {
        this.intents = new ArrayList<>(intents);
    }

    private boolean inRange(World world, Tile source, Tile structure, int i) {
        boolean inRange = false;
        if (source.coord().equals(structure.coord())) {
            return true;
        } else if (i < 5) {
            if (inRange(world, source.dl(world), structure, ++i)) {
                return true;
            }
            if (inRange(world, source.dr(world), structure, ++i)) {
                return true;
            }
            if (inRange(world, source.dm(world), structure, ++i)) {
                return true;
            }
            if (inRange(world, source.ur(world), structure, ++i)) {
                return true;
            }
            if (inRange(world, source.ul(world), structure, ++i)) {
                return true;
            }
            if (inRange(world, source.um(world), structure, ++i)) {
                return true;
            }
        } else {
            return false;
        }
        return inRange;
    }

    public void update(World world) {
        Intent intent = intents.remove(0);
        for (Structure structure : world.structures) {
            intent = structure.modify(world, intent);
        }
        for (Structure structure : world.structures) {
            if (intent instanceof PlayCardEndIntent) {
                PlayCardEndIntent i = (PlayCardEndIntent) intent;
                boolean inRange = inRange(world, i.source().tile(), structure.tile(), 0);
                if (inRange) {
                    List<ProcChain> newProcChains = structure.trigger(world, intent);
                    world.addAllProcChains(newProcChains);
                }
            }
        }
        intent.resolve(world);
    }

    public boolean empty() {
        return intents.isEmpty();
    }
}
