package nomadrealms.render.ui.custom.card;

import engine.common.math.Matrix4f;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class StackIcon implements UI {

    private final CardPlayedEvent event;
    private final ConstraintBox constraintBox;

    public StackIcon(CardPlayedEvent event, ConstraintBox constraintBox) {
        this.event = event;
        this.constraintBox = constraintBox;
    }

    @Override
    public void render(RenderingEnvironment re) {
        re.textureRenderer.render(
                re.imageMap.get(event.card().card().artwork()),
                new Matrix4f(constraintBox, re.glContext)
        );
    }

}
