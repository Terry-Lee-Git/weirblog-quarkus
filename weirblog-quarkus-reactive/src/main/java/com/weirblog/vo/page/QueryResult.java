package com.weirblog.vo.page;

import java.util.List;
/**
 * 查询结果集，包括数据和总数
 * @author db2admin
 *
 * @param <T>
 */
public class QueryResult<T> {
	/** 查询得出的数据List **/
	public List<T> resultList;
	/** 查询得出的总数 **/
	public Long totalRecord;

	public List<T> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<T> resultList)
	{
		this.resultList = resultList;
	}

	public Long getTotalRecord()
	{
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord)
	{
		this.totalRecord = totalRecord;
	}
}
