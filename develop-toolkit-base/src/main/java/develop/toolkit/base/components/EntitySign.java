package develop.toolkit.base.components;

/**
 * 实体记号接口
 *
 * @author qiushui on 2018-05-29.
 */
public interface EntitySign<K> {

    /**
     * 识别名
     *
     * @return 识别名
     */
    K key();
}
