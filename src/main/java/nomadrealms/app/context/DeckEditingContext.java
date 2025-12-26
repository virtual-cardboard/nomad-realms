package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.KeyReleasedInputEvent;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.ButtonUIContent;
import nomadrealms.render.ui.content.ScreenContainerContent;

public class DeckEditingContext extends GameContext {

    private RenderingEnvironment re;
    private DeckCollection deckCollection = new DeckCollection();
    private ScreenContainerContent screen;
    private InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();
    private Deck selectedDeck;

    @Override
    public void init() {
        re = new RenderingEnvironment(glContext(), config());
        screen = new ScreenContainerContent(re);
        ConstraintBox screenBox = glContext().screen;
        ConstraintPair dimensions = new ConstraintPair(
                absolute(200),
                absolute(100)
        );
        for (int i = 0; i < deckCollection.decks().length; i++) {
            final int deckIndex = i;
            ButtonUIContent button = new ButtonUIContent(screen, "Deck " + (i + 1),
                    new ConstraintBox(
                            screenBox.center().add(dimensions.scale(-0.5f)).add(absolute(0), dimensions.y().multiply(1.2f * (i - 1.5f))),
                            dimensions
                    ),
                    () -> {
                        selectedDeck = deckCollection.decks()[deckIndex];
                        System.out.println("Selected deck " + (deckIndex + 1));
                        transition(new MainContext(selectedDeck));
                    });
            button.registerCallbacks(inputCallbackRegistry);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void render(float alpha) {
        background(rgb(100, 100, 100));
        screen.render(re);
    }

    @Override
    public void cleanUp() {
    }

    @Override
    public void input(KeyPressedInputEvent event) {
    }

    @Override
    public void input(KeyReleasedInputEvent event) {
    }

    @Override
    public void input(MouseScrolledInputEvent event) {
    }

    @Override
    public void input(MouseMovedInputEvent event) {
        inputCallbackRegistry.triggerOnDrag(event);
    }

    @Override
    public void input(MousePressedInputEvent event) {
        inputCallbackRegistry.triggerOnPress(event);
    }

    @Override
    public void input(MouseReleasedInputEvent event) {
        inputCallbackRegistry.triggerOnDrop(event);
    }

    public Deck getSelectedDeck() {
        return selectedDeck;
    }
}
