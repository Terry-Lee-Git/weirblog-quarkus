package com.weirblog.vo;

import java.util.List;

public class DataGrid<T> {

	public Long total;
	public List<T> rows;

	public DataGrid() {
	}

	public DataGrid(Long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}

}
