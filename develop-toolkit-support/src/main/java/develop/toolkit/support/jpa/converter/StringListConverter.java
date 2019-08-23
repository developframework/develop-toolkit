package develop.toolkit.support.jpa.converter;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Collections;
import java.util.List;

/**
 * JPA 值转换器
 *
 * @author qiushui on 2019-02-21.
 */
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (list == null) {
            return null;
        }
        return StringUtils.join(list, ",");
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return Collections.emptyList();
        }
        return List.of(dbData.split(","));
    }
}
