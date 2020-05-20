package develop.toolkit.db.mysql;

import develop.toolkit.base.utils.IOAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiushui on 2019-09-18.
 */
public class SQLFactory {

    private Map<String, String> sqlMap = new HashMap<>();

    public SQLFactory(String... sqlFiles) {
        for (String sqlFile : sqlFiles) {
            parseSqlFile(sqlFile);
        }
    }

    private void parseSqlFile(String sqlFile) {
        List<String> lines = IOAdvice.readLinesFromClasspath(sqlFile).collect(Collectors.toList());
        String key = null;
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (line.startsWith("#")) {
                putSql(key, sb);
                key = line.substring(1).trim();
            } else {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(line.trim());
            }
        }
        putSql(key, sb);
    }

    private void putSql(String key, StringBuilder sb) {
        if (key != null && sb.length() > 0) {
            if (sqlMap.containsKey(key)) {
                throw new RuntimeException("sql map exists \"" + key + "\"");
            }
            sqlMap.put(key, sb.toString());
            sb.setLength(0);
        }
    }

    public String getSql(String key) {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new RuntimeException("sql map not exists \"" + key + "\"");
        }
        return sql;
    }
}
