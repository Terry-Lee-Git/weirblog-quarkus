package com.weirblog.vo;

import java.util.List;
import java.util.Map;

import com.weirblog.entity.Posts;
import com.weirblog.vo.page.PageView;

import io.quarkus.qute.TemplateData;

@TemplateData
public class IndexVo {

	private PageView<Posts> pageView;
	private String index;
	private List<Posts> rightNews;
	private List<Posts> rightReads;
	private Map<String, Long> mapType;
	
	@Override
	public String toString() {
		return "IndexVo [pageView=" + pageView + ", index=" + index + ", rightNews=" + rightNews + ", rightReads="
				+ rightReads + "]";
	}
	public PageView<Posts> getPageView() {
		return pageView;
	}
	public void setPageView(PageView<Posts> pageView) {
		this.pageView = pageView;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public List<Posts> getRightNews() {
		return rightNews;
	}
	public void setRightNews(List<Posts> rightNews) {
		this.rightNews = rightNews;
	}
	public List<Posts> getRightReads() {
		return rightReads;
	}
	public void setRightReads(List<Posts> rightReads) {
		this.rightReads = rightReads;
	}
	public Map<String, Long> getMapType() {
		return mapType;
	}
	public void setMapType(Map<String, Long> mapType) {
		this.mapType = mapType;
	}
}
