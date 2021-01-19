package develop.toolkit.base.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.developframework.expression.*;
import develop.toolkit.base.constants.DateFormatConstants;
import develop.toolkit.base.struct.KeyValuePair;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
     * 用表达式从json中取值
     */
    @SneakyThrows(JsonProcessingException.class)
    public static <T> T deserializeValue(ObjectMapper objectMapper, JsonNode rootNode, String expressionValue, Class<T> clazz) {
        return objectMapper.treeToValue(
                parseExpressionToJsonNode(rootNode, Expression.parse(expressionValue)),
                clazz
        );
    }

    /**
     * 用表达式从json中取数组
     */
    @SneakyThrows(JsonProcessingException.class)
    public static <T> T[] deserializeArray(ObjectMapper objectMapper, JsonNode rootNode, String expressionValue, Class<T> clazz) {
        final JsonNode jsonNode = parseExpressionToJsonNode(rootNode, Expression.parse(expressionValue));
        if (!jsonNode.isArray()) {
            throw new IllegalArgumentException("\"" + expressionValue + "\" value is not a array.");
        }
        return objectMapper.readValue(
                jsonNode.toString(),
                objectMapper.getTypeFactory().constructArrayType(clazz)
        );
    }

    /**
     * 用表达式从json中取列表
     */
    @SneakyThrows(JsonProcessingException.class)
    public static <T> List<T> deserializeList(ObjectMapper objectMapper, JsonNode rootNode, String expressionValue, Class<T> clazz) {
        final JsonNode jsonNode = parseExpressionToJsonNode(rootNode, Expression.parse(expressionValue));
        if (!jsonNode.isArray()) {
            throw new IllegalArgumentException("\"" + expressionValue + "\" value is not a list.");
        }
        return objectMapper.readValue(
                jsonNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
        );
    }

    @SneakyThrows(JsonProcessingException.class)
    public static Object[] deserializeValues(ObjectMapper objectMapper, JsonNode rootNode, KeyValuePair<String, Class<?>>... expressionValues) {
        final Object[] values = new Object[expressionValues.length];
        for (int i = 0; i < expressionValues.length; i++) {
            final KeyValuePair<String, Class<?>> kv = expressionValues[i];
            final Expression expression = Expression.parse(kv.getKey());
            JsonNode jsonNode = parseExpressionToJsonNode(rootNode, expression);
            values[i] = objectMapper.treeToValue(jsonNode, kv.getValue());
        }
        return values;
    }

    private static JsonNode parseExpressionToJsonNode(JsonNode jsonNode, Expression expression) {
        if (expression != EmptyExpression.INSTANCE) {
            for (Expression singleExpression : expression.expressionTree()) {
                if (singleExpression instanceof ObjectExpression) {
                    jsonNode = existsJsonNode(jsonNode, ((ObjectExpression) singleExpression).getPropertyName());
                } else if (singleExpression instanceof ArrayExpression) {
                    ArrayExpression ae = (ArrayExpression) singleExpression;
                    jsonNode = existsJsonNode(jsonNode, ae.getPropertyName());
                    if (jsonNode.isArray()) {
                        jsonNode = jsonNode.get(ae.getIndex());
                    }
                } else if (singleExpression instanceof MethodExpression) {
                    throw new IllegalArgumentException("not support method expression.");
                }
            }
        }
        return jsonNode;
    }

    private static JsonNode existsJsonNode(JsonNode parentNode, String propertyName) {
        final JsonNode node = parentNode.get(propertyName);
        if (node == null) {
            throw new IllegalArgumentException("Not found node \"" + propertyName + "\"");
        }
        return node;
    }
}
