package develop.toolkit.base.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
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
     * @param charset
     * @return
     * @throws IOException
     */
    public static Stream<String> fileReadLines(String filename, Charset charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset))) {
            return reader.lines();
        }
    }

    /**
     * 文本流读取行
     *
     * @param inputStream
     * @param charset
     * @return
     * @throws IOException
     */
    public static Stream<String> readLines(InputStream inputStream, Charset charset) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            return reader.lines();
        }
    }

    /**
     * 写出文本行到文件
     *
     * @param lines
     * @param filename
     * @param charset
     * @throws IOException
     */
    public static void fileWriteLines(List<String> lines, String filename, Charset charset) throws IOException {
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
     * 打印文件
     *
     * @param filename
     * @param charset
     * @throws IOException
     */
    public static void printFile(String filename, Charset charset) throws IOException {
        fileReadLines(filename, charset).forEach(System.out::println);
    }

    /**
     * 打印流文件
     *
     * @param inputStream
     * @param charset
     * @throws IOException
     */
    public static void printInputStream(InputStream inputStream, Charset charset) throws IOException {
        readLines(inputStream, charset).forEach(System.out::println);
    }
}
