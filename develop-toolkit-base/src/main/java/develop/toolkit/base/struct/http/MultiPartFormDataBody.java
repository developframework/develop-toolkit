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
import java.util.NoSuchElementException;
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
            throw new IllegalStateException("Must have at least one part to build multipart message.");
        }
        addFinalBoundaryPart();
        return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
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

    static class PartsSpecification {

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

    class PartsIterator implements Iterator<byte[]> {

        private final Iterator<PartsSpecification> iterator = partsSpecificationList.iterator();
        private InputStream currentFileInput;

        private boolean done;
        private byte[] next;

        private static final String NEW_LINE = "\r\n";

        @Override
        public boolean hasNext() {
            if (done) return false;
            if (next != null) return true;
            try {
                next = computeNext();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            if (next == null) {
                done = true;
                return false;
            }
            return true;
        }

        @Override
        public byte[] next() {
            if (!hasNext()) throw new NoSuchElementException();
            byte[] res = next;
            next = null;
            return res;
        }

        private byte[] computeNext() throws IOException {
            if (currentFileInput == null) {
                if (!iterator.hasNext()) return null;
                PartsSpecification nextPart = iterator.next();
                String filename = null;
                String contentType = "application/octet-stream";
                switch (nextPart.type) {
                    case STRING: {
                        return headerBytes(nextPart.name, nextPart.value, null, "text/plain; charset=UTF-8");
                    }
                    case FINAL_BOUNDARY: {
                        return nextPart.value.getBytes(StandardCharsets.UTF_8);
                    }
                    case BYTES: {
                        filename = nextPart.filename;
                        contentType = nextPart.contentType;
                        currentFileInput = new ByteArrayInputStream(nextPart.bytes);
                    }
                    break;
                    case FILE: {
                        filename = nextPart.path.getFileName().toString();
                        contentType = Files.probeContentType(nextPart.path);
                        currentFileInput = Files.newInputStream(nextPart.path);
                    }
                    break;
                    case STREAM: {
                        filename = nextPart.filename;
                        contentType = nextPart.contentType;
                        currentFileInput = nextPart.stream.get();
                    }
                    break;
                }
                return headerBytes(nextPart.name, null, filename, contentType);
            } else {
                byte[] buf = new byte[8192];
                int r = currentFileInput.read(buf);
                if (r > 0) {
                    byte[] actualBytes = new byte[r];
                    System.arraycopy(buf, 0, actualBytes, 0, r);
                    return actualBytes;
                } else {
                    currentFileInput.close();
                    currentFileInput = null;
                    return NEW_LINE.getBytes();
                }
            }
        }

        private byte[] headerBytes(String name, String value, String filename, String contentType) {
            StringBuilder sb = new StringBuilder("--")
                    .append(boundary).append(NEW_LINE)
                    .append("Content-Disposition: form-data; name=").append(name);
            if (filename != null) {
                sb.append("; filename=").append(filename);
            }
            sb.append(NEW_LINE).append("Content-Type: ").append(contentType).append(NEW_LINE).append(NEW_LINE);
            if (value != null) {
                sb.append(value).append(NEW_LINE);
            }
            return sb.toString().getBytes(StandardCharsets.UTF_8);
        }
    }
}
