package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.IOAdvice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Http接收器
 *
 * @author qiushui on 2020-09-10.
 */
@Getter
@AllArgsConstructor
public final class HttpClientReceiver<T> {

    private final int httpStatus;

    private final Map<String, List<String>> headers;

    private final T body;

    private final long costTime;

    public String getHeader(String header) {
        return StringUtils.join(headers.getOrDefault(header, List.of()), ";");
    }

    public boolean isSuccess() {
        return httpStatus >= 200 && httpStatus < 300;
    }

    public void save(Path path, OpenOption... openOptions) {
        byte[] data;
        if (body instanceof InputStream) {
            data = IOAdvice.toByteArray((InputStream) body);
        } else if (body.getClass().isArray()) {
            data = (byte[]) body;
        } else if (body instanceof String) {
            data = ((String) body).getBytes();
        } else {
            throw new IllegalArgumentException();
        }
        try {
            Files.write(path, data, openOptions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
