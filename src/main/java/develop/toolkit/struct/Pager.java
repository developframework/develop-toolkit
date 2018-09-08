package develop.toolkit.struct;

import lombok.Getter;

/**
 * 分页请求参数信息
 *
 * @author qiushui on 2018-06-07.
 * @since 0.1
 */
public abstract class Pager {

	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_SIZE = 10;

	/* 页码 */
	@Getter
	protected int page;
	/* 页容量 */
	@Getter
	protected int size;

	public Pager() {
		this(DEFAULT_PAGE, DEFAULT_SIZE);
	}

	public Pager(int page, int size) {
		this.page = page;
		this.size = size;
	}
}
