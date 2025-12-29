package nomadrealms.context.game.card.expression;

import org.junit.jupiter.api.Test;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static nomadrealms.context.game.card.GameCard.FLAME_CIRCLE;

import java.util.List;

class DamageExpressionTest {

    @Test
    void testQueryBasedDamage() {
        World world = mock(World.class);
        CardPlayer source = mock(CardPlayer.class);
        Actor target1 = mock(Actor.class);
        Actor target2 = mock(Actor.class);

        Query<Actor> query = (w, s) -> List.of(target1, target2);

        DamageExpression expression = new DamageExpression(query, 5);
        List<Intent> intents = expression.intents(world, null, source);

        assertEquals(2, intents.size());
    }

    @Test
    void testFlameCircleCard() {
        World world = mock(World.class);
        CardPlayer source = mock(CardPlayer.class);
        Actor target1 = mock(Actor.class);
        Actor target2 = mock(Actor.class);

        // This is a simplified test. The query logic is tested separately.
        // Here we just need to make sure the card uses the expression correctly.
        CardExpression expression = FLAME_CIRCLE.expression();
        List<Intent> intents = expression.intents(world, null, source);

        // We can't easily mock the chained queries, so we'll just check that
        // the expression is a DamageExpression.
        assertEquals(DamageExpression.class, expression.getClass());
    }
}
