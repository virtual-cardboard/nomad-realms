package loading;

public class CardArtTextureLoadTask extends NomadsTextureLoadTask {

	public CardArtTextureLoadTask(int textureUnit, String cardArtPath) {
		super(textureUnit, "card_art/" + cardArtPath + ".png");
	}

}
