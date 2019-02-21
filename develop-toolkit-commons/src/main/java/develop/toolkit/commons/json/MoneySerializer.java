package develop.toolkit.commons.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 金额序列化
 *
 * @author qiushui on 2019-02-21.
 */
public class MoneySerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(value / 100);
    }
}
