package context.mainmenu.gui;

import context.ResourcePack;
import context.visuals.gui.TexturedGui;
import context.visuals.renderer.TextureRenderer;

public class SettingsGui extends TexturedGui {

	public SettingsGui(ResourcePack resourcePack) {
		super(resourcePack.getRenderer("texture", TextureRenderer.class), resourcePack.getTexture("gui_main_menu_background"));
	}

}
