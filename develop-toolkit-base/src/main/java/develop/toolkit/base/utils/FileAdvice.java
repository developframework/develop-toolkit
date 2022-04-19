package develop.toolkit.base.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author qiushui on 2021-03-15.
 */
public abstract class FileAdvice {

    public static void write(Path filePath, CharSequence text, Charset charset, boolean append) {
        try {
            touch(filePath);
            Files.writeString(
                    filePath,
                    text,
                    charset,
                    StandardOpenOption.WRITE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void write(Path filePath, Iterable<? extends CharSequence> lines, Charset charset, boolean append) {
        try {
            touch(filePath);
            Files.write(
                    filePath,
                    lines,
                    charset,
                    StandardOpenOption.WRITE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void touch(Path path) throws IOException {
        final Path parent = path.getParent();
        if (Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

    /**
     * 遍历目录 找到所有满足条件的文件
     */
    public static List<Path> files(Path path, Predicate<Path> predicate) {
        List<Path> paths = new LinkedList<>();
        try {
            Files.walkFileTree(path, new FileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (predicate.test(file)) {
                        paths.add(path);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            return Collections.unmodifiableList(paths);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 截取文件中某一段的字节数据
     *
     * @param bufferSize 缓冲区大小
     * @param offset     偏移量
     * @param chunkSize  截取块大小
     * @param file       文件 内部会采用随机读取文件RandomAccessFile
     * @param out        输出流
     * @throws IOException IO异常
     */
    public static long sliceBytes(int bufferSize, long offset, long chunkSize, File file, OutputStream out) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            randomAccessFile.seek(offset);
            long transferred = 0L;
            int read;
            final byte[] buffer = new byte[bufferSize];
            while (transferred < chunkSize && (read = randomAccessFile.read(buffer, 0, (int) Math.min(buffer.length, chunkSize - transferred))) != -1) {
                transferred += read;
                out.write(buffer, 0, read);
            }
            return transferred;
        }
    }
}
