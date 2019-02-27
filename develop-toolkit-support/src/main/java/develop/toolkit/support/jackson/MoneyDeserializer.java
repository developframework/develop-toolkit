package develop.toolkit.support.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 金额反序列化
 *
 * @author qiushui on 2019-02-21.
 */
public class MoneyDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getIntValue() * 100;
    }
}
