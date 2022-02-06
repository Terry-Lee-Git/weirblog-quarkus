package com.weirblog.vo;

import java.util.ArrayList;
import java.util.List;

public class DataGrid<T> {

	public Long total = 0L;
	public List<T> rows = new ArrayList<T>();

	public DataGrid() {
		super();
	}

	public DataGrid(Long total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

}
