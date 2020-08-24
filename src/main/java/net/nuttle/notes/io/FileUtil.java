package net.nuttle.notes.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUtil {

    private int maxDepth = -1;

    public FileUtil() {}

    public FileUtil(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private List<File> getFilesInDir(File dir, final int level) {
        List<File> files = new ArrayList<>();
        if (!dir.isDirectory() || (maxDepth >= 0 && level >= maxDepth)) {
            return files;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir.toPath())) {
            for (Path entry : stream) {
                if (entry.toFile().isFile() && entry.toFile().getName().toLowerCase().endsWith(".md")) {
                    files.add(entry.toFile());
                }
                if (entry.toFile().isDirectory()) {
                    files.addAll(getFilesInDir(entry.toFile(), level + 1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return files;
    }

    public List<File> getFiles(File path) {
        return getFilesInDir(path, 0);
    }
}
