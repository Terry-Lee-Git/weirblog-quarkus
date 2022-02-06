package com.weirblog.vo.page;

import java.util.List;

/**
 * 查询结果集，包括数据和总数
 * @author db2admin
 *
 * @param <T>
 */
public class QueryResultVO<T> {
	/** 查询得出的数据List **/
	public List<T> resultList;
	/** 查询得出的总数 **/
	public Long totalRecord;
}
