package com.weirblog.vo.page;

import java.util.List;

/**
 * 分页数据包装，包括分页信息和List数据
 */
public class PageView<T> {
	/** 分页数据 **/
	public List<T> records;
	/** 页码开始索引和结束索引 **/
	public PageIndex pageIndex;
	/** 总页数 **/
	public int totalPage = 1;
	/** 每页显示记录数 **/
	public int maxResult = 10;
	/** 当前页 **/
	public int currentPage = 1;
	/** 总记录数 **/
	public Long totalRecord;
	/** 每次显示多少页，必须保证大于3页，保证左右链接都可以使用 **/
	public int viewPageCount = 10;

	/** 要获取记录的开始索引 **/
	public int getFirstResult() {
		return (this.currentPage - 1);
	}

	public int getViewPageCount() {
		return viewPageCount;
	}

	public void setViewPageCount(int viewPageCount) {
		this.viewPageCount = viewPageCount;
	}

	public PageView(int maxResult, int currentPage) {
		this.maxResult = maxResult;
		this.currentPage = (currentPage <= 0 ? 1 : currentPage);
	}

	public PageView(int currentPage) {
		this.currentPage = (currentPage <= 0 ? 1 : currentPage);
	}

	public void setQueryResult(QueryResult<T> qr) {
		setTotalRecord(qr.getTotalRecord());
		setRecords(qr.getResultList());
	}

	public Long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
		setTotalPage(this.totalRecord.intValue() % this.maxResult == 0 ? this.totalRecord.intValue()
				/ this.maxResult : this.totalRecord.intValue() / this.maxResult + 1);
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public PageIndex getPageIndex() {
		return pageIndex;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		this.pageIndex = PageIndex.getPageIndex(viewPageCount, currentPage,
				totalPage);
	}

	public int getMaxResult() {
		return maxResult;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setPageIndex(PageIndex pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "PageView [records=" + records + ", pageIndex=" + pageIndex + ", totalPage=" + totalPage + ", maxResult="
				+ maxResult + ", currentPage=" + currentPage + ", totalRecord=" + totalRecord + ", viewPageCount="
				+ viewPageCount + "]";
	}
}
