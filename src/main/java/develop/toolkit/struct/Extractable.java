package develop.toolkit.struct;

/**
 * 可提取能力接口
 *
 * @author qiushui on 2018-05-29.
 * @since 0.1
 * @param <E> 提取条件类型
 */
public interface Extractable<E> {

    /**
     * 是否允许提取
     * @param parameter 条件参数
     * @return 是否允许
     */
    boolean accept(E parameter);

}
