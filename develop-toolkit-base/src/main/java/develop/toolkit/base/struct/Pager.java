package develop.toolkit.base.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 分页请求参数信息
 *
 * @author qiushui on 2018-06-07.
 * @since 0.1
 */
@AllArgsConstructor
public class Pager implements Serializable {

    private static final long serialVersionUID = -4527797845938921337L;

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

}
