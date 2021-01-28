package develop.toolkit.base.struct.http;

import develop.toolkit.base.utils.IOAdvice;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Http接收器
 *
 * @author qiushui on 2020-09-10.
 */
@Getter
@Setter
public final class HttpClientReceiver<T> {

    private int httpStatus;

    private Map<String, List<String>> headers;

    private T body;

    private long costTime;

    private boolean connectTimeout;

    private boolean readTimeout;

    private String errorMessage;

    public String getHeader(String header) {
        return StringUtils.join(headers.getOrDefault(header, List.of()), ";");
    }

    public boolean isTimeout() {
        return connectTimeout || readTimeout;
    }

    public boolean isSuccess() {
        return errorMessage == null && !isTimeout() && httpStatus >= 200 && httpStatus < 300;
    }

    public void ifSuccess(Consumer<HttpClientReceiver<T>> consumer) {
        if (isSuccess()) {
            consumer.accept(this);
        }
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
