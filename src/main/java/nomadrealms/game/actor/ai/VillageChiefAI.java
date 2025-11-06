package nomadrealms.game.actor.ai;

import static nomadrealms.game.card.GameCard.MEANDER;
import static nomadrealms.game.card.GameCard.MELEE_ATTACK;
import static nomadrealms.game.card.target.TargetType.HEXAGON;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.actor.cardplayer.VillageChief;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.world.map.area.Tile;

public class VillageChiefAI extends CardPlayerAI {

    /**
     * No-arg constructor for serialization.
     */
    protected VillageChiefAI() {
    }

    public VillageChiefAI(CardPlayer self) {
        super(self);
    }

    /**
     * Village Chief AI decision-making.
     *
     * @param state the current game state
     */
    @Override
    public void update(GameState state) {
        // Simple AI: Move randomly using MEANDER card
        WorldCard cardToPlay = self.deckCollection().deck1().peek();
        self.addNextPlay(new CardPlayedEvent(cardToPlay, self, self.tile().dl((state.world))));
    }

    // Probably change this later
    @Override
    protected int resetThinkingTime() {
        return 0;
    }
}
