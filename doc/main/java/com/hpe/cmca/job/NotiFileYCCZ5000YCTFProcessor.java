package com.hpe.cmca.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

@Service("NotiFileYCCZ5000YCTFProcessor")
public class NotiFileYCCZ5000YCTFProcessor extends AbstractNotiFileProcessor {

    private String	    month;

    private String	    prvdId = "10000";

    private String		prvdName="全公司";
    
    private SXSSFWorkbook wb;
    
    private ArrayList<String> strs   = new ArrayList<String>();

    public void setMonth(String month) {
	this.month = month;
    }

    public void setPrvdId(String prvdId) {
	this.prvdId = prvdId;
    }

    // 重写构建文件名方法
    protected String buildFileName() {
	fileName = fileName + ".xlsx";
	return fileName;
    }

    public ArrayList<String> getFileNameList() {
	return strs;
    }
    private Map<String,CellStyle> mapStyle=new HashMap<String,CellStyle>();
    @Override
    public void start() throws Exception {
	FileOutputStream out = null;
	// List<Map<String, Object>> prvdList = null;
	List<Map<String, Object>> prvdList = notiFileGenService.getPrvd();
	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, prvdSize = prvdList.size(); i < prvdSize; i++) {
	    row = prvdList.get(i);
	    if (row.size() != 0) {
		prvdId = row.get("CMCC_prov_prvd_cd").toString();
		prvdName=row.get("CMCC_prov_prvd_nm").toString();
		wb = new SXSSFWorkbook();
		mapStyle.put("style0", getStyle0());
		mapStyle.put("style1", getStyle1());
		mapStyle.put("style3", getStyle3());
		mapStyle.put("style5", getStyle5());
		mapStyle.put("style6", getStyle6());
		this.generate();
		out = null;
		try {
		    out = new FileOutputStream(this.getLocalPath());
		    wb.write(out);
		    strs.add(fileName);
		} catch (IOException e) {
		    throw e;
		} catch (Exception e) {
		    throw e;
		} finally {
		    try {
			out.close();
		    } catch (IOException e) {
			throw e;
		    }
		}
	    }
	}
    }

    @Override
    public boolean generate() throws Exception {

	// 根据省份及月份对文件夹名命名
	if (this.prvdId.equals("10000")) {
	    this.setFileName("员工异常操作异常退费排名汇总_" + month);
	    // 创建第一个sheet
	    sh = wb.createSheet("表1 异常退费金额全国排名前100账户");
	    // getNotiFile5000YCJFZS()根据月份month、省份prvdId、类型Top50获取后台数据。
	    writeFirstPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "Top50"), sh, prvdId, month);

	    // 创建第二个sheet
	    sh = wb.createSheet("表2 异常退费金额排名前100账户明细");
	    writeDetailsPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "Details"), sh, prvdId, month);

	    // 创建第二个sheet
	    sh = wb.createSheet("表3 异常退费金额省排名情况");
	    writeSecondPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "COMTop50"), sh, prvdId, month);

	    // 创建第三个sheet
	    sh = wb.createSheet("表4 异常退费金额排名前100账户涉及操作工号");
	    // sh = wb.createSheet("各省疑似养卡号码数量排名前十渠道_" + month);
	    writeThirdPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "Top50ReleseStaff"), sh, prvdId, month);

	    // 创建第四个sheet
	    sh = wb.createSheet("表5 异常退费排名前100操作工号");
	    writeFourthPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "StaffTop50"), sh, prvdId, month);

	} else {
	    String prvdName = notiFileGenService.getPrvdName(prvdId).get(0).get("prvdName").toString();
	    this.setFileName("员工异常操作异常退费排名汇总_" + prvdName + "_" + month);
	    // 创建第一个sheet
	    sh = wb.createSheet("表1 异常退费金额全国排名前100账户");
	    // getNotiFile5000YCJFZS()根据月份month、省份prvdId、类型Top50获取后台数据。
	    writeFirstPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "Top50"), sh, prvdId, month);

	    // 创建第二个sheet
	    sh = wb.createSheet("表2 异常退费金额省排名情况");
	    writeSecondPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "COMTop50"), sh, prvdId, month);

	    // 创建第三个sheet
	    sh = wb.createSheet("表3 异常退费金额排名前100账户涉及操作工号");
	    // sh = wb.createSheet("各省疑似养卡号码数量排名前十渠道_" + month);
	    writeThirdPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "Top50ReleseStaff"), sh, prvdId, month);

	    // 创建第四个sheet
	    sh = wb.createSheet("表4 异常退费排名前100操作工号");
	    writeFourthPart(notiFileGenService.getNotiFile5000YCTF(month, prvdId, "StaffTop50"), sh, prvdId, month);

	}

	return true;
    }

    /**
     * 
     * <pre>
     * Desc  将获取到的数据插入到excel‘员工异常操作异常退费排名汇总’下的sheet1中
     * @param data：获取的数据
     * @param sh：应插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:31:13 PM
     * </pre>
     */
    public void writeFirstPart(List<Map<String, Object>> data, Sheet sh, String prvdId, String month) {
	sh.createRow(0);
	sh.getRow(0).createCell(0).setCellValue("表1 异常退费金额排名前100账户");
	sh.createRow(1);
	sh.getRow(1).createCell(0).setCellValue("审计月");
	sh.getRow(1).createCell(1).setCellValue("退费金额全国排名");
	sh.getRow(1).createCell(2).setCellValue("退费金额省份排名");
	sh.getRow(1).createCell(3).setCellValue("省份");
	sh.getRow(1).createCell(4).setCellValue("地市");
	sh.getRow(1).createCell(5).setCellValue("账户标识");
	sh.getRow(1).createCell(6).setCellValue("退费金额（元）");
	sh.getRow(1).createCell(7).setCellValue("退费金额（万元）");
	sh.getRow(1).createCell(8).setCellValue("退费次数");
	sh.getRow(1).createCell(9).setCellValue("自有渠道退费金额(元)");
	sh.getRow(1).createCell(10).setCellValue("社会渠道退费金额(元)");
	sh.getRow(1).createCell(11).setCellValue("退费用户数");
	sh.getRow(1).createCell(12).setCellValue("退费手机号数");
	sh.getRow(1).createCell(13).setCellValue("操作工号数");
	sh.getRow(1).createCell(14).setCellValue("办理业务渠道数");
	sh.getRow(1).createCell(15).setCellValue("办理业务自有渠道数");
	sh.getRow(1).createCell(16).setCellValue("办理业务社会渠道数");
	sh.getRow(1).createCell(17).setCellValue("集团客户标识");
	sh.getRow(1).createCell(18).setCellValue("个人客户标识");
	// 欠费金额,欠费账龄,欠费月数-dbt_amt,aging,dbt_mons --by hufei
	sh.getRow(1).createCell(19).setCellValue("账户名称");
	sh.getRow(1).createCell(20).setCellValue("欠费金额（元）");
	sh.getRow(1).createCell(21).setCellValue("欠费账龄");
	sh.getRow(1).createCell(22).setCellValue("欠费月数");
	sh.getRow(1).createCell(23).setCellValue("省份代码");
	sh.getRow(1).createCell(24).setCellValue("T-1月出账金额（元）");
	sh.getRow(1).createCell(25).setCellValue("T-2月出账金额（元）");
	sh.getRow(1).createCell(26).setCellValue("T-3月出账金额（元）");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 26));

	for (int j = 0; j <= 26; j++) {
	    if (null == sh.getRow(1).getCell(j)) {
		sh.getRow(1).createCell(j);
	    }
	    sh.getRow(1).getCell(j).setCellStyle(mapStyle.get("style1"));
	}

	for (int i = 0; i <= 26; i++) {
		if(i==24 || i==25 || i==26){
			sh.setColumnWidth(i, 256 * 30);
		}
		else if (i >= 6 && i <= 10) {
		sh.setColumnWidth(i, 256 * 16);
	    } else {
		sh.setColumnWidth(i, 256 * 10);
	    }
	}

	// sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(14, 2, 0, 0);

	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 新增数据行
	    sh.createRow(2 + i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		if (row.get("Aud_trm") != null) {
		    sh.getRow(2 + i).createCell(0).setCellValue(row.get("Aud_trm").toString());
		} else {
		    sh.getRow(2 + i).createCell(0).setCellValue("-");
		}
		if (row.get("china_no") != null) {
		    sh.getRow(2 + i).createCell(1).setCellValue(row.get("china_no").toString());
		} else {
		    sh.getRow(2 + i).createCell(1).setCellValue("-");
		}
		if (row.get("prvd_no") != null) {
		    sh.getRow(2 + i).createCell(2).setCellValue(row.get("prvd_no").toString());
		} else {
		    sh.getRow(2 + i).createCell(2).setCellValue("-");
		}
		if (row.get("prvd_name") != null) {
		    sh.getRow(2 + i).createCell(3).setCellValue(row.get("prvd_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(3).setCellValue("-");
		}
		if (row.get("city_name") != null) {
		    sh.getRow(2 + i).createCell(4).setCellValue(row.get("city_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(4).setCellValue("-");
		}
		if (row.get("acct_id") != null) {
		    sh.getRow(2 + i).createCell(5).setCellValue(row.get("acct_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(5).setCellValue("");
		}
		if (row.get("sum_amt") != null) {
		    sh.getRow(2 + i).createCell(6).setCellValue(row.get("sum_amt") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt")).doubleValue() : (Long) row.get("sum_amt"));
		} else {
		    sh.getRow(2 + i).createCell(6).setCellValue("-");
		}
		if (row.get("total_Amt_10000") != null) {
		    sh.getRow(2 + i).createCell(7)
			    .setCellValue(row.get("total_Amt_10000") instanceof BigDecimal ? ((BigDecimal) row.get("total_Amt_10000")).doubleValue() : (Long) row.get("total_Amt_10000"));
		} else {
		    sh.getRow(2 + i).createCell(7).setCellValue("-");
		}
		if (row.get("cnt_num") != null) {
		    sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("cnt_num"));
		} else {
		    sh.getRow(2 + i).createCell(8).setCellValue("-");
		}
		if (row.get("sum_amt_self") != null) {
		    sh.getRow(2 + i).createCell(9).setCellValue(row.get("sum_amt_self") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_self")).doubleValue() : (Long) row.get("sum_amt_self"));
		} else {
		    sh.getRow(2 + i).createCell(9).setCellValue("");
		}
		if (row.get("sum_amt_shehui") != null) {
		    sh.getRow(2 + i).createCell(10)
			    .setCellValue(row.get("sum_amt_shehui") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_shehui")).doubleValue() : (Long) row.get("sum_amt_shehui"));
		} else {
		    sh.getRow(2 + i).createCell(10).setCellValue("");
		}
		if (row.get("cnt_subs") != null) {
		    sh.getRow(2 + i).createCell(11).setCellValue((Integer) row.get("cnt_subs"));
		} else {
		    sh.getRow(2 + i).createCell(11).setCellValue("-");
		}
		if (row.get("cnt_msisdn") != null) {
		    sh.getRow(2 + i).createCell(12).setCellValue((Integer) row.get("cnt_msisdn"));
		} else {
		    sh.getRow(2 + i).createCell(12).setCellValue("-");
		}
		if (row.get("cnt_opr_id") != null) {
		    sh.getRow(2 + i).createCell(13).setCellValue((Integer) row.get("cnt_opr_id"));
		} else {
		    sh.getRow(2 + i).createCell(13).setCellValue("-");
		}
		if (row.get("cnt_chnl") != null) {
		    sh.getRow(2 + i).createCell(14).setCellValue((Integer) row.get("cnt_chnl"));
		} else {
		    sh.getRow(2 + i).createCell(14).setCellValue("-");
		}
		if (row.get("cnt_self") != null) {
		    sh.getRow(2 + i).createCell(15).setCellValue((Integer) row.get("cnt_self"));
		} else {
		    sh.getRow(2 + i).createCell(15).setCellValue("-");
		}
		if (row.get("cnt_shehui") != null) {
		    sh.getRow(2 + i).createCell(16).setCellValue((Integer) row.get("cnt_shehui"));
		} else {
		    sh.getRow(2 + i).createCell(16).setCellValue("-");
		}
		if (row.get("org_cust_id") != null) {
		    sh.getRow(2 + i).createCell(17).setCellValue(row.get("org_cust_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(17).setCellValue("");
		}
		if (row.get("indvl_cust_id") != null) {
		    sh.getRow(2 + i).createCell(18).setCellValue(row.get("indvl_cust_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(18).setCellValue("");
		}
		if (row.get("acct_nm") != null) {
		    sh.getRow(2 + i).createCell(19).setCellValue(row.get("acct_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(19).setCellValue("");
		}
		// 欠费金额,欠费账龄,欠费月数-dbt_amt,aging,dbt_mons --by hufei
		if (row.get("dbt_amt") != null) {
		    sh.getRow(2 + i).createCell(20).setCellValue(row.get("dbt_amt") instanceof BigDecimal ? ((BigDecimal) row.get("dbt_amt")).doubleValue() : (Long) row.get("dbt_amt"));
		} else {
		    sh.getRow(2 + i).createCell(20).setCellValue("");
		}
		if (row.get("aging") != null) {
		    sh.getRow(2 + i).createCell(21).setCellValue((Integer) row.get("aging"));
		} else {
		    sh.getRow(2 + i).createCell(21).setCellValue("");
		}
		if (row.get("dbt_mons") != null) {
		    sh.getRow(2 + i).createCell(22).setCellValue((Integer) row.get("dbt_mons"));
		} else {
		    sh.getRow(2 + i).createCell(22).setCellValue("");
		}
		if (prvdId.equals("10000")) {
		    sh.getRow(2 + i).createCell(23).setCellValue(row.get("prvd_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(23).setCellValue(row.get("city_id").toString());
		}
		//out_amt_t1,out_amt_t2,out_amt_t3, V1.9新增
		if (row.get("out_amt_t1") != null) {
		    sh.getRow(2 + i).createCell(24).setCellValue(row.get("out_amt_t1") instanceof BigDecimal ? ((BigDecimal) row.get("out_amt_t1")).doubleValue() : (Long) row.get("out_amt_t1"));
		} else {
		    sh.getRow(2 + i).createCell(24).setCellValue("");
		}
		if (row.get("out_amt_t2") != null) {
		    sh.getRow(2 + i).createCell(25).setCellValue(row.get("out_amt_t2") instanceof BigDecimal ? ((BigDecimal) row.get("out_amt_t2")).doubleValue() : (Long) row.get("out_amt_t2"));
		} else {
		    sh.getRow(2 + i).createCell(25).setCellValue("");
		}
		if (row.get("out_amt_t3") != null) {
		    sh.getRow(2 + i).createCell(26).setCellValue(row.get("out_amt_t3") instanceof BigDecimal ? ((BigDecimal) row.get("out_amt_t3")).doubleValue() : (Long) row.get("out_amt_t3"));
		} else {
		    sh.getRow(2 + i).createCell(26).setCellValue("");
		}
	    }
	    // 设置单元格格式
	    // 退费金额价值金额（元）、退费金额价值金额（万元）格式保留两位小数
	    sh.getRow(2 + i).getCell(6).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(7).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(9).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(10).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(20).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(24).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(25).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(26).setCellStyle(mapStyle.get("style5"));
	    // 退费金额、退费次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
	    sh.getRow(2 + i).getCell(8).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(11).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(12).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(13).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(14).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(15).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(16).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(21).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(22).setCellStyle(mapStyle.get("style6"));

	    sh.getRow(2 + i).getCell(0).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(1).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(2).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(3).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(4).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(5).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(17).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(18).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(19).setCellStyle(mapStyle.get("style3"));

	    sh.getRow(2 + i).getCell(23).setCellStyle(mapStyle.get("style3"));
	}

    }

    /**
     * 
     * <pre>
     * Desc  将获取到的数据插入到sheet中
     * @param data：获取的数据
     * @param sh：即将插入的sheet
     * @param prvdId：省份
     * @param month：月份
     * @author feihu
     * Mar 29, 2017 3:36:35 PM
     * </pre>
     */
    public void writeSecondPart(List<Map<String, Object>> data, Sheet sh, String prvdId, String month) {
	sh.createRow(0);
	if (prvdId.equals("10000")) {
	    sh.getRow(0).createCell(0).setCellValue("表3 异常退费金额公司排名");
	} else {
	    sh.getRow(0).createCell(0).setCellValue("表2 异常退费金额公司排名");
	}

	sh.createRow(1);
	sh.getRow(1).createCell(0).setCellValue("审计月");
	sh.getRow(1).createCell(1).setCellValue("退费金额排名");
	sh.getRow(1).createCell(2).setCellValue("省份");
	sh.getRow(1).createCell(3).setCellValue("违规地市数");
	sh.getRow(1).createCell(4).setCellValue("退费金额（元）");
	sh.getRow(1).createCell(5).setCellValue("退费金额（万元）");
	sh.getRow(1).createCell(6).setCellValue("退费次数");
	sh.getRow(1).createCell(7).setCellValue("自有渠道退费金额(元)");
	sh.getRow(1).createCell(8).setCellValue("社会渠道退费金额(元)");
	sh.getRow(1).createCell(9).setCellValue("退费账户数");
	sh.getRow(1).createCell(10).setCellValue("退费用户数");
	sh.getRow(1).createCell(11).setCellValue("退费手机号数");
	sh.getRow(1).createCell(12).setCellValue("操作工号数");
	sh.getRow(1).createCell(13).setCellValue("办理业务渠道数量");
	sh.getRow(1).createCell(14).setCellValue("办理业务自有渠道数");
	sh.getRow(1).createCell(15).setCellValue("办理业务社会渠道数");
	sh.getRow(1).createCell(16).setCellValue("操作员工归属渠道数量");
	sh.getRow(1).createCell(17).setCellValue("退费集团客户数");
	sh.getRow(1).createCell(18).setCellValue("退费个人客户数");
	sh.getRow(1).createCell(19).setCellValue("省份代码");

	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));

	for (int j = 0; j <= 19; j++) {
	    if (null == sh.getRow(1).getCell(j)) {
		sh.getRow(1).createCell(j);
	    }
	    sh.getRow(1).getCell(j).setCellStyle(mapStyle.get("style1"));
	}

	for (int i = 0; i <= 19; i++) {
	    if (i <= 8 && i >= 4) {
		sh.setColumnWidth(i, 256 * 16);
	    } else {
		sh.setColumnWidth(i, 256 * 10);
	    }
	}

	sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(0, 1, 0, 13);

	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 新增数据行
	    sh.createRow(2 + i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		if (row.get("cnt_area") != null) {
		    if (row.get("aud_trm") != null) {
			sh.getRow(2 + i).createCell(0).setCellValue(row.get("aud_trm").toString());
		    } else {
			sh.getRow(2 + i).createCell(0).setCellValue("-");
		    }
		    if (row.get("china_no") != null) {
			sh.getRow(2 + i).createCell(1).setCellValue(row.get("china_no").toString());
		    } else {
			sh.getRow(2 + i).createCell(1).setCellValue("-");
		    }
		    if (row.get("prvd_name") != null) {
			sh.getRow(2 + i).createCell(2).setCellValue(row.get("prvd_name").toString());
		    } else {
			sh.getRow(2 + i).createCell(2).setCellValue("-");
		    }
		    if (row.get("cnt_area") != null) {
			sh.getRow(2 + i).createCell(3).setCellValue((Integer) row.get("cnt_area"));
		    } else {
			sh.getRow(2 + i).createCell(3).setCellValue("-");
		    }
		    if (row.get("sum_amt") != null) {
			sh.getRow(2 + i).createCell(4).setCellValue(row.get("sum_amt") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt")).doubleValue() : (Long) row.get("sum_amt"));
		    } else {
			sh.getRow(2 + i).createCell(4).setCellValue("-");
		    }
		    if (row.get("total_Amt_10000") != null) {
			sh.getRow(2 + i).createCell(5)
				.setCellValue(row.get("total_Amt_10000") instanceof BigDecimal ? ((BigDecimal) row.get("total_Amt_10000")).doubleValue() : (Long) row.get("total_Amt_10000"));
		    } else {
			sh.getRow(2 + i).createCell(5).setCellValue("-");
		    }
		    if (row.get("cnt_num") != null) {
			sh.getRow(2 + i).createCell(6).setCellValue((Integer) row.get("cnt_num"));
		    } else {
			sh.getRow(2 + i).createCell(6).setCellValue("-");
		    }
		    if (row.get("sum_amt_self") != null) {
			sh.getRow(2 + i).createCell(7)
				.setCellValue(row.get("sum_amt_self") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_self")).doubleValue() : (Long) row.get("sum_amt_self"));
		    } else {
			sh.getRow(2 + i).createCell(7).setCellValue("");
		    }
		    if (row.get("sum_amt_shehui") != null) {
			sh.getRow(2 + i).createCell(8)
				.setCellValue(row.get("sum_amt_shehui") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_shehui")).doubleValue() : (Long) row.get("sum_amt_shehui"));
		    } else {
			sh.getRow(2 + i).createCell(8).setCellValue("");
		    }
		    if (row.get("cnt_acct") != null) {
			sh.getRow(2 + i).createCell(9).setCellValue((Integer) row.get("cnt_acct"));
		    } else {
			sh.getRow(2 + i).createCell(9).setCellValue("-");
		    }
		    if (row.get("cnt_subs") != null) {
			sh.getRow(2 + i).createCell(10).setCellValue((Integer) row.get("cnt_subs"));
		    } else {
			sh.getRow(2 + i).createCell(10).setCellValue("-");
		    }
		    if (row.get("cnt_msisdn") != null) {
			sh.getRow(2 + i).createCell(11).setCellValue((Integer) row.get("cnt_msisdn"));
		    } else {
			sh.getRow(2 + i).createCell(11).setCellValue("-");
		    }
		    if (row.get("cnt_opr_id") != null) {
			sh.getRow(2 + i).createCell(12).setCellValue((Integer) row.get("cnt_opr_id"));
		    } else {
			sh.getRow(2 + i).createCell(12).setCellValue("-");
		    }
		    if (row.get("cnt_chnl") != null) {
			sh.getRow(2 + i).createCell(13).setCellValue((Integer) row.get("cnt_chnl"));
		    } else {
			sh.getRow(2 + i).createCell(13).setCellValue("-");
		    }
		    if (row.get("cnt_self") != null) {
			sh.getRow(2 + i).createCell(14).setCellValue((Integer) row.get("cnt_self"));
		    } else {
			sh.getRow(2 + i).createCell(14).setCellValue("-");
		    }
		    if (row.get("cnt_shehui") != null) {
			sh.getRow(2 + i).createCell(15).setCellValue((Integer) row.get("cnt_shehui"));
		    } else {
			sh.getRow(2 + i).createCell(15).setCellValue("-");
		    }
		    if (row.get("cnt_staff_chnl") != null) {
			sh.getRow(2 + i).createCell(16).setCellValue((Integer) row.get("cnt_staff_chnl"));
		    } else {
			sh.getRow(2 + i).createCell(16).setCellValue("-");
		    }
		    if (row.get("total_Org") != null) {
			sh.getRow(2 + i).createCell(17).setCellValue((Integer) row.get("total_Org"));
		    } else {
			sh.getRow(2 + i).createCell(17).setCellValue("-");
		    }
		    if (row.get("total_Indvl") != null) {
			sh.getRow(2 + i).createCell(18).setCellValue((Integer) row.get("total_Indvl"));
		    } else {
			sh.getRow(2 + i).createCell(18).setCellValue("-");
		    }
		    if (row.get("prvd_id") != null) {
			sh.getRow(2 + i).createCell(19).setCellValue(row.get("prvd_id").toString());
		    } else {
			sh.getRow(2 + i).createCell(19).setCellValue("-");
		    }
		} else {
		    sh.getRow(2 + i).createCell(0).setCellValue(month);
		    sh.getRow(2 + i).createCell(1).setCellValue("-");
		    if (row.get("prvd_name") != null) {
			sh.getRow(2 + i).createCell(2).setCellValue(row.get("prvd_name").toString());
		    } else {
			sh.getRow(2 + i).createCell(2).setCellValue("-");
		    }
		    for (int j = 3; j < 19; j++) {
			sh.getRow(2 + i).createCell(j).setCellValue("-");
		    }
		    if (row.get("prvd_id") != null) {
			sh.getRow(2 + i).createCell(19).setCellValue(row.get("prvd_id").toString());
		    } else {
			sh.getRow(2 + i).createCell(19).setCellValue("-");
		    }
		}
	    }
	    // 设置单元格格式
	    // 退费金额价值金额（元）、退费金额价值金额（万元）格式保留两位小数
	    sh.getRow(2 + i).getCell(4).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(5).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(7).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(8).setCellStyle(mapStyle.get("style5"));
	    // 退费金额、退费次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
	    sh.getRow(2 + i).getCell(3).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(6).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(9).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(10).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(11).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(12).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(13).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(14).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(15).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(16).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(17).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(18).setCellStyle(mapStyle.get("style6"));

	    sh.getRow(2 + i).getCell(0).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(1).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(2).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(19).setCellStyle(mapStyle.get("style3"));
	}
    }

    public void writeThirdPart(List<Map<String, Object>> data, Sheet sh, String prvdId, String month) {
	sh.createRow(0);
	if (prvdId.equals("10000")) {
	    sh.getRow(0).createCell(0).setCellValue("表4 异常退费金额排名前100账户涉及操作工号");
	} else {
	    sh.getRow(0).createCell(0).setCellValue("表3 异常退费金额排名前100账户涉及操作工号");
	}

	sh.createRow(1);
	sh.getRow(1).createCell(0).setCellValue("审计月");
	sh.getRow(1).createCell(1).setCellValue("退费金额全国排名");
	sh.getRow(1).createCell(2).setCellValue("退费金额省份排名");
	sh.getRow(1).createCell(3).setCellValue("省份");
	sh.getRow(1).createCell(4).setCellValue("地市");
	sh.getRow(1).createCell(5).setCellValue("操作员工标识");
	sh.getRow(1).createCell(6).setCellValue("退费金额（元）");
	sh.getRow(1).createCell(7).setCellValue("退费金额（万元）");
	sh.getRow(1).createCell(8).setCellValue("退费次数");
	sh.getRow(1).createCell(9).setCellValue("退费账户数");
	sh.getRow(1).createCell(10).setCellValue("退费用户数");
	sh.getRow(1).createCell(11).setCellValue("退费手机号数");
	sh.getRow(1).createCell(12).setCellValue("办理业务渠道数");
	sh.getRow(1).createCell(13).setCellValue("办理业务自有渠道数");
	sh.getRow(1).createCell(14).setCellValue("办理业务社会渠道数");
	sh.getRow(1).createCell(15).setCellValue("自有渠道退费金额(元)");
	sh.getRow(1).createCell(16).setCellValue("社会渠道退费金额(元)");
	sh.getRow(1).createCell(17).setCellValue("省份代码");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));

	for (int j = 0; j <= 17; j++) {
	    if (null == sh.getRow(1).getCell(j)) {
		sh.getRow(1).createCell(j);
	    }
	    sh.getRow(1).getCell(j).setCellStyle(mapStyle.get("style1"));
	}
	for (int i = 0; i <= 17; i++) {
	    sh.setColumnWidth(i, 256 * 10);
	    if (i == 6) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	    if (i == 7) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	    if (i == 15) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	    if (i == 16) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	}

	sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(0, 1, 0, 13);

	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 新增数据行
	    sh.createRow(2 + i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		if (row.get("Aud_trm") != null) {
		    sh.getRow(2 + i).createCell(0).setCellValue(row.get("Aud_trm").toString());
		} else {
		    sh.getRow(2 + i).createCell(0).setCellValue("-");
		}
		if (row.get("china_no") != null) {
		    sh.getRow(2 + i).createCell(1).setCellValue(row.get("china_no").toString());
		} else {
		    sh.getRow(2 + i).createCell(1).setCellValue("-");
		}
		if (row.get("prvd_no") != null) {
		    sh.getRow(2 + i).createCell(2).setCellValue(row.get("prvd_no").toString());
		} else {
		    sh.getRow(2 + i).createCell(2).setCellValue("-");
		}
		if (row.get("prvd_name") != null) {
		    sh.getRow(2 + i).createCell(3).setCellValue(row.get("prvd_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(3).setCellValue("-");
		}
		if (row.get("city_name") != null) {
		    sh.getRow(2 + i).createCell(4).setCellValue(row.get("city_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(4).setCellValue("-");
		}
		if (row.get("opr_id") != null) {
		    sh.getRow(2 + i).createCell(5).setCellValue(row.get("opr_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(5).setCellValue("");
		}
		if (row.get("sum_amt") != null) {
		    sh.getRow(2 + i).createCell(6).setCellValue(row.get("sum_amt") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt")).doubleValue() : (Long) row.get("sum_amt"));
		} else {
		    sh.getRow(2 + i).createCell(6).setCellValue("-");
		}
		if (row.get("total_Amt_10000") != null) {
		    sh.getRow(2 + i).createCell(7)
			    .setCellValue(row.get("total_Amt_10000") instanceof BigDecimal ? ((BigDecimal) row.get("total_Amt_10000")).doubleValue() : (Long) row.get("total_Amt_10000"));
		} else {
		    sh.getRow(2 + i).createCell(7).setCellValue("-");
		}
		if (row.get("cnt_num") != null) {
		    sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("cnt_num"));
		} else {
		    sh.getRow(2 + i).createCell(8).setCellValue("-");
		}
		if (row.get("cnt_acct") != null) {
		    sh.getRow(2 + i).createCell(9).setCellValue((Integer) row.get("cnt_acct"));
		} else {
		    sh.getRow(2 + i).createCell(9).setCellValue("-");
		}
		if (row.get("cnt_subs") != null) {
		    sh.getRow(2 + i).createCell(10).setCellValue((Integer) row.get("cnt_subs"));
		} else {
		    sh.getRow(2 + i).createCell(10).setCellValue("-");
		}
		if (row.get("cnt_msisdn") != null) {
		    sh.getRow(2 + i).createCell(11).setCellValue((Integer) row.get("cnt_msisdn"));
		} else {
		    sh.getRow(2 + i).createCell(11).setCellValue("-");
		}
		if (row.get("cnt_chnl") != null) {
		    sh.getRow(2 + i).createCell(12).setCellValue((Integer) row.get("cnt_chnl"));
		} else {
		    sh.getRow(2 + i).createCell(12).setCellValue("-");
		}
		if (row.get("cnt_self") != null) {
		    sh.getRow(2 + i).createCell(13).setCellValue((Integer) row.get("cnt_self"));
		} else {
		    sh.getRow(2 + i).createCell(13).setCellValue("-");
		}
		if (row.get("cnt_shehui") != null) {
		    sh.getRow(2 + i).createCell(14).setCellValue((Integer) row.get("cnt_shehui"));
		} else {
		    sh.getRow(2 + i).createCell(14).setCellValue("-");
		}
		if (row.get("sum_amt_self") != null) {
		    sh.getRow(2 + i).createCell(15).setCellValue(row.get("sum_amt_self") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_self")).doubleValue() : (Long) row.get("sum_amt_self"));
		} else {
		    sh.getRow(2 + i).createCell(15).setCellValue("");
		}
		if (row.get("sum_amt_shehui") != null) {
		    sh.getRow(2 + i).createCell(16)
			    .setCellValue(row.get("sum_amt_shehui") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_shehui")).doubleValue() : (Long) row.get("sum_amt_shehui"));
		} else {
		    sh.getRow(2 + i).createCell(16).setCellValue("");
		}
		if (prvdId.equals("10000")) {
		    if (row.get("prvd_id") != null) {
			sh.getRow(2 + i).createCell(17).setCellValue(row.get("prvd_id").toString());
		    } else {
			sh.getRow(2 + i).createCell(17).setCellValue("-");
		    }
		} else {
		    if (row.get("city_id") != null) {
			sh.getRow(2 + i).createCell(17).setCellValue(row.get("city_id").toString());
		    } else {
			sh.getRow(2 + i).createCell(17).setCellValue("-");
		    }
		}

	    }
	    // 设置单元格格式
	    // 退费金额价值金额（元）、退费金额价值金额（万元）格式保留两位小数
	    sh.getRow(2 + i).getCell(6).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(7).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(15).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(16).setCellStyle(mapStyle.get("style5"));
	    // 退费金额、退费次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
	    sh.getRow(2 + i).getCell(8).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(9).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(10).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(11).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(12).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(13).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(14).setCellStyle(mapStyle.get("style6"));

	    sh.getRow(2 + i).getCell(0).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(1).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(2).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(3).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(4).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(5).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(17).setCellStyle(mapStyle.get("style3"));
	}

    }

    public void writeFourthPart(List<Map<String, Object>> data, Sheet sh, String prvdId, String month) {
	sh.createRow(0);
	if (prvdId.equals("10000")) {
	    sh.getRow(0).createCell(0).setCellValue("表5 异常退费金额排名前100操作工号");
	} else {
	    sh.getRow(0).createCell(0).setCellValue("表4 异常退费金额排名前100操作工号");
	}

	sh.createRow(1);
	sh.getRow(1).createCell(0).setCellValue("审计月");
	sh.getRow(1).createCell(1).setCellValue("退费金额排名");
	sh.getRow(1).createCell(2).setCellValue("省份");
	sh.getRow(1).createCell(3).setCellValue("地市");
	sh.getRow(1).createCell(4).setCellValue("操作员工标识");
	sh.getRow(1).createCell(5).setCellValue("退费金额（元）");
	sh.getRow(1).createCell(6).setCellValue("退费金额（万元）");
	sh.getRow(1).createCell(7).setCellValue("退费次数");
	sh.getRow(1).createCell(8).setCellValue("退费账户数");
	sh.getRow(1).createCell(9).setCellValue("退费用户数");
	sh.getRow(1).createCell(10).setCellValue("退费手机号数");
	sh.getRow(1).createCell(11).setCellValue("办理业务渠道数");
	sh.getRow(1).createCell(12).setCellValue("办理业务自有渠道数");
	sh.getRow(1).createCell(13).setCellValue("办理业务社会渠道数");
	sh.getRow(1).createCell(14).setCellValue("自有渠道退费金额(元)");
	sh.getRow(1).createCell(15).setCellValue("社会渠道退费金额(元)");
	sh.getRow(1).createCell(16).setCellValue("省份代码");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));

	for (int j = 0; j <= 16; j++) {
	    if (null == sh.getRow(1).getCell(j)) {
		sh.getRow(1).createCell(j);
	    }
	    sh.getRow(1).getCell(j).setCellStyle(mapStyle.get("style1"));
	}

	for (int i = 0; i <= 16; i++) {
	    sh.setColumnWidth(i, 256 * 10);
	    if (i == 5) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	    if (i == 6) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	    if (i == 14) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	    if (i == 15) {
		sh.setColumnWidth(i, 256 * 16);
	    }
	}

	sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(0, 1, 0, 13);

	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 新增数据行
	    sh.createRow(2 + i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		if (row.get("Aud_trm") != null) {
		    sh.getRow(2 + i).createCell(0).setCellValue(row.get("Aud_trm").toString());
		} else {
		    sh.getRow(2 + i).createCell(0).setCellValue("-");
		}
		if (row.get("prvd_name") != null) {
		    sh.getRow(2 + i).createCell(2).setCellValue(row.get("prvd_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(2).setCellValue("-");
		}
		if (row.get("city_name") != null) {
		    sh.getRow(2 + i).createCell(3).setCellValue(row.get("city_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(3).setCellValue("-");
		}
		if (row.get("opr_id") != null) {
		    sh.getRow(2 + i).createCell(4).setCellValue(row.get("opr_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(4).setCellValue("");
		}
		if (row.get("sum_amt") != null) {
		    sh.getRow(2 + i).createCell(5).setCellValue(row.get("sum_amt") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt")).doubleValue() : (Long) row.get("sum_amt"));
		} else {
		    sh.getRow(2 + i).createCell(5).setCellValue("-");
		}
		if (row.get("total_Amt_10000") != null) {
		    sh.getRow(2 + i).createCell(6)
			    .setCellValue(row.get("total_Amt_10000") instanceof BigDecimal ? ((BigDecimal) row.get("total_Amt_10000")).doubleValue() : (Long) row.get("total_Amt_10000"));
		} else {
		    sh.getRow(2 + i).createCell(6).setCellValue("-");
		}
		if (row.get("cnt_num") != null) {
		    sh.getRow(2 + i).createCell(7).setCellValue((Integer) row.get("cnt_num"));
		} else {
		    sh.getRow(2 + i).createCell(7).setCellValue("-");
		}
		if (row.get("cnt_acct") != null) {
		    sh.getRow(2 + i).createCell(8).setCellValue((Integer) row.get("cnt_acct"));
		} else {
		    sh.getRow(2 + i).createCell(8).setCellValue("-");
		}
		if (row.get("cnt_subs") != null) {
		    sh.getRow(2 + i).createCell(9).setCellValue((Integer) row.get("cnt_subs"));
		} else {
		    sh.getRow(2 + i).createCell(9).setCellValue("-");
		}
		if (row.get("cnt_msisdn") != null) {
		    sh.getRow(2 + i).createCell(10).setCellValue((Integer) row.get("cnt_msisdn"));
		} else {
		    sh.getRow(2 + i).createCell(10).setCellValue("-");
		}
		if (row.get("cnt_chnl") != null) {
		    sh.getRow(2 + i).createCell(11).setCellValue((Integer) row.get("cnt_chnl"));
		} else {
		    sh.getRow(2 + i).createCell(11).setCellValue("-");
		}
		if (row.get("cnt_self") != null) {
		    sh.getRow(2 + i).createCell(12).setCellValue((Integer) row.get("cnt_self"));
		} else {
		    sh.getRow(2 + i).createCell(12).setCellValue("-");
		}
		if (row.get("cnt_shehui") != null) {
		    sh.getRow(2 + i).createCell(13).setCellValue((Integer) row.get("cnt_shehui"));
		} else {
		    sh.getRow(2 + i).createCell(13).setCellValue("-");
		}
		if (row.get("sum_amt_self") != null) {
		    sh.getRow(2 + i).createCell(14).setCellValue(row.get("sum_amt_self") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_self")).doubleValue() : (Long) row.get("sum_amt_self"));
		} else {
		    sh.getRow(2 + i).createCell(14).setCellValue("");
		}
		if (row.get("sum_amt_shehui") != null) {
		    sh.getRow(2 + i).createCell(15)
			    .setCellValue(row.get("sum_amt_shehui") instanceof BigDecimal ? ((BigDecimal) row.get("sum_amt_shehui")).doubleValue() : (Long) row.get("sum_amt_shehui"));
		} else {
		    sh.getRow(2 + i).createCell(15).setCellValue("");
		}
		if (prvdId.equals("10000")) {
		    if (row.get("china_no") != null) {
			sh.getRow(2 + i).createCell(1).setCellValue(row.get("china_no").toString());
		    } else {
			sh.getRow(2 + i).createCell(1).setCellValue("-");
		    }
		    if (row.get("prvd_id") != null) {
			sh.getRow(2 + i).createCell(16).setCellValue(row.get("prvd_id").toString());
		    } else {
			sh.getRow(2 + i).createCell(16).setCellValue("-");
		    }
		} else {
		    if (row.get("prvd_no") != null) {
			sh.getRow(2 + i).createCell(1).setCellValue(row.get("prvd_no").toString());
		    } else {
			sh.getRow(2 + i).createCell(1).setCellValue("-");
		    }
		    if (row.get("city_id") != null) {
			sh.getRow(2 + i).createCell(16).setCellValue(row.get("city_id").toString());
		    } else {
			sh.getRow(2 + i).createCell(16).setCellValue("-");
		    }
		}

	    }
	    // 设置单元格格式
	    // 退费金额价值金额（元）、退费金额价值金额（万元）格式保留两位小数
	    sh.getRow(2 + i).getCell(5).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(6).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(14).setCellStyle(mapStyle.get("style5"));
	    sh.getRow(2 + i).getCell(15).setCellStyle(mapStyle.get("style5"));
	    // 退费金额、退费次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
	    sh.getRow(2 + i).getCell(7).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(8).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(9).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(10).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(11).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(12).setCellStyle(mapStyle.get("style6"));
	    sh.getRow(2 + i).getCell(13).setCellStyle(mapStyle.get("style6"));

	    sh.getRow(2 + i).getCell(0).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(1).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(2).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(3).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(4).setCellStyle(mapStyle.get("style3"));
	    sh.getRow(2 + i).getCell(16).setCellStyle(mapStyle.get("style3"));
	}

    }

    public void writeDetailsPart(List<Map<String, Object>> data, Sheet sh, String prvdId, String month) {
	sh.createRow(0);
	if (prvdId.equals("10000")) {
	    sh.getRow(0).createCell(0).setCellValue("表2 异常退费金额排名前100账户明细");
	}
	sh.createRow(1);
	sh.getRow(1).createCell(0).setCellValue("审计月");
	sh.getRow(1).createCell(1).setCellValue("省份代码");
	sh.getRow(1).createCell(2).setCellValue("省份名称");
	sh.getRow(1).createCell(3).setCellValue("地市代码");
	sh.getRow(1).createCell(4).setCellValue("地市名称");
	sh.getRow(1).createCell(5).setCellValue("交易流水号");
	sh.getRow(1).createCell(6).setCellValue("账户标识");
	sh.getRow(1).createCell(7).setCellValue("用户标识");
	sh.getRow(1).createCell(8).setCellValue("手机号");
	sh.getRow(1).createCell(9).setCellValue("办理业务渠道标志");
	sh.getRow(1).createCell(10).setCellValue("业务类型编码");
	sh.getRow(1).createCell(11).setCellValue("业务类型名称");
	sh.getRow(1).createCell(12).setCellValue("操作前账本余额：元");
	sh.getRow(1).createCell(13).setCellValue("操作后账本余额：元");
	sh.getRow(1).createCell(14).setCellValue("交易金额：元");
	sh.getRow(1).createCell(15).setCellValue("业务办理时间");
	sh.getRow(1).createCell(16).setCellValue("操作员工号");
	sh.getRow(1).createCell(17).setCellValue("营销案编码");
	sh.getRow(1).createCell(18).setCellValue("营销案名称");
	sh.getRow(1).createCell(19).setCellValue("办理业务渠道类别");
	sh.getRow(1).createCell(20).setCellValue("办理业务渠道类别名称");
	sh.getRow(1).createCell(21).setCellValue("办理业务渠道名称");
	sh.getRow(1).createCell(22).setCellValue("操作员工归属渠道标识");
	sh.getRow(1).createCell(23).setCellValue("操作员工归属渠道名称");
	sh.getRow(1).createCell(24).setCellValue("操作员工归属渠道类型");
	sh.getRow(1).createCell(25).setCellValue("归属客户标识");
	sh.getRow(1).createCell(26).setCellValue("个人客户标识");
	sh.getRow(1).createCell(27).setCellValue("集团客户标识");
	sh.getRow(1).createCell(28).setCellValue("退费方式");
	sh.getRow(1).createCell(29).setCellValue("账户名称");
	sh.getRow(1).createCell(30).setCellValue("欠费金额（元）");
	sh.getRow(1).createCell(31).setCellValue("欠费金额（万元）");
	sh.getRow(1).createCell(32).setCellValue("欠费账龄");
	sh.getRow(1).createCell(33).setCellValue("欠费月数");
	// 设置第一行单元格格式 及 合并单元格 -add by hufei
	sh.getRow(0).getCell(0).setCellStyle(mapStyle.get("style0"));
	sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 33));

	for (int j = 0; j <= 33; j++) {
	    if (null == sh.getRow(1).getCell(j)) {
		sh.getRow(1).createCell(j);
	    }
	    sh.getRow(1).getCell(j).setCellStyle(mapStyle.get("style1"));
	}

	for (int i = 0; i <= 33; i++) {
	    sh.setColumnWidth(i, 256 * 10);
	}

	sh.getRow(1).setHeightInPoints(35);

	// 第一个参数表示要冻结的列数，这里只冻结行所以为0；
	// 第二个参数表示要冻结的行数；
	// 第三个参数表示右边区域可见的首列序号，从1开始计算，这里是冻结行，所以为0；
	// 第四个参数表示下边区域可见的首行序号，也是从1开始计算；
	// sh.createFreezePane(0, 1, 0, 13);

	Map<String, Object> row = new HashMap<String, Object>();
	for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
	    row = data.get(i);
	    // 新增数据行
	    sh.createRow(2 + i);
	    // 插入单元格数据
	    if (row.size() == 0) {
		sh.getRow(2 + i).createCell(2).setCellValue("-");
	    } else {
		if (row.get("Aud_trm") != null) {
		    sh.getRow(2 + i).createCell(0).setCellValue(row.get("Aud_trm").toString());
		} else {
		    sh.getRow(2 + i).createCell(0).setCellValue("-");
		}
		if (row.get("prvd_id") != null) {
		    sh.getRow(2 + i).createCell(1).setCellValue(row.get("prvd_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(1).setCellValue("-");
		}
		if (row.get("prvd_name") != null) {
		    sh.getRow(2 + i).createCell(2).setCellValue(row.get("prvd_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(2).setCellValue("-");
		}
		if (row.get("city_id") != null) {
		    sh.getRow(2 + i).createCell(3).setCellValue(row.get("city_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(3).setCellValue("-");
		}
		if (row.get("city_name") != null) {
		    sh.getRow(2 + i).createCell(4).setCellValue(row.get("city_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(4).setCellValue("-");
		}
		if (row.get("trade_ser_no") != null) {
		    sh.getRow(2 + i).createCell(5).setCellValue(row.get("trade_ser_no").toString());
		} else {
		    sh.getRow(2 + i).createCell(5).setCellValue("-");
		}
		if (row.get("acct_id") != null) {
		    sh.getRow(2 + i).createCell(6).setCellValue(row.get("acct_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(6).setCellValue("-");
		}
		if (row.get("subs_id") != null) {
		    sh.getRow(2 + i).createCell(7).setCellValue(row.get("subs_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(7).setCellValue("-");
		}
		if (row.get("msisdn") != null) {
		    sh.getRow(2 + i).createCell(8).setCellValue(row.get("msisdn").toString());
		} else {
		    sh.getRow(2 + i).createCell(8).setCellValue("-");
		}
		if (row.get("busi_chnl_id") != null) {
		    sh.getRow(2 + i).createCell(9).setCellValue(row.get("busi_chnl_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(9).setCellValue("-");
		}
		if (row.get("busi_typ") != null) {
		    sh.getRow(2 + i).createCell(10).setCellValue(row.get("busi_typ").toString());
		} else {
		    sh.getRow(2 + i).createCell(10).setCellValue("-");
		}
		if (row.get("busi_typ_nm") != null) {
		    sh.getRow(2 + i).createCell(11).setCellValue(row.get("busi_typ_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(11).setCellValue("-");
		}
		if (row.get("opr_before_amt") != null) {
		    sh.getRow(2 + i).createCell(12)
			    .setCellValue(row.get("opr_before_amt") instanceof BigDecimal ? ((BigDecimal) row.get("opr_before_amt")).doubleValue() : (Long) row.get("opr_before_amt"));
		} else {
		    sh.getRow(2 + i).createCell(12).setCellValue("");
		}
		if (row.get("opr_after_amt") != null) {
		    sh.getRow(2 + i).createCell(13)
			    .setCellValue(row.get("opr_after_amt") instanceof BigDecimal ? ((BigDecimal) row.get("opr_after_amt")).doubleValue() : (Long) row.get("opr_after_amt"));
		} else {
		    sh.getRow(2 + i).createCell(13).setCellValue("");
		}
		if (row.get("trade_amt") != null) {
		    sh.getRow(2 + i).createCell(14).setCellValue(row.get("trade_amt") instanceof BigDecimal ? ((BigDecimal) row.get("trade_amt")).doubleValue() : (Long) row.get("trade_amt"));
		} else {
		    sh.getRow(2 + i).createCell(14).setCellValue("");
		}
		if (row.get("opr_tm") != null) {
		    sh.getRow(2 + i).createCell(15).setCellValue(row.get("opr_tm").toString());
		} else {
		    sh.getRow(2 + i).createCell(15).setCellValue("-");
		}
		if (row.get("opr_id") != null) {
		    sh.getRow(2 + i).createCell(16).setCellValue(row.get("opr_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(16).setCellValue("-");
		}
		if (row.get("offer_id") != null) {
		    sh.getRow(2 + i).createCell(17).setCellValue(row.get("offer_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(17).setCellValue("-");
		}
		if (row.get("offer_nm") != null) {
		    sh.getRow(2 + i).createCell(18).setCellValue(row.get("offer_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(18).setCellValue("-");
		}
		if (row.get("chnl_class") != null) {
		    sh.getRow(2 + i).createCell(19).setCellValue(row.get("chnl_class").toString());
		} else {
		    sh.getRow(2 + i).createCell(19).setCellValue("-");
		}
		if (row.get("chnl_class_nm") != null) {
		    sh.getRow(2 + i).createCell(20).setCellValue(row.get("chnl_class_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(20).setCellValue("-");
		}
		if (row.get("chnl_name") != null) {
		    sh.getRow(2 + i).createCell(21).setCellValue(row.get("chnl_name").toString());
		} else {
		    sh.getRow(2 + i).createCell(21).setCellValue("-");
		}
		if (row.get("staff_chnl_id") != null) {
		    sh.getRow(2 + i).createCell(22).setCellValue(row.get("staff_chnl_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(22).setCellValue("-");
		}
		if (row.get("staff_chnl_nm") != null) {
		    sh.getRow(2 + i).createCell(23).setCellValue(row.get("staff_chnl_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(23).setCellValue("-");
		}
		if (row.get("chnl_typ") != null) {
		    sh.getRow(2 + i).createCell(24).setCellValue(row.get("chnl_typ").toString());
		} else {
		    sh.getRow(2 + i).createCell(24).setCellValue("-");
		}
		if (row.get("blto_cust_id") != null) {
		    sh.getRow(2 + i).createCell(25).setCellValue(row.get("blto_cust_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(25).setCellValue("-");
		}
		if (row.get("indvl_cust_id") != null) {
		    sh.getRow(2 + i).createCell(26).setCellValue(row.get("indvl_cust_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(26).setCellValue("-");
		}
		if (row.get("indvl_cust_id") != null) {
		    sh.getRow(2 + i).createCell(26).setCellValue(row.get("indvl_cust_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(26).setCellValue("-");
		}
		if (row.get("org_cust_id") != null) {
		    sh.getRow(2 + i).createCell(27).setCellValue(row.get("org_cust_id").toString());
		} else {
		    sh.getRow(2 + i).createCell(27).setCellValue("-");
		}
		if (row.get("back_type_nm") != null) {
		    sh.getRow(2 + i).createCell(28).setCellValue(row.get("back_type_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(28).setCellValue("-");
		}
		if (row.get("acct_nm") != null) {
		    sh.getRow(2 + i).createCell(29).setCellValue(row.get("acct_nm").toString());
		} else {
		    sh.getRow(2 + i).createCell(29).setCellValue("-");
		}
		if (row.get("dbt_amt") != null) {
		    sh.getRow(2 + i).createCell(30).setCellValue(row.get("dbt_amt") instanceof BigDecimal ? ((BigDecimal) row.get("dbt_amt")).doubleValue() : (Long) row.get("dbt_amt"));
		} else {
		    sh.getRow(2 + i).createCell(30).setCellValue("-");
		}
		if (row.get("dbt_amt") != null) {
		    sh.getRow(2 + i)
			    .createCell(31)
			    .setCellValue(
				    row.get("dbt_amt") instanceof BigDecimal ? (((BigDecimal) row.get("dbt_amt")).divide(new BigDecimal(10000.00))).doubleValue()
					    : ((Long) row.get("dbt_amt") / 10000.00));
		} else {
		    sh.getRow(2 + i).createCell(31).setCellValue("-");
		}
		if (row.get("aging") != null) {
		    sh.getRow(2 + i).createCell(32).setCellValue((Integer) row.get("aging"));
		} else {
		    sh.getRow(2 + i).createCell(32).setCellValue("-");
		}
		if (row.get("dbt_mons") != null) {
		    sh.getRow(2 + i).createCell(33).setCellValue((Integer) row.get("dbt_mons"));
		} else {
		    sh.getRow(2 + i).createCell(33).setCellValue("-");
		}
		// 设置单元格格式
		for (int m = 0; m <= 33; m++) {
		    sh.getRow(2 + i).getCell(m).setCellStyle(mapStyle.get("style3"));
		}
		sh.getRow(2 + i).getCell(12).setCellStyle(mapStyle.get("style5"));
		sh.getRow(2 + i).getCell(13).setCellStyle(mapStyle.get("style5"));
		sh.getRow(2 + i).getCell(14).setCellStyle(mapStyle.get("style5"));
		sh.getRow(2 + i).getCell(30).setCellStyle(mapStyle.get("style5"));
		sh.getRow(2 + i).getCell(31).setCellStyle(mapStyle.get("style5"));
		sh.getRow(2 + i).getCell(32).setCellStyle(mapStyle.get("style6"));
		sh.getRow(2 + i).getCell(33).setCellStyle(mapStyle.get("style6"));
	    }
	}
    }

    private CellStyle getStyle0() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 16);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	style.setFont(font);
	return style;
    }

    private CellStyle getStyle1() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	// style.setFillBackgroundColor(HSSFColor.BLUE.index);
	style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	return style;
    }

    private CellStyle getStyle3() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	return style;
    }

    private CellStyle getStyle5() {
	CellStyle style = wb.createCellStyle();
	style.setAlignment(CellStyle.ALIGN_CENTER);
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style.setFont(font);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setBorderTop(CellStyle.BORDER_THIN);
	style.setWrapText(true);
	style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
	return style;
    }

    private CellStyle getStyle6() {
	CellStyle style0 = wb.createCellStyle();
	style0.setAlignment(CellStyle.ALIGN_CENTER);
	style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	Font font = wb.createFont();
	font.setFontName("宋体");
	font.setFontHeightInPoints((short) 12);
	style0.setFont(font);
	style0.setBorderBottom(CellStyle.BORDER_THIN);
	style0.setBorderLeft(CellStyle.BORDER_THIN);
	style0.setBorderRight(CellStyle.BORDER_THIN);
	style0.setBorderTop(CellStyle.BORDER_THIN);
	style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
	return style0;
    }

}