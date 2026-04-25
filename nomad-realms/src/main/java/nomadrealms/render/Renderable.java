package nomadrealms.render;

import nomadrealms.context.game.interaction.InteractionState;

public interface Renderable {

	void render(RenderingEnvironment re, InteractionState is);

}
