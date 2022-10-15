package context.game.visuals.gui.inventory;

import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.NomadsGameData;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.AspectDimensionConstraint;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextureRenderer;
import model.item.Item;
import model.item.ItemCard;
import model.item.ItemCollection;

public class InventoryGui extends Gui {

	private final TextureRenderer textureRenderer;
	private final Texture menuBackground;
	private final ResourcePack resourcePack;
	// If we need to refresh the inventory
	private boolean refreshInventory = true;

	public InventoryGui(ResourcePack resourcePack) {
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		menuBackground = resourcePack.getTexture("gui_menu_background");
		this.resourcePack = resourcePack;
		setWidth(new RelativeDimensionConstraint(0.90f));
		setHeight(new AspectDimensionConstraint(1f * menuBackground.height() / menuBackground.width(), width()));
		setPosX(new CenterPositionConstraint(width()));
		setPosY(new CenterPositionConstraint(height()));
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		textureRenderer.render(menuBackground, x, y, width, height);
		NomadsGameData nomadsGameData = ((NomadsGameData) data);
		ItemCollection inventory = nomadsGameData.playerID().getFrom(nomadsGameData.currentState()).inventory();
		if (refreshInventory) {
			refreshInventory(inventory);
			refreshInventory = false;
		}
	}

	private void refreshInventory(ItemCollection itemCollection) {
		clearChildren();
		for (Item item : itemCollection.keySet()) {
			int amount = itemCollection.get(item);
			for (int i = 0; i < amount; i++) {
				addChild(new ItemCardGui(resourcePack, new ItemCard(item)));
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			refreshInventory = true;
		}
	}

}
