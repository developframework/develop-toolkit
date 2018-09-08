package develop.toolkit.struct;

import lombok.Getter;

/**
 * 不可逆密码基类
 *
 * @author qiushui on 2018-05-26.
 * @version 0.1
 */
public abstract class Password {

    @Getter
    protected String encryptPassword;

    public Password(String originalPassword) {
        this.encryptPassword = encrypt(originalPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj != null && obj instanceof Password) {
            return encryptPassword.equals(((Password) obj).getEncryptPassword());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash += encryptPassword.hashCode() * 31;
        return hash;
    }

    /**
     * 比较加密密码字符串
     * @param encryptPassword 加密密码字符串
     * @return
     */
    public boolean equalsEncryptPassword(String encryptPassword) {
        return this.encryptPassword.equals(encryptPassword);
    }

    @Override
    public String toString() {
        return encryptPassword;
    }

    /**
     * 加密算法
     * @param originalPassword 原文密码
     * @return 加密密码
     */
    public abstract String encrypt(String originalPassword);
}
