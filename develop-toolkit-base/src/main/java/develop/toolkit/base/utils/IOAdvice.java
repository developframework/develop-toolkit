package develop.toolkit.base.utils;

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
     * 转换成字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        inputStream.transferTo(baos);
        byte[] data = baos.toByteArray();
        baos.close();
        return data;
    }

    /**
     * 转换成字节数组
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArrayFromClasspath(String filename) throws IOException {
        return toByteArray(readInputStreamFromClasspath(filename));
    }

    /**
     * 文件读取行
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static Stream<String> readLines(String filename) throws IOException {
        return readLines(filename, null);
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
     */
    public static Stream<String> readLines(InputStream inputStream) {
        return readLines(inputStream, null);
    }

    /**
     * 文本流读取行
     *
     * @param inputStream
     * @param charset
     * @return
     */
    public static Stream<String> readLines(InputStream inputStream, Charset charset) {
        Scanner scanner = new Scanner(inputStream, charset == null ? StandardCharsets.UTF_8 : charset);
        List<String> lines = new LinkedList<>();
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines.stream();
    }

    /**
     * 从classpath读流
     *
     * @param filename
     * @return
     */
    public static InputStream readInputStreamFromClasspath(String filename) {
        return IOAdvice.class.getResourceAsStream(filename.startsWith("/") ? filename : ("/" + filename));
    }

    /**
     * 从classpath读取文件
     *
     * @param filename
     * @param charset
     * @return
     */
    public static Stream<String> readLinesFromClasspath(String filename, Charset charset) {
        return readLines(readInputStreamFromClasspath(filename), charset);
    }

    /**
     * 从classpath读取文件
     *
     * @param filename
     * @return
     */
    public static Stream<String> readLinesFromClasspath(String filename) {
        return readLines(readInputStreamFromClasspath(filename), null);
    }

    /**
     * 从classpath读取文件并每行用regex切分
     *
     * @param filename
     * @param regex
     * @return
     */
    public static Stream<String[]> splitFromClasspath(String filename, String regex) {
        return readLinesFromClasspath(filename).map(line -> line.split(regex));
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
     * 复制文本
     *
     * @param inputStream
     * @param outputStream
     * @param charset
     * @param function
     * @throws IOException
     */
    public static void copyText(InputStream inputStream, OutputStream outputStream, Charset charset, Function<String, String> function) throws IOException {
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
     * 复制文件
     *
     * @param inputStream
     * @param outputStream
     * @return
     * @throws IOException
     */
    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        final byte[] buffer = new byte[4096];
        long count = 0;
        int n;
        while (-1 != (n = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 安静地复制文件
     *
     * @param source
     * @param target
     * @return
     */
    public static long copyQuietly(File source, File target) {
        target.getParentFile().mkdirs();
        try (
                InputStream inputStream = new FileInputStream(source);
                OutputStream outputStream = new FileOutputStream(target)
        ) {
            return copy(inputStream, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
