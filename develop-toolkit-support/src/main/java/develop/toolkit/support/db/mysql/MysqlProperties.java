package develop.toolkit.support.db.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qiushui on 2019-09-03.
 */
@Getter
@AllArgsConstructor
public class MysqlProperties {

    private String domain;

    private int port;

    private String username;

    private String password;

    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/hclc_informationize?useSSL=false", domain, port);
    }
}
