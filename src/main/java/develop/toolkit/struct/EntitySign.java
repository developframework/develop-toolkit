package develop.toolkit.struct;

/**
 * 实体记号接口
 *
 * @author qiushui on 2018-05-29.
 * @since 0.1
 */
public interface EntitySign<K> {

    /**
     * 识别名
     * @return 识别名
     */
    K key();
}
