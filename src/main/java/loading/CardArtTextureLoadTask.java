package loading;

public class CardArtTextureLoadTask extends NomadsTextureLoadTask {

	public CardArtTextureLoadTask(String cardArtPath) {
		super("card_art/" + cardArtPath + ".png");
	}

}
