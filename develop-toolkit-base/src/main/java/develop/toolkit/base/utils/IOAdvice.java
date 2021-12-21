package develop.toolkit.base.utils;

import develop.toolkit.base.struct.ListInMap;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 输入输出流增强
 *
 * @author qiushui on 2019-02-21.
 */
@SuppressWarnings("unused")
public final class IOAdvice {

    /**
     * 转换成字节数组
     */
    public static byte[] toByteArray(InputStream inputStream) {
        try (inputStream; ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            inputStream.transferTo(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换成字节数组
     */
    public static byte[] toByteArrayFromClasspath(String filename) {
        return toByteArray(readInputStreamFromClasspath(filename));
    }

    /**
     * 文件读取行
     */
    public static Stream<String> readLines(String filename) {
        return readLines(filename, null);
    }

    /**
     * 文件读取行
     */
    public static Stream<String> readLines(String filename, Charset charset) {
        try {
            return readLines(new FileInputStream(filename), charset);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文本流读取行
     */
    public static Stream<String> readLines(InputStream inputStream) {
        return readLines(inputStream, null);
    }

    /**
     * 文本流读取行
     */
    public static Stream<String> readLines(InputStream inputStream, Charset charset) {
        try (inputStream) {
            Scanner scanner = new Scanner(inputStream, charset == null ? StandardCharsets.UTF_8 : charset);
            List<String> lines = new LinkedList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
            return lines.stream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从classpath读流
     */
    public static InputStream readInputStreamFromClasspath(String filename) {
        return IOAdvice.class.getResourceAsStream(filename.startsWith("/") ? filename : ("/" + filename));
    }

    /**
     * 从classpath读取文件
     */
    public static Stream<String> readLinesFromClasspath(String filename, Charset charset) {
        return readLines(readInputStreamFromClasspath(filename), charset);
    }

    /**
     * 从classpath读取文件
     */
    public static Stream<String> readLinesFromClasspath(String filename) {
        return readLines(readInputStreamFromClasspath(filename), null);
    }

    /**
     * 从classpath读取文件并每行用regex切分
     */
    public static Stream<String[]> splitFromClasspath(String filename, String regex) {
        return readLinesFromClasspath(filename).map(line -> line.split(regex));
    }

    /**
     * 从classpath读取文件并每行用regex切分，然后装填到实体类
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> splitFromClasspath(String filename, String regex, Class<T> clazz) {
        List<String[]> list = splitFromClasspath(filename, regex).collect(Collectors.toList());
        if (list.isEmpty()) {
            return Stream.empty();
        } else {
            final String[] first = list.get(0);
            final Constructor<?> constructor = ArrayAdvice
                    .getFirstMatch(clazz.getConstructors(), first.length, Constructor::getParameterCount)
                    .orElseThrow(() -> new IllegalArgumentException("No match constructor for parameter size: " + first.length));
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            return list
                    .stream()
                    .map(objs -> {
                        try {
                            final Object[] parameters = new Object[objs.length];
                            for (int i = 0; i < objs.length; i++) {
                                parameters[i] = ObjectAdvice.primitiveTypeCast(objs[i], parameterTypes[i]);
                            }
                            return (T) constructor.newInstance(parameters);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    /**
     * 从classpath读取文件并每行用regex切分，然后按keyFunction分组
     */
    public static <K> ListInMap<K, String[]> splitGroupingFormClasspath(String filename, String regex, Function<String[], K> keyFunction) {
        ListInMap<K, String[]> map = new ListInMap<>();
        splitFromClasspath(filename, regex).forEach(objs -> map.putItem(keyFunction.apply(objs), objs));
        return map;
    }

    /**
     * 从classpath读取文件并每行用regex切分，然后按keyFunction分组，明确值是唯一的
     */
    public static <K> Map<K, String[]> splitGroupingUniqueFormClasspath(String filename, String regex, Function<String[], K> keyFunction) {
        Map<K, String[]> map = new HashMap<>();
        splitFromClasspath(filename, regex).forEach(objs -> {
            K k = keyFunction.apply(objs);
            if (map.containsKey(k)) {
                throw new IllegalStateException("exists key \"" + k + "\"");
            }
            map.put(k, objs);
        });
        return map;
    }

    /**
     * 从classpath读取文件并每行用regex切分，装填到实体类，然后按keyFunction分组
     */
    public static <K, T> ListInMap<K, T> splitGroupingFormClasspath(String filename, String regex, Class<T> clazz, Function<T, K> keyFunction) {
        ListInMap<K, T> map = new ListInMap<>();
        splitFromClasspath(filename, regex, clazz).forEach(t -> map.putItem(keyFunction.apply(t), t));
        return map;
    }

    /**
     * 从classpath读取文件并每行用regex切分，然后按keyFunction分组
     */
    public static <K, V> ListInMap<K, V> splitGroupingFormClasspath(String filename, String regex, Function<String[], K> keyFunction, Function<String[], V> valueFunction) {
        ListInMap<K, V> map = new ListInMap<>();
        splitFromClasspath(filename, regex).forEach(objs -> map.putItem(keyFunction.apply(objs), valueFunction.apply(objs)));
        return map;
    }

    /**
     * 从classpath读取文件并每行用regex切分，装填到实体类，然后按keyFunction分组
     */
    public static <K, V, T> ListInMap<K, V> splitGroupingFormClasspath(String filename, String regex, Class<T> clazz, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        ListInMap<K, V> map = new ListInMap<>();
        splitFromClasspath(filename, regex, clazz).forEach(t -> map.putItem(keyFunction.apply(t), valueFunction.apply(t)));
        return map;
    }

    /**
     * 从classpath读取文件并每行用regex切分，然后按keyFunction分组，明确值是唯一的
     */
    public static <K, V> Map<K, V> splitGroupingUniqueFormClasspath(String filename, String regex, Function<String[], K> keyFunction, Function<String[], V> valueFunction) {
        Map<K, V> map = new HashMap<>();
        splitFromClasspath(filename, regex).forEach(objs -> {
            K k = keyFunction.apply(objs);
            if (map.containsKey(k)) {
                throw new IllegalStateException("exists key \"" + k + "\"");
            }
            map.put(k, valueFunction.apply(objs));
        });
        return map;
    }

    /**
     * 读取文本
     */
    public static String readText(InputStream inputStream, Charset charset) {
        StringBuilder sb = new StringBuilder();
        readLines(inputStream, charset).forEach(line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 读取文本
     */
    public static String readText(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        readLines(inputStream).forEach(line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 从classpath读取文本
     */
    public static String readTextFromClasspath(String filename, Charset charset) {
        StringBuilder sb = new StringBuilder();
        readLinesFromClasspath(filename, charset).forEach(line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 从classpath读取文本
     */
    public static String readTextFromClasspath(String filename) {
        StringBuilder sb = new StringBuilder();
        readLinesFromClasspath(filename).forEach(line -> sb.append(line.trim()));
        return sb.toString();
    }

    /**
     * 写出文本行到文件
     */
    public static void writeLines(List<String> lines, String filename, Charset charset) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), charset))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写出文本行到文件
     */
    public static void writeLines(List<String> lines, String filename) {
        writeLines(lines, filename, StandardCharsets.UTF_8);
    }

    /**
     * 写出文本行
     */
    public static void writeLines(List<String> lines, OutputStream outputStream, Charset charset) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 追加文本行
     */
    public static void appendLines(List<String> lines, String filename, Charset charset) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), charset))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 追加文本行
     */
    public static void appendLines(List<String> lines, String filename) {
        appendLines(lines, filename, StandardCharsets.UTF_8);
    }

    /**
     * 复制文本
     */
    public static void copyText(InputStream inputStream, OutputStream outputStream, Charset charset, Function<String, String> function) {
        Scanner scanner = new Scanner(inputStream, charset);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
            while (scanner.hasNext()) {
                String line = function.apply(scanner.nextLine());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scanner.close();
    }

    /**
     * 安静地复制文件
     */
    public static long copyQuietly(File source, File target) {
        if (target.getParentFile().mkdirs()) {
            try (
                    InputStream inputStream = new FileInputStream(source);
                    OutputStream outputStream = new FileOutputStream(target)
            ) {
                return inputStream.transferTo(outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }

    /**
     * 打印文件
     */
    public static void printFile(String filename, Charset charset) {
        readLines(filename, charset).forEach(System.out::println);
    }

    /**
     * 打印流文件
     */
    public static void printInputStream(InputStream inputStream, Charset charset) {
        readLines(inputStream, charset).forEach(System.out::println);
    }

    /**
     * 打印流文件
     */
    public static void printInputStream(InputStream inputStream) {
        readLines(inputStream, StandardCharsets.UTF_8).forEach(System.out::println);
    }

    /**
     * 截取输入流中某一段的字节数据
     *
     * @param bufferSize 缓冲区大小
     * @param offset     偏移量
     * @param chunkSize  截取块大小
     * @param in         输入流
     * @param out        输出流
     * @throws IOException IO异常
     */
    public static long sliceBytes(int bufferSize, long offset, long chunkSize, InputStream in, OutputStream out) throws IOException {
        in.skip(offset);
        long transferred = 0L;
        int read;
        final byte[] buffer = new byte[bufferSize];
        while (transferred < chunkSize && (read = in.read(buffer, 0, (int) Math.min(buffer.length, chunkSize - transferred))) != -1) {
            transferred += read;
            out.write(buffer, 0, read);
        }
        return transferred;
    }
}
