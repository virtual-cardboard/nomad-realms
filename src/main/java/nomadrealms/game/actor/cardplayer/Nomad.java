package nomadrealms.game.actor.cardplayer;

import nomadrealms.game.card.WorldCard;
import nomadrealms.game.world.map.tile.Tile;
import nomadrealms.game.zone.Deck;
import nomadrealms.render.RenderingEnvironment;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

import static common.colour.Colour.rgb;
import static java.util.Arrays.stream;
import static nomadrealms.game.card.GameCard.*;
import static nomadrealms.game.world.map.tile.Tile.SCALE;

public class Nomad extends CardPlayer {

    private final String name;


    public Nomad(String name, Tile tile) {
        this.name = name;
        this.tile(tile);
        this.health(10);
        stream(this.deckCollection().decks()).forEach(this::initializeDeck);
    }

    private void initializeDeck(Deck deck) {
        deck
                .addCard(new WorldCard(MOVE))
                .addCard(new WorldCard(HEAL))
                .addCard(new WorldCard(ATTACK))
                .addCard(new WorldCard(GATHER));
        deck.shuffle();
    }

    @Override
    public void render(RenderingEnvironment re) {
        float scale = 0.6f * SCALE;
        DefaultFrameBuffer.instance().render(
                () -> {
                    re.textureRenderer.render(
                            re.imageMap.get("nomad"),
                            tile().getScreenPosition().x() - 0.5f * scale,
                            tile().getScreenPosition().y() - 0.7f * scale,
                            scale, scale
                    );
                    re.textRenderer.render(
                            tile().getScreenPosition().x(),
                            tile().getScreenPosition().y() + 0.1f * scale,
                            name,
                            0,
                            re.font,
                            0.5f * scale,
                            rgb(255, 255, 255)
                    );
                    re.textRenderer.render(
                            tile().getScreenPosition().x(),
                            tile().getScreenPosition().y() + 0.5f * scale,
                            health() + " HP",
                            0,
                            re.font,
                            0.5f * scale,
                            rgb(255, 255, 255)
                    );
                }
        );
    }

}
