package develop.toolkit.world.person;

import develop.toolkit.world.normal.Password;

import java.io.Serializable;

/**
 * 账号
 *
 * @author qiushui on 2019-02-26.
 */
@SuppressWarnings("unused")
public interface Account extends Serializable {

    /**
     * 获得账号
     */
    String getAccount();

    /**
     * 获得密码
     */
    Password getPassword();
}
