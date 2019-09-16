package develop.toolkit.world.normal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 不可逆密码基类
 *
 * @author qiushui on 2018-05-26.
 * @version 0.1
 */
@Getter
@EqualsAndHashCode
@ToString(of = "encryptPassword")
public abstract class Password {

    protected String encryptPassword;

    public Password(String password, boolean isEncrypt) {
        if (isEncrypt) {
            this.encryptPassword = password;
        } else {
            this.encryptPassword = encrypt(password);
        }
    }

    /**
     * 比较加密密码字符串
     * @param encryptPassword 加密密码字符串
     * @return
     */
    public boolean equalsEncryptPassword(String encryptPassword) {
        return this.encryptPassword.equals(encryptPassword);
    }

    /**
     * 加密算法
     * @param originalPassword 原文密码
     * @return 加密密码
     */
    public abstract String encrypt(String originalPassword);
}
