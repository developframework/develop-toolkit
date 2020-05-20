package develop.toolkit.db.mysql;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiushui on 2019-09-03.
 */
@Getter
public class MysqlProperties {

    private final String domain;

    private final int port;

    private final String username;

    private final String password;

    private final String database;

    private final Map<String, String> parameters = new HashMap<>();

    public MysqlProperties(String domain, int port, String username, String password, String database) {
        this.domain = domain;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        parameters.put("useSSL", "false");
        parameters.put("serverTimezone", "Asia/Shanghai");
    }

    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s?", domain, port, database) +
                parameters.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors.joining("&"));
    }
}
