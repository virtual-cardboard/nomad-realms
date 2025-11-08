package engine.common;

import java.io.File;

public abstract class FileLoader<T> {

    private final File file;

    public FileLoader(File file) {
        this.file = file;
    }

    public abstract T load();

    public File getFile() {
        return file;
    }

}
