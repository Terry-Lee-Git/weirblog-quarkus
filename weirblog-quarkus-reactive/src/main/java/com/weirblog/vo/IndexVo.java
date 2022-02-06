package com.weirblog.vo;

import java.util.List;

import com.weirblog.entity.Posts;
import com.weirblog.vo.page.PageView;

public class IndexVo {

	public PageView<Posts> pageView;
	public String index;
	public List<Posts> rightNews;
	public List<Posts> rightReads;
	@Override
	public String toString() {
		return "IndexVo [pageView=" + pageView + ", index=" + index + ", rightNews=" + rightNews + ", rightReads="
				+ rightReads + "]";
	}
}
