package develop.toolkit.base.utils;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * @author qiushui on 2021-03-15.
 */
public abstract class FileAdvice {

    /**
     * 打开缓冲写
     */
    public static BufferedWriter open(File file, boolean append) throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file, append),
                        StandardCharsets.UTF_8
                )
        );
    }

    @SneakyThrows(IOException.class)
    public static void write(File file, String text, boolean append) {
        if (file.exists() || file.getParentFile().mkdirs()) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8)) {
                writer.write(text);
            }
        }
    }

    @SneakyThrows(IOException.class)
    public static void write(File file, List<String> lines, boolean append) {
        if (file.exists() || file.getParentFile().mkdirs()) {
            try (BufferedWriter writer = open(file, append)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    /**
     * 遍历目录 找到所有满足条件的文件
     */
    public static List<File> files(File dir, Predicate<File> predicate) {
        if (dir.isFile()) {
            return Collections.emptyList();
        }
        List<File> files = new LinkedList<>();
        Stack<File> stack = new Stack<>();
        stack.push(dir);
        do {
            eachFiles(stack.pop(), stack, files, predicate);
        } while (!stack.empty());
        return List.copyOf(files);
    }

    private static void eachFiles(File dir, Stack<File> stack, List<File> files, Predicate<File> predicate) {
        final File[] listFiles = dir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    stack.push(file);
                } else if (predicate.test(file)) {
                    files.add(file);
                }
            }
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
