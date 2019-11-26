package develop.toolkit.http;

import develop.toolkit.base.exception.FormatRuntimeException;

public class HttpFailedException extends FormatRuntimeException {

    public HttpFailedException(String format, Object... parameters) {
        super(format, parameters);
    }

    public HttpFailedException(String format, Throwable cause, Object... parameters) {
        super(format, cause, parameters);
    }
}
