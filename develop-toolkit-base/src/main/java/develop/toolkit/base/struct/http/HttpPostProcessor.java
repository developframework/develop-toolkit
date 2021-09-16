package develop.toolkit.base.struct.http;

import develop.toolkit.base.components.HttpClientSender;

/**
 * Http发送器后置处理
 *
 * @author qiushui on 2021-09-16.
 */
@FunctionalInterface
public interface HttpPostProcessor {

    <T> void process(HttpClientSender sender, HttpClientReceiver<T> receiver);
}
