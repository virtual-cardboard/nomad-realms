package nomadrealms.render.ui;

import context.input.Mouse;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import nomadrealms.game.GameState;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.GLContext;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class GameInterface {

    private final Queue<InputEvent> stateEventChannel;

    DeckTab deckTab;
    TargetingArrow targetingArrow;

    public GameInterface(Queue<InputEvent> stateEventChannel, GameState state, GLContext glContext, Mouse mouse, List<Consumer<MousePressedInputEvent>> onClick, List<Consumer<MouseMovedInputEvent>> onDrag, List<Consumer<MouseReleasedInputEvent>> onDrop) {
        this.stateEventChannel = stateEventChannel;
        targetingArrow = new TargetingArrow(state).mouse(mouse);
        deckTab = new DeckTab(state.world.nomad, glContext.screen, targetingArrow,
                onClick, onDrag, onDrop);
    }

    public void render(RenderingEnvironment re) {
        if (!stateEventChannel.isEmpty()) {
            stateEventChannel.poll().resolve(this);
        }
        deckTab.render(re);
        targetingArrow.render(re);
    }

    public void resolve(InputEvent event) {
        System.out.println("You should override the double visitor pattern resolve method in "
                + event.getClass().getSimpleName() + " and add a resolve method in World.");
    }

    public void resolve(CardPlayedEvent event) {
        if (event.source() == deckTab.owner) {
            Deck deck = (Deck) event.card().zone();
            deckTab.deleteUI(event.card());
            deckTab.addUI(deck.peek());
        }
    }

}
