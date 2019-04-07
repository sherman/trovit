package com.sherman.trovit.util;

import java.io.File;

/**
 * @author Denis M. Gabaydulin
 * @since 07.04.19
 */
public class Settings {
    private final File resultPath;

    public Settings(File resultPath) {
        this.resultPath = resultPath;
    }

    public File getResultPath() {
        return resultPath;
    }
}
