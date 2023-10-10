package com.nomadrealms.loading;

import com.badlogic.gdx.graphics.Texture;

public class LocalAssets {

	public static Texture lavaGolemTexture;

	public static void loadLocalAssets() {
		lavaGolemTexture = new Texture("lava-golem.png");
	}

	public static void disposeLocalAssets() {
		lavaGolemTexture.dispose();
	}

}
