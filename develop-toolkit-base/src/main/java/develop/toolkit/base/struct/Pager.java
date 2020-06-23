package develop.toolkit.base.struct;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页请求参数信息
 *
 * @author qiushui on 2018-06-07.
 */
@Getter
public class Pager implements Serializable {

	private static final long serialVersionUID = -4527797845938921337L;

	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_SIZE = 20;

	/* 页码 */
	protected int page;
	/* 页容量 */
	protected int size;

	/* 记录总条数 */
	@Setter
	private long recordTotal;

	/* 页总数 */
	@Setter
	private long pageTotal;

	public Pager() {
		this(DEFAULT_PAGE, DEFAULT_SIZE);
	}

	public Pager(int page, int size) {
		this.page = page;
		this.size = size;
	}

	@SuppressWarnings("unused")
	public int getOffset() {
		return page * size;
	}
}
