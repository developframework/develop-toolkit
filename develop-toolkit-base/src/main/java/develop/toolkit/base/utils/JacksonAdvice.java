package develop.toolkit.base.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
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
     * 快速序列化
     */
    @SneakyThrows
    public static String quickSerialize(Object object, boolean pretty) {
        if (pretty) {
            return defaultObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } else {
            return defaultObjectMapper().writeValueAsString(object);
        }
    }

    /**
     * 快速反序列化
     */
    @SneakyThrows
    public static <T> T quickDeserialize(String json, Class<T> clazz) {
        return defaultObjectMapper().readValue(json, clazz);
    }

    /**
     * 快速反序列化数组
     */
    @SneakyThrows
    public static <T> T quickDeserializeArray(String json, Class<T> clazz) {
        ObjectMapper objectMapper = defaultObjectMapper();
        ArrayType arrayType = objectMapper.getTypeFactory().constructArrayType(clazz);
        return defaultObjectMapper().readValue(json, arrayType);
    }

    /**
     * 快速反序列化集合
     */
    @SneakyThrows
    public static <T> Collection<T> quickDeserializeCollection(String json, Class<T> clazz, Class<? extends Collection<?>> type) {
        ObjectMapper objectMapper = defaultObjectMapper();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(type, clazz);
        return defaultObjectMapper().readValue(json, collectionType);
    }
}
