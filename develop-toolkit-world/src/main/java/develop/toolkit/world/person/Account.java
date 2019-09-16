package develop.toolkit.world.person;

import develop.toolkit.world.normal.Password;

import java.io.Serializable;

/**
 * 账号
 *
 * @author qiushui on 2019-02-26.
 */
public interface Account extends Serializable {

    /**
     * 获得账号
     *
     * @return
     */
    String getAccount();

    /**
     * 获得密码
     *
     * @return
     */
    Password getPassword();
}
