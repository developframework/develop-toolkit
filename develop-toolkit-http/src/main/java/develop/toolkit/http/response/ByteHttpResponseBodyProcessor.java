package develop.toolkit.http.response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ByteHttpResponseBodyProcessor implements HttpResponseDataBodyProcessor<byte[], String> {

    @Override
    public byte[] parseBodyContent(byte[] data) throws IOException {
        return data;
    }

    @Override
    public String error(byte[] data) throws IOException {
        return new String(data, StandardCharsets.UTF_8);
    }
}
