package com.hpe.cmwa.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Pager {

	// 总页数
	private int					totalPages		= 0;

	// 总行数
	private int					totalRecords	= 0;

	// 当前页号
	private int					curPage			= 1;

	// 每页显示的行数
	private int					pageSize		= 10;

	// 其他的参数我们把它分装成一个Map对象
	private Map<String, Object>	params			= new HashMap<String, Object>();
	
	//多个参数map对象
	List<Map<String, Object>> paramsList = new ArrayList<Map<String,Object>>();	

	public List<Map<String, Object>> getParamsList() {
		return paramsList;
	}

	public void setParamsList(List<Map<String, Object>> paramsList) {
		this.paramsList = paramsList;
	}

	// 结果集数据行
	private List				dataRows		= new ArrayList();

	// 排序字段
	private String				orderBy;
	// 排序方式（升序asc或降序desc）
	private String				order;

	private Map<String, String>	extraInfos		= new HashMap<String, String>();

	public Pager() {
		
	}

	public Pager(int pageSize) {
		this.pageSize = pageSize;
	}

	public Pager(int totalRows, int pageSize) {

		this.totalRecords = totalRows;
		this.pageSize = pageSize;
		totalPages = totalRows / pageSize;
		int mod = totalRows % pageSize;
		if (mod > 0) {
			totalPages++;
		}
		this.totalPages = 1;
	}

	/**
	 * 设定起始行数
	 * @param currentPage
	 */
	public void setStart(int curPage) {
		this.curPage = curPage;
	}

	public int getRecords() {
		return totalRecords;
	}

	public void setRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		this.totalPages = totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List getDataRows() {
		return dataRows;
	}

	public void setDataRows(List dataRows) {
		this.dataRows = dataRows;
	}

	public Map<String, Object> getParams() {
		return this.params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void addParameter(String key, Object value) {
		if (this.params == null) {
			this.params = new HashMap<String, Object>();
		}
		this.params.put(key, value);
	}

	public int getTotalPages() {
		return this.totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurPage() {
		return this.curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return this.order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Map<String, String> getExtraInfos() {
		return this.extraInfos;
	}

	public void setExtraInfos(Map<String, String> extraInfos) {
		this.extraInfos = extraInfos;
	}

}
