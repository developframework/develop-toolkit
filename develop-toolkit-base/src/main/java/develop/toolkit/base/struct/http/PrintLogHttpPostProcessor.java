package develop.toolkit.base.struct.http;

import develop.toolkit.base.components.HttpClientSender;
import develop.toolkit.base.utils.DateTimeAdvice;
import develop.toolkit.base.utils.K;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author qiushui on 2021-09-16.
 */
@Slf4j
public final class PrintLogHttpPostProcessor implements HttpPostProcessor {

    @Override
    public void process(HttpClientSender sender, HttpClientReceiver<?> receiver) {
        if (log.isDebugEnabled() && (!sender.isOnlyPrintFailed() || !receiver.isSuccess())) {
            debugPrintLog(sender, receiver);
        }
    }

    private void debugPrintLog(HttpClientSender sender, HttpClientReceiver<?> receiver) {
        StringBuilder sb = new StringBuilder("\n=========================================================================================================\n");
        sb
                .append("\nlabel: ").append(K.def(sender.getDebugLabel(), "(Undefined)"))
                .append("\nhttp request:\n  method: ").append(sender.getMethod()).append("\n  url: ")
                .append(sender.getUri().toString()).append("\n  headers:\n");
        sender
                .getHeaders()
                .forEach((k, v) -> sb.append("    ").append(k).append(": ").append(StringUtils.join(v, ";")).append("\n"));
        sb.append("  body: ").append(sender.getRequestStringBody()).append("\n").append("\nhttp response:\n");
        if (receiver.isConnectTimeout()) {
            sb.append("  (connect timeout ").append(sender.getHttpClient().connectTimeout().map(Duration::getSeconds).orElse(0L)).append("s)");
        } else if (receiver.isReadTimeout()) {
            sb.append("  (read timeout ").append(sender.getReadTimeout().getSeconds()).append("s)");
        } else if (receiver.getErrorMessage() != null) {
            sb.append("  (ioerror ").append(receiver.getErrorMessage()).append(")");
        } else if (receiver.getHeaders() != null) {
            sb.append("  status: ").append(receiver.getHttpStatus()).append("\n  headers:\n");
            for (Map.Entry<String, List<String>> entry : receiver.getHeaders().entrySet()) {
                sb.append("    ").append(entry.getKey()).append(": ").append(StringUtils.join(entry.getValue(), ";")).append("\n");
            }
            sb.append("  cost: ").append(DateTimeAdvice.millisecondPretty(receiver.getCostTime())).append("\n");
            sb.append("  body: ").append(bodyToString(receiver.getBody()));
        }
        sb.append("\n\n=========================================================================================================\n");
        log.debug(sb.toString());
    }

    private String bodyToString(Object body) {
        if (body == null) {
            return "(No content)";
        } else if (body instanceof String) {
            return (String) body;
        } else {
            return "(Binary byte data)";
        }
    }
}
