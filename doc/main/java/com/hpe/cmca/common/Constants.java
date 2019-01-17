/**
 * com.hp.cmccca.common.Constants.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Desc：
 * @author peter.fu
 * @refactor peter.fu
 * @date   Mar 9, 2015 5:24:04 PM
 * @version 1.0
 * @see
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  Mar 9, 2015 	   peter.fu 	         1. Created this class.
 * </pre>
 */
public class Constants {

	/**
	 * 代表全国的机构编码
	 */
	public static final int	ChinaCode			= 10000;
	/**
	 * 改成从notify中取数，31各省份+1个全国，总共32
	 */
	public static final int	ChinaProvinceCount	= 32;

	/**
	 * <pre>
	 * Desc： 系统级常量
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Jun 12, 2015 6:01:21 PM
	 * @version 1.0
	 * @see
	 * REVISIONS:
	 * Version 	   Date 		    Author 			  Description
	 * -------------------------------------------------------------------
	 * 1.0 		  Jun 12, 2015 	   peter.fu 	         1. Created this class.
	 * </pre>
	 */
	public static class System {

		public static final String	RETURNALL	= "1";	// 页面是否需要一次全部返回结果，即不分页

	}

	public static class Concern {

		public static final int	defaultAuditCycleBeforeN	= 1;	// 默认审计周期相对于当前时间的差值，比如1就是默认实现为上个月
		public static final int	defaultAuditInterval		= 1;	// 默认审计周期相对于当前时间的差值，比如1就是默认审计区间持续1个月

	}

	public static class Model {

		public static class ModelType {

			public static final int		CHINA				= 0;
			public static final int		PROVINCE			= 1;

			public static final String	CHINA_MESSAGE		= "全国";
			public static final String	PROVINCE_MESSAGE	= "省";

			public static String getDesc(int key) {

				if (CHINA == key) {
					return CHINA_MESSAGE;
				}
				if (PROVINCE == key) {
					return PROVINCE_MESSAGE;
				}
				return "其他";

			}
		}

		public static class FileRequestStatus {

			// 0-模型运行完毕，1-csv文件生成完毕 2-doc文件生成完毕 3-csv文件ftp完毕 4-doc文件ftp完毕 5-处理完毕
			public static final int		MODEL_FINISHED				= 0;
			public static final int		CSV_FILE_FINISHED			= 1;
			public static final int		DOC_FILE_FINISHED			= 2;
			public static final int		CSV_FTP_FINISHED			= 3;
			public static final int		DOC_FTP_FINISHED			= 4;
			public static final int		File_FINISHED				= 5;

			public static final String	MODEL_FINISHED_MESSAGE		= "模型运行完毕";
			public static final String	CSV_FILE_FINISHED_MESSAGE	= "csv文件生成完毕";
			public static final String	DOC_FILE_FINISHED_MESSAGE	= "doc文件生成完毕";
			public static final String	CSV_FTP_FINISHED_MESSAGE	= "csv文件ftp完毕";
			public static final String	DOC_FTP_FINISHED_MESSAGE	= "doc文件ftp完毕";
			public static final String	File_FINISHED_MESSAGE		= "处理完毕";

			public static String getDesc(int key) {

				if (MODEL_FINISHED == key) {
					return MODEL_FINISHED_MESSAGE;
				}
				if (CSV_FILE_FINISHED == key) {
					return CSV_FILE_FINISHED_MESSAGE;
				}
				if (DOC_FILE_FINISHED == key) {
					return DOC_FILE_FINISHED_MESSAGE;
				}
				if (CSV_FTP_FINISHED == key) {
					return CSV_FTP_FINISHED_MESSAGE;
				}
				if (DOC_FTP_FINISHED == key) {
					return DOC_FTP_FINISHED_MESSAGE;
				}
				if (File_FINISHED == key) {
					return File_FINISHED_MESSAGE;
				}
				return "其他";

			}
		}

		public static class FileType {

			// 审计报告audReport，审计清单
			public static final String	AUD_REPORT			= "audReport";
			public static final String	AUD_DETAIL			= "audDetail";

			public static final String	AUD_ZIP				= "audZip";


			public static final String	AUD_REPORT_MESSAGE	= "审计报告";
			public static final String	AUD_DETAIL_MESSAGE	= "审计清单";

			public static String getDesc(String key) {

				if (AUD_REPORT.equalsIgnoreCase(key)) {
					return AUD_REPORT_MESSAGE;
				}
				if (AUD_DETAIL.equalsIgnoreCase(key)) {
					return AUD_DETAIL_MESSAGE;
				}

				return "其他";

			}
		}
	}

	// 省公司ID和名称映射  20161019 add By GuoXY for 审计报告生成job
    public static final Map<Integer, String> MAP_PROVD_NAME = new LinkedHashMap<Integer, String>();
    static {
    	MAP_PROVD_NAME.put(10000,"全公司");
    	MAP_PROVD_NAME.put(10100,"北京");
    	MAP_PROVD_NAME.put(10200,"上海");
    	MAP_PROVD_NAME.put(10300,"天津");
    	MAP_PROVD_NAME.put(10400,"重庆");
    	MAP_PROVD_NAME.put(10500,"贵州");
    	MAP_PROVD_NAME.put(10600,"湖北");
    	MAP_PROVD_NAME.put(10700,"陕西");
    	MAP_PROVD_NAME.put(10800,"河北");
    	MAP_PROVD_NAME.put(10900,"河南");
    	MAP_PROVD_NAME.put(11000,"安徽");
    	MAP_PROVD_NAME.put(11100,"福建");
    	MAP_PROVD_NAME.put(11200,"青海");
    	MAP_PROVD_NAME.put(11300,"甘肃");
    	MAP_PROVD_NAME.put(11400,"浙江");
    	MAP_PROVD_NAME.put(11500,"海南");
    	MAP_PROVD_NAME.put(11600,"黑龙江");
    	MAP_PROVD_NAME.put(11700,"江苏");
    	MAP_PROVD_NAME.put(11800,"吉林");
    	MAP_PROVD_NAME.put(11900,"宁夏");
    	MAP_PROVD_NAME.put(12000,"山东");
    	MAP_PROVD_NAME.put(12100,"山西");
    	MAP_PROVD_NAME.put(12200,"新疆");
    	MAP_PROVD_NAME.put(12300,"广东");
    	MAP_PROVD_NAME.put(12400,"辽宁");
    	MAP_PROVD_NAME.put(12500,"广西");
    	MAP_PROVD_NAME.put(12600,"湖南");
    	MAP_PROVD_NAME.put(12700,"江西");
    	MAP_PROVD_NAME.put(12800,"内蒙古");
    	MAP_PROVD_NAME.put(12900,"云南");
    	MAP_PROVD_NAME.put(13000,"四川");
    	MAP_PROVD_NAME.put(13100,"西藏");

		MAP_PROVD_NAME.put(13200,"南方基地");
		MAP_PROVD_NAME.put(13300,"深圳中心");
		MAP_PROVD_NAME.put(13400,"集团一级支撑中心");
		MAP_PROVD_NAME.put(13500,"集团网络部");
		MAP_PROVD_NAME.put(20008,"中移在线公司");
    }

    //全国31省份的名称集合--审计跟进中有用到
    public static final List<String> LIST_PROVD_NAME = new ArrayList< String>();
    static {
    	LIST_PROVD_NAME.add("北京");
    	LIST_PROVD_NAME.add("上海");
    	LIST_PROVD_NAME.add("天津");
    	LIST_PROVD_NAME.add("重庆");
    	LIST_PROVD_NAME.add("贵州");
    	LIST_PROVD_NAME.add("湖北");
    	LIST_PROVD_NAME.add("陕西");
    	LIST_PROVD_NAME.add("河北");
    	LIST_PROVD_NAME.add("河南");
    	LIST_PROVD_NAME.add("安徽");
    	LIST_PROVD_NAME.add("福建");
    	LIST_PROVD_NAME.add("青海");
    	LIST_PROVD_NAME.add("甘肃");
    	LIST_PROVD_NAME.add("浙江");
    	LIST_PROVD_NAME.add("海南");
    	LIST_PROVD_NAME.add("黑龙江");
    	LIST_PROVD_NAME.add("江苏");
    	LIST_PROVD_NAME.add("吉林");
    	LIST_PROVD_NAME.add("宁夏");
    	LIST_PROVD_NAME.add("山东");
    	LIST_PROVD_NAME.add("山西");
    	LIST_PROVD_NAME.add("新疆");
    	LIST_PROVD_NAME.add("广东");
    	LIST_PROVD_NAME.add("辽宁");
    	LIST_PROVD_NAME.add("广西");
    	LIST_PROVD_NAME.add("湖南");
    	LIST_PROVD_NAME.add("江西");
    	LIST_PROVD_NAME.add("内蒙古");
    	LIST_PROVD_NAME.add("云南");
    	LIST_PROVD_NAME.add("四川");
    	LIST_PROVD_NAME.add("西藏");
    }
    public static final List<String> LIST_SUBJECT_NAME = new ArrayList<String>();
    static{
    	LIST_SUBJECT_NAME.add("客户欠费");
    	LIST_SUBJECT_NAME.add("有价卡管理违规");
    	LIST_SUBJECT_NAME.add("电子券管理违规");
    	LIST_SUBJECT_NAME.add("流量管理违规");
    	LIST_SUBJECT_NAME.add("员工异常业务操作");
		LIST_SUBJECT_NAME.add("跨省窜卡");
		LIST_SUBJECT_NAME.add("宽带业务管理违规");

		LIST_SUBJECT_NAME.add("养卡套利");
		LIST_SUBJECT_NAME.add("终端套利");
		LIST_SUBJECT_NAME.add("客户信息安全");
    }
    // 专题ID与名称映射  20161019 add By GuoXY for 审计报告生成job
    public static final Map<String, String> MAP_SUBJECT_NAME = new LinkedHashMap<String, String>();
    static {
    	MAP_SUBJECT_NAME.put("1","有价卡管理违规");
    	MAP_SUBJECT_NAME.put("2","养卡套利");
    	MAP_SUBJECT_NAME.put("3","社会渠道终端异常销售、套利");
    	MAP_SUBJECT_NAME.put("4","客户欠费");
    	MAP_SUBJECT_NAME.put("5","员工异常业务操作");
    	MAP_SUBJECT_NAME.put("6","电子券管理违规");
    	MAP_SUBJECT_NAME.put("7","流量管理违规");
    	MAP_SUBJECT_NAME.put("8","流量异常赠送");
    	MAP_SUBJECT_NAME.put("9","各专题");
		MAP_SUBJECT_NAME.put("11","跨省窜卡");
		MAP_SUBJECT_NAME.put("12","宽带业务管理违规");
		MAP_SUBJECT_NAME.put("13","客户信息安全");
    }
    // 关注点ID与名称映射  20161019 add By GuoXY for 审计报告生成job
    public static final Map<String, String> MAP_FOCUSCD_NAME = new LinkedHashMap<String, String>();
    static {
	MAP_FOCUSCD_NAME.put("1000","有价卡管理违规");
    	MAP_FOCUSCD_NAME.put("1001","未按规定在系统间同步加载");
    	MAP_FOCUSCD_NAME.put("1002","有价卡违规激活");
    	MAP_FOCUSCD_NAME.put("1003","有价卡违规销售");
    	MAP_FOCUSCD_NAME.put("1004","退换后的坏卡或报废卡未封锁");
    	MAP_FOCUSCD_NAME.put("1005","有价卡违规重复使用");
    	MAP_FOCUSCD_NAME.put("1012","赠送有价卡异省充值");

    	MAP_FOCUSCD_NAME.put("2000","养卡套利汇总情况");
    	MAP_FOCUSCD_NAME.put("2001","渠道养卡套利");
    	MAP_FOCUSCD_NAME.put("2002","渠道养卡");

    	MAP_FOCUSCD_NAME.put("3000","终端套利汇总情况");
    	MAP_FOCUSCD_NAME.put("3001","代理商沉默串码套利");
    	MAP_FOCUSCD_NAME.put("3002","代理商养机");
    	MAP_FOCUSCD_NAME.put("3003","代理商空串码套利");
    	MAP_FOCUSCD_NAME.put("3004","代理商终端拆包套利");
    	MAP_FOCUSCD_NAME.put("3005","代理商跨省窜货套利");

    	MAP_FOCUSCD_NAME.put("5001","异常积分赠送");
    	MAP_FOCUSCD_NAME.put("5002","异常积分转移");
    	MAP_FOCUSCD_NAME.put("5003","异常话费赠送");
    	MAP_FOCUSCD_NAME.put("5004","异常退费");
    	MAP_FOCUSCD_NAME.put("5000","员工异常业务操作汇总情况");

    	MAP_FOCUSCD_NAME.put("6001","平台间电子券数据不一致");
    	MAP_FOCUSCD_NAME.put("6002","电子券管理违规");
    	MAP_FOCUSCD_NAME.put("6000","电子券管理违规");
    	MAP_FOCUSCD_NAME.put("600301","折价电子券兑换话费套利_电子券管理违规");
    	MAP_FOCUSCD_NAME.put("600302","折价电子券兑换话费套利_电子券消费");
    	MAP_FOCUSCD_NAME.put("6004"	,"虚假用户套取电子券_用户入网");
    	MAP_FOCUSCD_NAME.put("6000","电子券管理违规");
    	
    	MAP_FOCUSCD_NAME.put("7000","流量管理违规");
    	MAP_FOCUSCD_NAME.put("700101","疑似违规流量转售");
    	MAP_FOCUSCD_NAME.put("700102","疑似违规流量转售集团客户");
    	MAP_FOCUSCD_NAME.put("8000","流量异常赠送汇总");
    	MAP_FOCUSCD_NAME.put("9999","各专题");

		MAP_FOCUSCD_NAME.put("1100","跨省窜卡");

		MAP_FOCUSCD_NAME.put("1200", "宽带业务管理违规");
		MAP_FOCUSCD_NAME.put("1201","虚假宽带用户");
		MAP_FOCUSCD_NAME.put("1203","系统间宽带信息不一致");

		MAP_FOCUSCD_NAME.put("1300", "客户信息安全");
		MAP_FOCUSCD_NAME.put("1301","客户信息安全系统权限管控违规");
		MAP_FOCUSCD_NAME.put("1302","客户信息安全金库模式管控违规");
    }
//    // 专题与其对应汇总关注点映射  20161019 add By GuoXY for 审计报告生成job
//    public static final Map<String, String> MAP_SUBJECT_SUMFOCUSCD = new LinkedHashMap<String, String>();
//    static {
//    	MAP_SUBJECT_SUMFOCUSCD.put("1","1000"); // 有价卡
//    	MAP_SUBJECT_SUMFOCUSCD.put("2","2002"); // 渠道养卡（只有一个关注点，用他代表汇总）
//    	MAP_SUBJECT_SUMFOCUSCD.put("3","3000"); // 终端套利
//    }
}
