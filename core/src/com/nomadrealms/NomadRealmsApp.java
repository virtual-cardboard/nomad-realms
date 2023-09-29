package com.nomadrealms;

import static com.nomadrealms.loading.LocalAssets.disposeLocalAssets;
import static com.nomadrealms.loading.LocalAssets.lavaGolemTexture;
import static com.nomadrealms.loading.LocalAssets.loadLocalAssets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class NomadRealmsApp extends ApplicationAdapter {

	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		loadLocalAssets();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(lavaGolemTexture, 10, 20);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		disposeLocalAssets();
	}

}
