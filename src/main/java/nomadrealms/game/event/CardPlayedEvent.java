package nomadrealms.game.event;

import nomadrealms.game.card.WorldCard;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.PlayCardEndIntent;
import nomadrealms.game.card.intent.PlayCardStartIntent;
import nomadrealms.game.card.intent.ReshuffleDecksIntent;
import nomadrealms.game.world.World;
import nomadrealms.render.ui.GameInterface;

import java.util.ArrayList;
import java.util.List;

public class CardPlayedEvent extends InputEvent {

    WorldCard card;
    CardPlayer source;
    Target target;

    public CardPlayedEvent(WorldCard card, CardPlayer source, Target target) {
        this.card = card;
        this.source = source;
        this.target = target;
    }

    public WorldCard card() {
        return card;
    }

    public CardPlayer source() {
        return source;
    }

    public Target target() {
        return target;
    }

    public ProcChain procChain(World world) {
        List<Intent> intents = new ArrayList<>();
        intents.add(new PlayCardStartIntent(card()));
        intents.addAll(card().card().expression().intents(world, target(), source()));
        if (card().card().title().equals("Reshuffle Decks")) {
            intents.add(new ReshuffleDecksIntent(source()));
        }
        intents.add(new PlayCardEndIntent(source(), card()));
        return new ProcChain(intents);
    }

    @Override
    public void resolve(World world) {
        world.resolve(this);
    }

    @Override
    public void resolve(GameInterface ui) {
        ui.resolve(this);
    }

    @Override
    public String toString() {
        return "CardPlayedEvent{" +
                "card=" + card.card() +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
