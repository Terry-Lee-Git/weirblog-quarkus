package com.weirblog.vo.page;

import java.util.List;

import io.quarkus.qute.TemplateData;

/**
 * 分页数据包装，包括分页信息和List数据
 */
@TemplateData
public class PageView<T> {
	/** 分页数据 **/
	private List<T> records;
	/** 页码开始索引和结束索引 **/
	private PageIndex pageIndex;
	/** 总页数 **/
	private Integer totalPage = 1;
	/** 每页显示记录数 **/
	private Integer maxResult = 10;
	/** 当前页 **/
	private Integer currentPage = 1;
	/** 总记录数 **/
	private Integer totalRecord;
	/** 每次显示多少页，必须保证大于3页，保证左右链接都可以使用 **/
	private Integer viewPageCount = 10;

	/** 要获取记录的开始索引 **/
	public Integer getFirstResult() {
		return (this.currentPage - 1);
	}

	public Integer getViewPageCount() {
		return viewPageCount;
	}

	public void setViewPageCount(Integer viewPageCount) {
		this.viewPageCount = viewPageCount;
	}

	public PageView(Integer maxResult, Integer currentPage) {
		this.maxResult = maxResult;
		this.currentPage = (currentPage <= 0 ? 1 : currentPage);
	}

	public PageView(Integer currentPage) {
		this.currentPage = (currentPage <= 0 ? 1 : currentPage);
	}

	public void setQueryResult(QueryResult<T> qr) {
		setTotalRecord(qr.getTotalRecord());
		setRecords(qr.getResultList());
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
		setTotalPage(this.totalRecord % this.maxResult == 0 ? this.totalRecord
				/ this.maxResult : this.totalRecord / this.maxResult + 1);
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

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
		this.pageIndex = PageIndex.getPageIndex(viewPageCount, currentPage,
				totalPage);
	}

	public Integer getMaxResult() {
		return maxResult;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setPageIndex(PageIndex pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "PageView [records=" + records + ", pageIndex=" + pageIndex + ", totalPage=" + totalPage + ", maxResult="
				+ maxResult + ", currentPage=" + currentPage + ", totalRecord=" + totalRecord + ", viewPageCount="
				+ viewPageCount + "]";
	}
}
