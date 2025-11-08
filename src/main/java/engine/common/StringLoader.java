package engine.common;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.File;
import java.io.IOException;

public class StringLoader extends FileLoader<String> {

    public StringLoader(File file) {
        super(file);
    }

    @Override
    public String load() {
        try {
            return new String(readAllBytes(get(getFile().getAbsolutePath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
