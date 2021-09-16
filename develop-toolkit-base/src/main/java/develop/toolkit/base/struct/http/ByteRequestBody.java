package develop.toolkit.base.struct.http;

import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-09-16.
 */
@RequiredArgsConstructor
public class ByteRequestBody implements HttpRequestBody<byte[]> {

    private final byte[] bytes;

    @Override
    public String toString() {
        return "(Binary byte data)";
    }

    @Override
    public byte[] getBody() {
        return bytes;
    }
}
