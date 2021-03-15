package develop.toolkit.base.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qiushui on 2021-03-15.
 */
public abstract class FileAdvice {

    public static List<File> files(File dir, FileFilter filter) {
        if (dir.isFile()) {
            return Collections.emptyList();
        }
        List<File> files = new LinkedList<>();
        recursiveFiles(dir, filter, files);
        return Collections.unmodifiableList(files);
    }

    private static void recursiveFiles(File dir, FileFilter filter, List<File> files) {
        if (dir.isDirectory()) {
            final File[] subFiles = dir.listFiles(filter);
            if (subFiles != null && subFiles.length > 0) {
                files.addAll(List.of(subFiles));
            }
        }
    }
}
