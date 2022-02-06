package com.weirblog.vo;

import java.util.List;

import com.weirblog.entity.Posts;

import io.quarkus.qute.TemplateData;

@TemplateData
public class PostView {

	public Posts posts;
	public List<Posts> rightNews;
	public List<Posts> rightReads;
	public Posts getPosts() {
		return posts;
	}
	public void setPosts(Posts posts) {
		this.posts = posts;
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
	
	
}
