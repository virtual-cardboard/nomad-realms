package engine.visuals.rendering.text;

import engine.visuals.lwjgl.render.Texture;

public final class GameFont {

	private final String name;
	private final int fontSize;
	private final CharacterData[] characterDatas;
	private final Texture texture;

	public GameFont(String name, int fontSize, Texture texture) {
		this.name = name;
		this.fontSize = fontSize;
		characterDatas = new CharacterData[128];
		this.texture = texture;
	}

	public String getName() {
		return name;
	}

	public int getFontSize() {
		return fontSize;
	}

	public CharacterData[] getCharacterDatas() {
		return characterDatas;
	}

	public Texture texture() {
		return texture;
	}

	public void delete() {
		texture.delete();
	}

}
