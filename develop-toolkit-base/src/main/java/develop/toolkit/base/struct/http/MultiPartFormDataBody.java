package develop.toolkit.base.struct.http;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author qiushui on 2020-09-14.
 */
public class MultiPartFormDataBody {

    private final List<PartsSpecification> partsSpecificationList = new ArrayList<>();

    @Getter
    private final String boundary = RandomStringUtils.randomAlphabetic(10);

    public HttpRequest.BodyPublisher buildBodyPublisher() {
        if (partsSpecificationList.isEmpty()) {
            return HttpRequest.BodyPublishers.noBody();
        }
        addFinalBoundaryPart();

        /*
         * 直接使用迭代器获取字节数据会报错  Too few bytes returned by the publisher
         * JDK的bug   参考 https://bugs.openjdk.java.net/browse/JDK-8222968
         */
        // return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
        final byte[] data = assemble();
        return HttpRequest.BodyPublishers.ofByteArray(data);
    }

    public MultiPartFormDataBody addPart(String name, String value) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.Type.STRING;
        newPart.name = name;
        newPart.value = value;
        partsSpecificationList.add(newPart);
        return this;
    }

    public MultiPartFormDataBody addPart(String name, Path path) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.Type.FILE;
        newPart.name = name;
        newPart.path = path;
        partsSpecificationList.add(newPart);
        return this;
    }

    public MultiPartFormDataBody addPart(String name, String filename, String contentType, byte[] bytes) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.Type.BYTES;
        newPart.name = name;
        newPart.bytes = bytes;
        newPart.filename = filename;
        newPart.contentType = contentType;
        partsSpecificationList.add(newPart);
        return this;
    }

    public MultiPartFormDataBody addPart(String name, String filename, String contentType, Supplier<InputStream> stream) {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.Type.STREAM;
        newPart.name = name;
        newPart.stream = stream;
        newPart.filename = filename;
        newPart.contentType = contentType;
        partsSpecificationList.add(newPart);
        return this;
    }

    private void addFinalBoundaryPart() {
        PartsSpecification newPart = new PartsSpecification();
        newPart.type = PartsSpecification.Type.FINAL_BOUNDARY;
        newPart.value = "--" + boundary + "--";
        partsSpecificationList.add(newPart);
    }

    private static class PartsSpecification {

        public enum Type {
            STRING, FILE, BYTES, STREAM, FINAL_BOUNDARY
        }

        public Type type;
        public String name;
        public String value;
        public Path path;
        public byte[] bytes;
        public Supplier<InputStream> stream;
        public String filename;
        public String contentType;

    }

    private class PartsIterator implements Iterator<byte[]> {

        private final Iterator<PartsSpecification> iterator = partsSpecificationList.iterator();

        private InputStream currentInputStream;

        private byte[] nextBytes;

        private static final String NEW_LINE = "\r\n";

        @Override
        public boolean hasNext() {
            try {
                nextBytes = currentInputStream == null ? determineNextPart() : readCurrentInputStream();
                return nextBytes != null;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public byte[] next() {
            byte[] result = nextBytes;
            nextBytes = null;
            return result;
        }

        /**
         * 决定下一个Part
         */
        private byte[] determineNextPart() throws IOException {
            if (!iterator.hasNext()) return null;
            final PartsSpecification nextPart = iterator.next();
            switch (nextPart.type) {
                case FINAL_BOUNDARY: {
                    return nextPart.value.getBytes(StandardCharsets.UTF_8);
                }
                case STRING: {
                    currentInputStream = new ByteArrayInputStream((nextPart.value).getBytes(StandardCharsets.UTF_8));
                    return headerBytes(nextPart.name, null, "text/plain; charset=UTF-8");
                }
                case BYTES: {
                    currentInputStream = new ByteArrayInputStream(nextPart.bytes);
                    return headerBytes(
                            nextPart.name,
                            nextPart.filename,
                            nextPart.contentType
                    );
                }
                case FILE: {
                    currentInputStream = Files.newInputStream(nextPart.path);
                    return headerBytes(
                            nextPart.name,
                            nextPart.path.getFileName().toString(),
                            Files.probeContentType(nextPart.path)
                    );
                }
                case STREAM: {
                    currentInputStream = nextPart.stream.get();
                    return headerBytes(
                            nextPart.name,
                            nextPart.filename,
                            nextPart.contentType
                    );
                }
                default:
                    throw new AssertionError();
            }
        }

        private byte[] readCurrentInputStream() throws IOException {
            byte[] buffer = new byte[8192];
            int r = currentInputStream.read(buffer);
            if (r > 0) {
                byte[] actualBytes = new byte[r];
                System.arraycopy(buffer, 0, actualBytes, 0, r);
                return actualBytes;
            } else {
                currentInputStream.close();
                currentInputStream = null;
                return NEW_LINE.getBytes();
            }
        }

        private byte[] headerBytes(String name, String filename, String contentType) {
            StringBuilder sb = new StringBuilder("--")
                    .append(boundary).append(NEW_LINE)
                    .append("Content-Disposition: form-data; name=").append(name);
            if (filename != null) {
                sb.append("; filename=").append(filename);
            }
            sb.append(NEW_LINE).append("Content-Type: ").append(contentType).append(NEW_LINE).append(NEW_LINE);
            return sb.toString().getBytes(StandardCharsets.UTF_8);
        }
    }

    private byte[] assemble() {
        // 使用以下方法 自己拼装byte[]
        int length = 0, pos = 0;
        PartsIterator iteratorForCount = new PartsIterator();
        while (iteratorForCount.hasNext()) {
            length += iteratorForCount.next().length;
        }
        byte[] data = new byte[length];
        PartsIterator iteratorForBytes = new PartsIterator();
        while (iteratorForBytes.hasNext()) {
            final byte[] nextBytes = iteratorForBytes.next();
            System.arraycopy(nextBytes, 0, data, pos, nextBytes.length);
            pos += nextBytes.length;
        }
        return data;
    }
}
