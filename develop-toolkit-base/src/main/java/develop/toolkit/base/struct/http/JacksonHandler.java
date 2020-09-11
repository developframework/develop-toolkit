package develop.toolkit.base.struct.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * @author qiushui on 2020-09-11.
 */
@AllArgsConstructor
public class JacksonHandler<T> extends AbstractStringSenderHandler<T> {

    private final ObjectMapper objectMapper;

    private final Class<T> clazz;

    private final Class<T> type;

    @Override
    public T convert(String json) {
        try {
            if (type == null) {
                return objectMapper.readValue(json, clazz);
            } else if (Collection.class.isAssignableFrom(type)) {
                objectMapper.getTypeFactory().constructCollectionType(type, clazz)
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
