package develop.toolkit.base.struct.http;

import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-09-16.
 */
@RequiredArgsConstructor
public class RawRequestBody implements HttpRequestBody<String> {

    private final String raw;

    @Override
    public String toString() {
        return raw;
    }

    @Override
    public String getBody() {
        return raw;
    }
}
