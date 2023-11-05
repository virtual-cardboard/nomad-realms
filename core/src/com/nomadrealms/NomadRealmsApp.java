package com.nomadrealms;

import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.nomadrealms.loading.LocalAssets.disposeLocalAssets;
import static com.nomadrealms.loading.LocalAssets.lavaGolemTexture;
import static com.nomadrealms.loading.LocalAssets.loadLocalAssets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.nomadrealms.logic.game.world.World;
import com.nomadrealms.math.Vector2i;
import com.nomadrealms.rendering.basic.shape.HexagonShapeRenderer;

/**
 * Main application class.
 * <p>
 * This is a game about nomads in a fantasy world. The world is divided into hexagonal zones. Each zone is a 2D map of
 * tiles. Each tile has a type (grass, water, etc.) and a height. The height is used to determine the type of the tile
 * (grass, snow, etc.).
 */
public class NomadRealmsApp extends ApplicationAdapter {

	private SpriteBatch batch;
	private World world;
	private Vector2 camera = new Vector2(0, 0);

	@Override
	public void create() {
		batch = new SpriteBatch();
		loadLocalAssets();
		HexagonShapeRenderer.init();
		world = new World();
		world.addRegion(new Vector2i(0, 0));
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);

		if (Gdx.input.isKeyPressed(LEFT)) {
			camera.x -= 10;
		} else if (Gdx.input.isKeyPressed(RIGHT)) {
			camera.x += 10;
		}

		batch.begin();
		batch.draw(lavaGolemTexture, 10, 20);
		batch.end();

		world.render(camera);
	}

	@Override
	public void dispose() {
		batch.dispose();
		disposeLocalAssets();
	}

}
