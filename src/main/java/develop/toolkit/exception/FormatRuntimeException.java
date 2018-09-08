package develop.toolkit.exception;

/**
 * 带格式化的运行时异常
 *
 * @author qiushui
 * @version 0.1
 */
public abstract class FormatRuntimeException extends RuntimeException{

    public FormatRuntimeException() {
    }

    public FormatRuntimeException(String message) {
        super(message);
    }

    public FormatRuntimeException(String format, Object... parameters) {
        this(String.format(format, parameters));
    }

    public FormatRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatRuntimeException(String format, Throwable cause, Object... parameters) {
        this(String.format(format, parameters), cause);
    }
}
