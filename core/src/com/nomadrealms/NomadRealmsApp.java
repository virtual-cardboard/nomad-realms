package com.nomadrealms;

import static com.nomadrealms.loading.LocalAssets.disposeLocalAssets;
import static com.nomadrealms.loading.LocalAssets.lavaGolemTexture;
import static com.nomadrealms.loading.LocalAssets.loadLocalAssets;
import static com.nomadrealms.rendering.basic.shape.HexagonShapeRenderer.renderHexagon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.nomadrealms.rendering.basic.shape.HexagonShapeRenderer;

public class NomadRealmsApp extends ApplicationAdapter {

	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		loadLocalAssets();
		HexagonShapeRenderer.init();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(lavaGolemTexture, 10, 20);
		batch.end();

		renderHexagon(100, 100, 50);

	}

	@Override
	public void dispose() {
		batch.dispose();
		disposeLocalAssets();
	}

}
