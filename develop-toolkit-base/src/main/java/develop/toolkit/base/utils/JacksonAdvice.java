package develop.toolkit.base.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import develop.toolkit.base.constants.DateFormatConstants;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * @author qiushui on 2020-09-15.
 */
public final class JacksonAdvice {

    /**
     * 常用默认的ObjectMapper配置
     */
    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setDateFormat(new SimpleDateFormat(DateFormatConstants.STANDARD));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateFormatConstants.STANDARD);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    /**
     * 安静地序列化
     */
    @SneakyThrows(JsonProcessingException.class)
    public static String serializeQuietly(ObjectMapper objectMapper, Object object, boolean pretty) {
        if (pretty) {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            return objectMapper.writeValueAsString(object);
        }
    }

    /**
     * 安静地反序列化
     */
    @SneakyThrows({JsonProcessingException.class, JsonMappingException.class})
    public static <T> T deserializeQuietly(ObjectMapper objectMapper, String json, Class<T> clazz) {
        return objectMapper.readValue(json, clazz);
    }

    /**
     * 安静地反序列化数组
     */
    @SneakyThrows({JsonProcessingException.class, JsonMappingException.class})
    public static <T> T deserializeArrayQuietly(ObjectMapper objectMapper, String json, Class<T> clazz) {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructArrayType(clazz));
    }

    /**
     * 安静地反序列化集合
     */
    @SneakyThrows({JsonProcessingException.class, JsonMappingException.class})
    public static <T> Collection<T> deserializeCollectionQuietly(ObjectMapper objectMapper, String json, Class<T> clazz, Class<? extends Collection<?>> type) {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(type, clazz));
    }
}
