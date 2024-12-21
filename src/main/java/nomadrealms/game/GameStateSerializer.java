package nomadrealms.game;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

import java.io.*;

public class GameStateSerializer {

    public static void serialize(GameState gameState, String filePath) throws IOException {
        try (FSTObjectOutput oos = new FSTObjectOutput(new FileOutputStream(filePath))) {
            oos.writeObject(gameState);
        }
    }

    public static GameState deserialize(String filePath) throws IOException, ClassNotFoundException {
        try (FSTObjectInput ois = new FSTObjectInput(new FileInputStream(filePath))) {
            return (GameState) ois.readObject();
        }
    }
}
