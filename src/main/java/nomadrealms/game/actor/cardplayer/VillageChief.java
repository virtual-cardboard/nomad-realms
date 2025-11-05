package nomadrealms.game.actor.cardplayer;

import static engine.common.colour.Colour.rgb;
import static java.util.Arrays.asList;
import static nomadrealms.game.actor.cardplayer.appendage.Appendage.*;
import static nomadrealms.game.card.GameCard.HEAL;
import static nomadrealms.game.card.GameCard.MOVE;
import static nomadrealms.game.card.GameCard.TILL_SOIL;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;

import java.util.List;
import java.util.stream.Stream;

import engine.common.math.Vector2f;
import nomadrealms.game.GameState;
import nomadrealms.game.actor.cardplayer.appendage.Appendage;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;

public class VillageChief extends CardPlayer {

    @Override
    public void render(RenderingEnvironment re) {

    }

    @Override
    public List<Appendage> appendages() {
        return asList(HEAD, EYE, EYE, TORSO, ARM, ARM, LEG, LEG, TAIL);
    }
}
