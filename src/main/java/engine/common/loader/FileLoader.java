package engine.common.loader;

import java.io.File;

public abstract class FileLoader<T> implements Loader<T> {

    private final File file;

    public FileLoader(File file) {
        this.file = file;
    }

    public abstract T load();

    public File getFile() {
        return file;
    }

}
