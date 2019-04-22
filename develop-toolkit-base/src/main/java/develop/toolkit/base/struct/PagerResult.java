package develop.toolkit.base.struct;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页列表结果
 *
 * @author qiushui on 2018-06-07.
 */
@Getter
@NoArgsConstructor
public class PagerResult<T> implements Serializable {

    private static final long serialVersionUID = -3028130281925624773L;
	/* 数据列表 */
	private List<T> list;

	/* 记录总条数 */
    private long recordTotal;

	/* 分页信息 */
	private Pager pager;

	/* 页总数 */
	private long pageTotal;

    public PagerResult(Pager pager, List<T> list, long recordTotal) {
		this.list = list;
        this.recordTotal = recordTotal;
		this.pager = pager;
        this.pageTotal = recordTotal % pager.getSize() == 0 ? recordTotal / pager.getSize() : (recordTotal / pager.getSize() + 1L);
	}

	public PagerResult(int page, int size, List<T> list, long total) {
        this(new Pager(page, size), list, total);
	}

    /**
     * 空分页结果
     *
     * @return
     */
    public static <T> PagerResult<T> empty(Class<T> clazz, int page, int size) {
        return new PagerResult<>(page, size, List.of(), 0);
    }

    public static <T> PagerResult<T> empty(Class<T> clazz) {
        return new PagerResult<>(new Pager(), List.of(), 0);
    }
}
