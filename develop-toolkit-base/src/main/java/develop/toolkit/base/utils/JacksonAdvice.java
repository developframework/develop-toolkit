package develop.toolkit.base.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.EmptyExpression;
import com.github.developframework.expression.Expression;
import develop.toolkit.base.constants.DateFormatConstants;
import develop.toolkit.base.struct.KeyValuePair;
import lombok.SneakyThrows;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateFormatConstants.STANDARD_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateFormatConstants.STANDARD_FORMATTER));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    /**
     * 常用默认的XmlMapper配置
     */
    public static XmlMapper defaultXmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        xmlMapper.setDateFormat(new SimpleDateFormat(DateFormatConstants.STANDARD));
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.FAIL_ON_EMPTY_BEANS);
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        xmlMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateFormatConstants.STANDARD_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateFormatConstants.STANDARD_FORMATTER));
        xmlMapper.registerModule(javaTimeModule);
        return xmlMapper;
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

    @SneakyThrows(JsonProcessingException.class)
    public static JsonNode deserializeTreeQuietly(ObjectMapper objectMapper, String json) {
        return objectMapper.readTree(json);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> T treeToValueQuietly(ObjectMapper objectMapper, TreeNode node, Class<T> clazz) {
        return objectMapper.treeToValue(node, clazz);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> T deserializeQuietly(ObjectMapper objectMapper, String json, Class<T> clazz) {
        return objectMapper.readValue(json, clazz);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> T[] deserializeArrayQuietly(ObjectMapper objectMapper, String json, Class<T> clazz) {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructArrayType(clazz));
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T extends Collection<E>, E> T deserializeCollectionQuietly(ObjectMapper objectMapper, String json, Class<T> collectionClass, Class<E> itemClass) {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(collectionClass, itemClass));
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

    @SafeVarargs
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

    /**
     * ArrayNode转到List
     */
    public static <T> List<T> arrayNodeToList(ArrayNode arrayNode, Function<JsonNode, T> function) {
        List<T> list = new ArrayList<>(arrayNode.size());
        for (JsonNode node : arrayNode) {
            list.add(function.apply(node));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * ArrayNode转到数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] arrayNodeToArray(ArrayNode arrayNode, Class<T> clazz, Function<JsonNode, T> function) {
        final T[] array = (T[]) Array.newInstance(clazz, arrayNode.size());
        int i = 0;
        for (JsonNode node : arrayNode) {
            array[i++] = function.apply(node);
        }
        return array;
    }

    private static JsonNode parseExpressionToJsonNode(JsonNode jsonNode, Expression expression) {
        if (expression != EmptyExpression.INSTANCE) {
            for (Expression singleExpression : expression.expressionTree()) {
                if (singleExpression.isObject()) {
                    jsonNode = existsJsonNode(jsonNode, singleExpression.getName());
                } else if (singleExpression.isArray()) {
                    ArrayExpression ae = (ArrayExpression) singleExpression;
                    jsonNode = existsJsonNode(jsonNode, ae.getName());
                    for (int i : ae.getIndexArray()) {
                        if (jsonNode.isArray()) {
                            jsonNode = jsonNode.get(i);
                        } else {
                            throw new IllegalArgumentException("jsonNode is not Array,for expression:" + singleExpression);
                        }
                    }
                } else if (singleExpression.isMethod()) {
                    throw new IllegalArgumentException("not support method expression.");
                }
            }
        }
        return jsonNode;
    }

    private static JsonNode existsJsonNode(JsonNode parentNode, String propertyName) {
        if (propertyName.isEmpty() && parentNode.isArray()) {
            return parentNode;
        } else {
            final JsonNode node = parentNode.get(propertyName);
            if (node == null) {
                throw new IllegalArgumentException("Not found node \"" + propertyName + "\"");
            }
            return node;
        }
    }
}
