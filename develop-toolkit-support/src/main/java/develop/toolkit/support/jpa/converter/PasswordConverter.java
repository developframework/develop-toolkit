package develop.toolkit.support.jpa.converter;

import develop.toolkit.base.struct.Password;

import javax.persistence.AttributeConverter;

/**
 * JPA密码转换器
 *
 * @author qiushui on 2019-02-26.
 */
public abstract class PasswordConverter<T extends Password> implements AttributeConverter<T, String> {

    @Override
    public String convertToDatabaseColumn(T password) {
        if (password == null) {
            return null;
        }
        return password.getEncryptPassword();
    }

    @Override
    public T convertToEntityAttribute(String passwordStr) {
        if (passwordStr == null) {
            return null;
        }
        return stringToPassword(passwordStr);
    }

    protected abstract T stringToPassword(String passwordStr);
}
