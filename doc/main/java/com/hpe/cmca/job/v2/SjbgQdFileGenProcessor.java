package com.hpe.cmca.job.v2;

import java.io.File;
import java.io.FileOutputStream;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.service.YgycService;
import com.hpe.cmca.finals.Constants;

@Service("SjbgQdFileGenProcessor")
public class SjbgQdFileGenProcessor  extends BaseObject{
	
		@Autowired
		private YgycService ygycService;
	
		SXSSFWorkbook wb =null;
		
		Sheet sh =null;
	
	    private String fileName;
	    
	    private ArrayList<String> strs   = new ArrayList<String>();
	    
	    Map stylemap =new HashMap();

	    // 重写构建文件名方法
	    protected String buildFileName() {
		return fileName +".xlsx";
	    }

	    public ArrayList<String> getFileNameList() {
		return strs;
	    }

	    public File generateFile(String prvdid,String andtrm) throws Exception {
	    	String month =andtrm.substring(4);
	    	String year =andtrm.substring(0,4);
	    	wb = new SXSSFWorkbook(1000);
	    	stylemap.put("style0", getStyle0());
	    	stylemap.put("style1", getStyle1());
	    	stylemap.put("style5", getStyle5());
	    	stylemap.put("style6", getStyle6());
		// 根据省份及月份对文件夹名命名
		    this.setFileName(Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdid)) +"_"+andtrm+"_审计报告清单");
		    // 创建第一个sheet
		    //异常高额积分赠送
		    writeFirstPart(ygycService.getYGYCJFZSdata(andtrm, prvdid, "1"), wb, prvdid,year, month);
		    // 创建第二个sheet
		    //异常高额积分转移
		    writeSecondPart(ygycService.getYGYCJFZSdata(andtrm, prvdid, "2"), wb, prvdid,year, month);
//		    // 创建第三个sheet
		    //异常高额话费赠送
		    writeThirdPart(ygycService.getYGYCJFZSdata(andtrm, prvdid, "3"), wb, prvdid,year, month);
//		    // 创建第四个sheet
		    //异常高额退费
		    writeFourthPart(ygycService.getYGYCJFZSdata(andtrm, prvdid, "4"), wb,prvdid,year, month);

		    FileOutputStream out = new FileOutputStream(this.getLocalPath());
		    wb.write(out);
		    File f =new File(this.getLocalPath());
		    return f;
	    }
	    

	    /**
	     * 
	     * <pre>
	     * Desc  将获取到的数据插入到excel‘员工异常操作异常积分转移排名汇总’下的sheet1中
	     * @param data：获取的数据
	     * @param sh：应插入的sheet
	     * @param prvdId：省份
	     * @param month：月份
	     * @author feihu
	     * Mar 29, 2017 3:31:13 PM
	     * </pre>
	     */
	    public void writeFirstPart(List<Map<String, Object>> data, SXSSFWorkbook wb, String prvdId, String year,String month) {
	    	if(data!= null && data.size() > 0)
	    	{
	    		sh = wb.createSheet("异常高额积分赠送");
	    		sh.createRow(0);
	    		sh.getRow(0).createCell(0).setCellValue(year+"年"+month+"月"+Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId))+"异常高额积分赠送清单");
	    		
	    		sh.createRow(1);
	    		//序号	审计月	省份	地市	操作工号	赠送号码	赠送积分数量(分)	价值金额(元)	赠送次数（次）
	    		sh.getRow(1).createCell(0).setCellValue("序号");
	    		sh.getRow(1).createCell(1).setCellValue("审计月");
	    		sh.getRow(1).createCell(2).setCellValue("省份");
	    		sh.getRow(1).createCell(3).setCellValue("地市");
	    		sh.getRow(1).createCell(4).setCellValue("操作工号");
	    		sh.getRow(1).createCell(5).setCellValue("赠送用户标识");
	    		sh.getRow(1).createCell(6).setCellValue("赠送积分数量(分)");
	    		sh.getRow(1).createCell(7).setCellValue("价值金额(元)");
	    		sh.getRow(1).createCell(8).setCellValue("赠送次数（次）");
	    		// 设置第一行单元格格式 及 合并单元格 
	    		sh.getRow(0).getCell(0).setCellStyle((CellStyle)stylemap.get("style0"));
	    		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

	    		for (int j = 0; j <= 8; j++) {
	    		    if (null == sh.getRow(1).getCell(j)) {
	    			sh.getRow(1).createCell(j);
	    		    }
	    		    sh.getRow(1).getCell(j).setCellStyle((CellStyle)stylemap.get("style1"));
	    		}

	    		for (int i = 0; i <= 8; i++) {
	    		    if (i >= 5 && i <= 7) {
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
	    		    	if (row.get("rank_no") != null) {
	    				    sh.getRow(2 + i).createCell(0).setCellValue(row.get("rank_no").toString());
	    				} else {
	    				    sh.getRow(2 + i).createCell(0).setCellValue("-");
	    				}
	    				if (row.get("Aud_trm") != null) {
	    				    sh.getRow(2 + i).createCell(1).setCellValue(row.get("Aud_trm").toString());
	    				} else {
	    				    sh.getRow(2 + i).createCell(1).setCellValue("-");
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
	    				if (row.get("staff_id") != null) {//操作工号
	    				    sh.getRow(2 + i).createCell(4).setCellValue(row.get("staff_id").toString());
	    				} else {
	    				    sh.getRow(2 + i).createCell(4).setCellValue("-");
	    				}
	    				if (row.get("subs_id") != null) {//赠送用户标识
	    				    sh.getRow(2 + i).createCell(5).setCellValue(row.get("subs_id").toString());
	    				} else {
	    				    sh.getRow(2 + i).createCell(5).setCellValue("-");
	    				}
	    				if (row.get("total_value") != null) {//赠送积分数量
	    				    sh.getRow(2 + i).createCell(6).setCellValue(
	    				    		row.get("total_value") instanceof BigDecimal ? ((BigDecimal) row.get("total_value")).doubleValue() : (Long) row.get("total_value")
	    				    		);
	    				} else {
	    				    sh.getRow(2 + i).createCell(6).setCellValue("-");
	    				}
	    				if (row.get("total_Amt") != null) {//价值金额
	    				    sh.getRow(2 + i).createCell(7).setCellValue(
	    				    		row.get("total_Amt") instanceof BigDecimal ? ((BigDecimal) row.get("total_Amt")).doubleValue() : (Long) row.get("total_Amt")
	    				    		);
	    				} else {
	    				    sh.getRow(2 + i).createCell(7).setCellValue("");
	    				}
	    				if (row.get("total_Time") != null) {//赠送次数
	    				    sh.getRow(2 + i).createCell(8).setCellValue(
	    				    		row.get("total_Time") instanceof BigDecimal ? ((BigDecimal) row.get("total_Time")).doubleValue() : (Long) row.get("total_Time")
	    				    		);
	    				} else {
	    				    sh.getRow(2 + i).createCell(8).setCellValue("");
	    				}
	    			
	    		    }
	    		    // 设置单元格格式
	    		    // 转入积分值价值金额（元）、转入积分值价值金额（万元）格式保留两位小数
	    		    sh.getRow(2 + i).getCell(7).setCellStyle((CellStyle)stylemap.get("style5"));
	    		    // 转入积分值、转入次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
	    		    sh.getRow(2 + i).getCell(0).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(1).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(2).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(3).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(4).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(5).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(6).setCellStyle((CellStyle)stylemap.get("style6"));
	    		    sh.getRow(2 + i).getCell(8).setCellStyle((CellStyle)stylemap.get("style6"));
	    		}
	    	}

	    }
	    
	    public void writeSecondPart(List<Map<String, Object>> data, SXSSFWorkbook wb, String prvdId,String year, String month) {
			if(data != null && data.size() > 0){
				sh = wb.createSheet("异常高额积分转移");
				sh.createRow(0);
				sh.getRow(0).createCell(0).setCellValue(year+"年"+month+"月"+Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId))+"异常高额积分转移清单");
				sh.createRow(1);
				//序号	审计月	省份	地市	操作工号	转入手机号	转入积分值数量(分)	 价值金额（元） 	转入次数（次）
				sh.getRow(1).createCell(0).setCellValue("序号");
				sh.getRow(1).createCell(1).setCellValue("审计月");
				sh.getRow(1).createCell(2).setCellValue("省份");
				sh.getRow(1).createCell(3).setCellValue("地市");
				sh.getRow(1).createCell(4).setCellValue("操作工号");
				sh.getRow(1).createCell(5).setCellValue("转入用户标识");
				sh.getRow(1).createCell(6).setCellValue("转入积分值数量（分）");
				sh.getRow(1).createCell(7).setCellValue("价值金额（元）");
				sh.getRow(1).createCell(8).setCellValue("转入次数（次）");
				// 设置第一行单元格格式 及 合并单元格 
				sh.getRow(0).getCell(0).setCellStyle((CellStyle)stylemap.get("style0"));
				sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

				for (int j = 0; j <= 8; j++) {
				    if (null == sh.getRow(1).getCell(j)) {
					sh.getRow(1).createCell(j);
				    }
				    sh.getRow(1).getCell(j).setCellStyle((CellStyle)stylemap.get("style1"));
				}

				for (int i = 0; i <= 8; i++) {
				    if (i >= 5 && i <= 7) {
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
				    	if (row.get("rank_no") != null) {
						    sh.getRow(2 + i).createCell(0).setCellValue(row.get("rank_no").toString());
						} else {
						    sh.getRow(2 + i).createCell(0).setCellValue("-");
						}
						if (row.get("Aud_trm") != null) {
						    sh.getRow(2 + i).createCell(1).setCellValue(row.get("Aud_trm").toString());
						} else {
						    sh.getRow(2 + i).createCell(1).setCellValue("-");
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
						if (row.get("staff_id") != null) {//操作工号
						    sh.getRow(2 + i).createCell(4).setCellValue(row.get("staff_id").toString());
						} else {
						    sh.getRow(2 + i).createCell(4).setCellValue("-");
						}
						if (row.get("subs_id") != null) {//转入用户标识
						    sh.getRow(2 + i).createCell(5).setCellValue(row.get("subs_id").toString());
						} else {
						    sh.getRow(2 + i).createCell(5).setCellValue("-");
						}
						if (row.get("total_value") != null) {//转入积分值数量
						    sh.getRow(2 + i).createCell(6).setCellValue(
						    		row.get("total_value") instanceof BigDecimal ? ((BigDecimal) row.get("total_value")).doubleValue() : (Long) row.get("total_value")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(6).setCellValue("-");
						}
						if (row.get("total_Amt") != null) {//价值金额
						    sh.getRow(2 + i).createCell(7).setCellValue(
						    		row.get("total_Amt") instanceof BigDecimal ? ((BigDecimal) row.get("total_Amt")).doubleValue() : (Long) row.get("total_Amt")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(7).setCellValue("");
						}
						if (row.get("total_Time") != null) {//转入次数
						    sh.getRow(2 + i).createCell(8).setCellValue(
						    		row.get("total_Time") instanceof BigDecimal ? ((BigDecimal) row.get("total_Time")).doubleValue() : (Long) row.get("total_Time")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(8).setCellValue("");
						}
					
				    }
				    // 设置单元格格式
				    // 转入积分值价值金额（元）、转入积分值价值金额（万元）格式保留两位小数
				    sh.getRow(2 + i).getCell(7).setCellStyle((CellStyle)stylemap.get("style5"));
				    // 转入积分值、转入次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
				    sh.getRow(2 + i).getCell(0).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(1).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(2).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(3).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(4).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(5).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(6).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(8).setCellStyle((CellStyle)stylemap.get("style6"));
				}
			}

		}
	    
	    public void writeThirdPart(List<Map<String, Object>> data, SXSSFWorkbook wb, String prvdId,String year, String month) {
			if(data != null && data.size() > 0){
				sh = wb.createSheet("异常高额话费赠送");
				sh.createRow(0);
				sh.getRow(0).createCell(0).setCellValue(year+"年"+month+"月"+Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId))+"异常高额话费赠送清单");
				sh.createRow(1);
				//序号	审计月	省份	地市	操作工号	赠送账号	赠送金额（元）	 赠送次数（次） 
				sh.getRow(1).createCell(0).setCellValue("序号");
				sh.getRow(1).createCell(1).setCellValue("审计月");
				sh.getRow(1).createCell(2).setCellValue("省份");
				sh.getRow(1).createCell(3).setCellValue("地市");
				sh.getRow(1).createCell(4).setCellValue("操作工号");
				sh.getRow(1).createCell(5).setCellValue("赠送账号");
				sh.getRow(1).createCell(6).setCellValue("赠送金额（元）");
				sh.getRow(1).createCell(7).setCellValue("赠送次数（次）");
				// 设置第一行单元格格式 及 合并单元格 
				sh.getRow(0).getCell(0).setCellStyle((CellStyle)stylemap.get("style0"));
				sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

				for (int j = 0; j <= 7; j++) {
				    if (null == sh.getRow(1).getCell(j)) {
					sh.getRow(1).createCell(j);
				    }
				    sh.getRow(1).getCell(j).setCellStyle((CellStyle)stylemap.get("style1"));
				}

				for (int i = 0; i <= 7; i++) {
				    if (i >= 5 && i <= 7) {
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
				    	if (row.get("rank_no") != null) {
						    sh.getRow(2 + i).createCell(0).setCellValue(row.get("rank_no").toString());
						} else {
						    sh.getRow(2 + i).createCell(0).setCellValue("-");
						}
						if (row.get("Aud_trm") != null) {
						    sh.getRow(2 + i).createCell(1).setCellValue(row.get("Aud_trm").toString());
						} else {
						    sh.getRow(2 + i).createCell(1).setCellValue("-");
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
						if (row.get("staff_id") != null) {//操作工号
						    sh.getRow(2 + i).createCell(4).setCellValue(row.get("staff_id").toString());
						} else {
						    sh.getRow(2 + i).createCell(4).setCellValue("-");
						}
						if (row.get("acct_id") != null) {//赠送账号
						    sh.getRow(2 + i).createCell(5).setCellValue(row.get("acct_id").toString());
						} else {
						    sh.getRow(2 + i).createCell(5).setCellValue("-");
						}
						if (row.get("total_value") != null) {//赠送金额
						    sh.getRow(2 + i).createCell(6).setCellValue(
						    		row.get("total_value") instanceof BigDecimal ? ((BigDecimal) row.get("total_value")).doubleValue() : (Long) row.get("total_value")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(6).setCellValue("-");
						}
						if (row.get("total_Time") != null) {//赠送次数
						    sh.getRow(2 + i).createCell(7).setCellValue(
						    		row.get("total_Time") instanceof BigDecimal ? ((BigDecimal) row.get("total_Time")).doubleValue() : (Long) row.get("total_Time")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(7).setCellValue("");
						}
					
				    }
				    // 设置单元格格式
				    // 转入积分值价值金额（元）、转入积分值价值金额（万元）格式保留两位小数
				    sh.getRow(2 + i).getCell(6).setCellStyle((CellStyle)stylemap.get("style5"));
				    // 转入积分值、转入次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
				    sh.getRow(2 + i).getCell(0).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(1).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(2).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(3).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(4).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(5).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(7).setCellStyle((CellStyle)stylemap.get("style6"));
				}
			}
	    	

		   }
	    
	    public void writeFourthPart(List<Map<String, Object>> data, SXSSFWorkbook wb, String prvdId,String year, String month) {
			if(data != null && data.size() > 0){
				sh = wb.createSheet("异常高额退费");
				sh.createRow(0);
				sh.getRow(0).createCell(0).setCellValue(year+"年"+month+"月"+Constants.MAP_PROVD_NAME.get(Integer.parseInt(prvdId))+"异常高额退费清单");
				sh.createRow(1);
				//序号	审计月	省份	地市	操作工号	退费账号	退费金额（元）	退费次数（次）
				sh.getRow(1).createCell(0).setCellValue("序号");
				sh.getRow(1).createCell(1).setCellValue("审计月");
				sh.getRow(1).createCell(2).setCellValue("省份");
				sh.getRow(1).createCell(3).setCellValue("地市");
				sh.getRow(1).createCell(4).setCellValue("操作工号");
				sh.getRow(1).createCell(5).setCellValue("退费账号");
				sh.getRow(1).createCell(6).setCellValue("退费金额（元）");
				sh.getRow(1).createCell(7).setCellValue("退费次数（次）");
				// 设置第一行单元格格式 及 合并单元格 
				sh.getRow(0).getCell(0).setCellStyle((CellStyle)stylemap.get("style0"));
				sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

				for (int j = 0; j <= 7; j++) {
				    if (null == sh.getRow(1).getCell(j)) {
					sh.getRow(1).createCell(j);
				    }
				    sh.getRow(1).getCell(j).setCellStyle((CellStyle)stylemap.get("style1"));
				}

				for (int i = 0; i <= 7; i++) {
				    if (i >= 5 && i <= 7) {
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
				    	if (row.get("rank_no") != null) {
						    sh.getRow(2 + i).createCell(0).setCellValue(row.get("rank_no").toString());
						} else {
						    sh.getRow(2 + i).createCell(0).setCellValue("-");
						}
						if (row.get("Aud_trm") != null) {
						    sh.getRow(2 + i).createCell(1).setCellValue(row.get("Aud_trm").toString());
						} else {
						    sh.getRow(2 + i).createCell(1).setCellValue("-");
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
						if (row.get("staff_id") != null) {//操作工号
						    sh.getRow(2 + i).createCell(4).setCellValue(row.get("staff_id").toString());
						} else {
						    sh.getRow(2 + i).createCell(4).setCellValue("-");
						}
						if (row.get("acct_id") != null) {//赠送账号
						    sh.getRow(2 + i).createCell(5).setCellValue(row.get("acct_id").toString());
						} else {
						    sh.getRow(2 + i).createCell(5).setCellValue("-");
						}
						if (row.get("total_value") != null) {//赠送积分数量
						    sh.getRow(2 + i).createCell(6).setCellValue(
						    		row.get("total_value") instanceof BigDecimal ? ((BigDecimal) row.get("total_value")).doubleValue() : (Long) row.get("total_value")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(6).setCellValue("-");
						}
						if (row.get("total_Time") != null) {//赠送次数
						    sh.getRow(2 + i).createCell(7).setCellValue(
						    		row.get("total_Time") instanceof BigDecimal ? ((BigDecimal) row.get("total_Time")).doubleValue() : (Long) row.get("total_Time")
						    		);
						} else {
						    sh.getRow(2 + i).createCell(7).setCellValue("");
						}
					
				    }
				    // 设置单元格格式
				    // 转入积分值价值金额（元）、转入积分值价值金额（万元）格式保留两位小数
				    sh.getRow(2 + i).getCell(6).setCellStyle((CellStyle)stylemap.get("style5"));
				    // 转入积分值、转入次数、操作工号数 、积分服务渠道种类数 、操作工号归属渠道数格式为数字
				    sh.getRow(2 + i).getCell(0).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(1).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(2).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(3).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(4).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(5).setCellStyle((CellStyle)stylemap.get("style6"));
				    sh.getRow(2 + i).getCell(7).setCellStyle((CellStyle)stylemap.get("style6"));
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
	
	 
    
    public String getLocalDir() {
    	String tempDir = propertyUtil.getPropValue("tempDirV2");
		FileUtil.mkdirs(tempDir);
		return propertyUtil.getPropValue("tempDirV2");
	}

	public String getLocalPath() {
		return getLocalDir() + '/' + this.buildFileName();
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
