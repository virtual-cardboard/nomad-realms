package nomadrealms.game.actor.cardplayer;

import static engine.common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.*;
import static nomadrealms.game.card.GameCard.*;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;
import java.util.stream.Stream;

import engine.common.math.Vector2f;
import nomadrealms.game.GameState;
import nomadrealms.game.actor.ai.VillageChiefAI;
import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class VillageChief extends CardPlayer {

    private final String name;

    public VillageChief(String name, Tile tile) {
        this.setAi(new VillageChiefAI(this));
        this.name = name;
        this.tile(tile);
        this.health(10);
        // Add more cards later
        this.deckCollection().deck1().addCards(Stream.of(MEANDER).map(WorldCard::new));
    }

    @Override
    public void render(RenderingEnvironment re) {
        float scale = 0.6f * TILE_RADIUS;
        DefaultFrameBuffer.instance().render(
                () -> {
                    Vector2f screenPosition = getScreenPosition(re);
                    re.textureRenderer.render(
                            re.imageMap.get("chief"),
                            screenPosition.x() - 0.5f * scale,
                            screenPosition.y() - 0.7f * scale,
                            scale, scale);
                    re.textRenderer.render(
                            screenPosition.x(),
                            screenPosition.y() + 0.1f * scale,
                            name + " VILLAGE CHIEF",
                            0,
                            re.font,
                            0.5f * scale,
                            rgb(255, 255, 255));
                    re.textRenderer.render(
                            screenPosition.x(),
                            screenPosition.y() + 0.5f * scale,
                            health() + " HP",
                            0,
                            re.font,
                            0.5f * scale,
                            rgb(255, 255, 255));
                });
    }

    @Override
    public List<Appendage> appendages() {
        return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG, TAIL);
    }
}
