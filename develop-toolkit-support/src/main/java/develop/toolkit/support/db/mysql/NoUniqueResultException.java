package develop.toolkit.support.db.mysql;

/**
 * @author qiushui on 2019-09-03.
 */
public class NoUniqueResultException extends RuntimeException {

    public NoUniqueResultException(int size) {
        super("no unique result: " + size);
    }
}
