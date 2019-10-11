package develop.toolkit.base.utils;

import lombok.NonNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 输入输出流增强
 *
 * @author qiushui on 2019-02-21.
 */
public final class IOAdvice {

    /**
     * 文件读取行
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static Stream<String> readLines(String filename) throws IOException {
        return readLines(filename, StandardCharsets.UTF_8);
    }

    /**
     * 文件读取行
     *
     * @param filename
     * @param charset
     * @return
     * @throws IOException
     */
    public static Stream<String> readLines(String filename, Charset charset) throws IOException {
        return readLines(new FileInputStream(filename), charset);
    }

    /**
     * 文本流读取行
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static Stream<String> readLines(InputStream inputStream) {
        return readLines(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * 文本流读取行
     *
     * @param inputStream
     * @param charset
     * @return
     */
    public static Stream<String> readLines(@NonNull InputStream inputStream, Charset charset) {
        Scanner scanner = new Scanner(inputStream, charset);
        List<String> lines = new LinkedList<>();
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines.stream();
    }

    /**
     * 从classpath读取文件
     *
     * @param filename
     * @param charset
     * @return
     * @throws IOException
     */
    public static Stream<String> readLinesFromClasspath(String filename, Charset charset) {
        return readLines(IOAdvice.class.getResourceAsStream(filename), charset);
    }

    /**
     * 从classpath读取文件
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static Stream<String> readLinesFromClasspath(String filename) {
        return readLines(IOAdvice.class.getResourceAsStream(filename));
    }

    /**
     * 读取文本
     *
     * @param inputStream
     * @param charset
     * @return
     */
    public static String readText(InputStream inputStream, Charset charset) {
        StringBuilder sb = new StringBuilder();
        readLines(inputStream, charset).forEach(line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 读取文本
     *
     * @param inputStream
     * @return
     */
    public static String readText(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        readLines(inputStream).forEach(line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 从classpath读取文本
     *
     * @param filename
     * @param charset
     * @return
     */
    public static String readTextFromClasspath(String filename, Charset charset) {
        StringBuilder sb = new StringBuilder();
        forEachFromClasspath(filename, charset, line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 从classpath读取文本
     *
     * @param filename
     * @return
     */
    public static String readTextFromClasspath(String filename) {
        StringBuilder sb = new StringBuilder();
        forEachFromClasspath(filename, line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 文本流按行循环处理
     *
     * @param inputStream
     * @param charset
     * @param consumer
     */
    public static void forEach(InputStream inputStream, Charset charset, Consumer<String> consumer) {
        readLines(inputStream, charset).forEach(consumer);
    }

    /**
     * 文本流按行循环处理
     *
     * @param inputStream
     * @param consumer
     */
    public static void forEach(InputStream inputStream, Consumer<String> consumer) {
        readLines(inputStream).forEach(consumer);
    }

    /**
     * classpath文本流按行循环处理
     *
     * @param filename
     * @param charset
     * @param consumer
     */
    public static void forEachFromClasspath(String filename, Charset charset, Consumer<String> consumer) {
        readLinesFromClasspath(filename, charset).forEach(consumer);
    }

    /**
     * classpath文本流按行循环处理
     *
     * @param filename
     * @param consumer
     */
    public static void forEachFromClasspath(String filename, Consumer<String> consumer) {
        readLinesFromClasspath(filename).forEach(consumer);
    }

    /**
     * 写出文本行到文件
     *
     * @param lines
     * @param filename
     * @param charset
     * @throws IOException
     */
    public static void writeLines(List<String> lines, String filename, Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), charset))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        }
    }

    /**
     * 写出文本行
     *
     * @param lines
     * @param outputStream
     * @param charset
     * @throws IOException
     */
    public static void writeLines(List<String> lines, OutputStream outputStream, Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        }
    }

    /**
     * 追加文本行
     *
     * @param lines
     * @param filename
     * @param charset
     * @throws IOException
     */
    public static void appendLines(List<String> lines, String filename, Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), charset))) {
            for (String line : lines) {
                writer.append(line);
                writer.newLine();
            }
            writer.flush();
        }
    }

    /**
     * 追加文本行
     *
     * @param lines
     * @param outputStream
     * @param charset
     * @throws IOException
     */
    public static void appendLines(List<String> lines, OutputStream outputStream, Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            for (String line : lines) {
                writer.append(line);
                writer.newLine();
            }
            writer.flush();
        }
    }

    /**
     * 转移
     *
     * @param inputStream
     * @param outputStream
     * @param charset
     * @param function
     * @throws IOException
     */
    public static void transferText(InputStream inputStream, OutputStream outputStream, Charset charset, Function<String, String> function) throws IOException {
        Scanner scanner = new Scanner(inputStream, charset);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            while (scanner.hasNext()) {
                String line = function.apply(scanner.nextLine());
                writer.write(line);
                writer.newLine();
            }
        }
        scanner.close();
    }

    /**
     * 打印文件
     *
     * @param filename
     * @param charset
     * @throws IOException
     */
    public static void printFile(String filename, Charset charset) throws IOException {
        readLines(filename, charset).forEach(System.out::println);
    }

    /**
     * 打印流文件
     *
     * @param inputStream
     * @param charset
     * @throws IOException
     */
    public static void printInputStream(InputStream inputStream, Charset charset) {
        readLines(inputStream, charset).forEach(System.out::println);
    }

    /**
     * 打印流文件
     *
     * @param inputStream
     */
    public static void printInputStream(InputStream inputStream) {
        readLines(inputStream, StandardCharsets.UTF_8).forEach(System.out::println);
    }
}
