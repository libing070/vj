package com.hpe.cmca.web.taglib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpe.cmca.util.JacksonJsonUtil;

@SuppressWarnings("rawtypes")
public class Pager {

	// 是否返回所有数据，如导出时可采用相同的方法处理，只需将returnAll设为true即可
	private boolean				returnAll		= false;
	// selectId对应myBatis Mapper文件中的select Id
	private String				selectId		= "";
	//有价卡的关注点，1001到1009的关注点都是用1001的XML文件			
	private String focuscd_ = "";

	public String getFocuscd_() {
		return focuscd_;
	}

	public void setFocuscd_(String focuscd_) {
		this.focuscd_ = focuscd_;
	}

	// 总页数
	private int					total		= 0;
	// 总行数
	private int					records		= 0;
	// 当前页号
	private int					page		= 1;
	public static final int		PAGE		= 1;
	// 当前页在数据库中的起始行
	private int					startRow	= 0;
	public static final int		STARTROW	= 0;
	// 每页显示的行数
	private int					pageSize	= 15;
	public static final int		PAGESIZE	= 15;
	
	private String				orderBy			= "1";
	private String				orderDirection	= "";
	
	
	private String				sidx	= "";
	private String				sord	= "";
	
	

	/**
	 * @return the sidx
	 */
	public String getSidx() {
		return this.sidx;
	}

	/**
	 * @param sidx the sidx to set
	 */
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	/**
	 * @return the sord
	 */
	public String getSord() {
		return this.sord;
	}

	/**
	 * @param sord the sord to set
	 */
	public void setSord(String sord) {
		this.sord = sord;
	}

	// 其他的参数我们把它分装成一个Map对象
	private Map<String, Object>	params			= new HashMap<String, Object>();
	/**
	 * 结果集
	 */

	private List				rows		= new ArrayList();
	private String 				resultJson = "{}";

	public Pager() {
	}

	public Pager(int pageSize) {
		this.pageSize = pageSize;
	}

	public Pager(int totalRows, int pageSize) {

		this.records = totalRows;
		this.pageSize = pageSize;
		total = totalRows / pageSize;
		int mod = totalRows % pageSize;
		if (mod > 0) {
			total++;
		}
		this.total = 1;
		this.startRow = 0;
	}

	public boolean isReturnAll() {
		return this.returnAll;
	}

	
	public void setReturnAll(boolean returnAll) {
		this.returnAll = returnAll;
	}
	
	
	public String getSelectId() {
		return this.selectId;
	}

	
	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	/**
	 * 设定起始行数
	 * @param currentPage
	 */
	public void setStart(int page) {
		this.page = page;
		startRow = (page - 1) * pageSize;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
		this.total = records % pageSize == 0 ? records / pageSize : records / pageSize + 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public List getRows() {
		return rows;
	}

	public void setResultList(List rows) {
		this.rows = rows;
		this.resultJson = JacksonJsonUtil.beanToJson(rows);
	}
	
	public String getResultJson() {
		return this.resultJson;
	}
	
	public void setResultJson(String resultJson) {
		this.resultJson = resultJson;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDirection() {
		return this.orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
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

	// 20161213 add by GuoXY 恢复默认值 
	public void clear() {
		this.setTotal(0);
		this.setRecords(0);
		this.setPage(1);
		this.setStartRow(1);
	}
}
