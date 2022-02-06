package com.weirblog.vo;

import javax.persistence.Transient;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.qute.TemplateData;

@TemplateData
public class BaseVo extends PanacheEntityBase {

	@Transient
	public Integer page = 0;
	@Transient
	public Integer rows = 10;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "BaseVo [page=" + page + ", rows=" + rows + "]";
	}
}
