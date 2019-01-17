/**
 * com.hp.cmcc.service.sjzlService.java
 * Copyright (c) 2009 Hewlett-Packard Development Company, L.P.
 * All rights reserved.
 */
package com.hpe.cmca.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.GRSJMapper;
import com.hpe.cmca.pojo.GRSJData;
import com.hpe.cmca.pojo.GRSJsf;
import com.hpe.cmca.pojo.GRSJzt;

@Repository
@Service
public class SJRWService {
	@Autowired
	private MybatisDao mybatisDao;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	public Object getCollec(String month) {
		//获取下个月的时间字段
		// 获取所有数据
		GRSJMapper grsjMapper = mybatisDao.getSqlSession().getMapper(GRSJMapper.class);
		List<GRSJData> collec = grsjMapper.getCollec(month);
		// 获取月份，创建相应的数组
		int dayOfMonth = getTime(month);
		Object[] dayNum = new Object[dayOfMonth];// 日期
		List<GRSJzt> zt = new ArrayList<GRSJzt>();// 专题数组
		// 建立外层数组
		zt = grsjMapper.getZt();
		// 將专题排序
		List<GRSJzt> zt1 = new ArrayList<>();
		zt1.add(zt.get(1));
		zt1.add(zt.get(2));
		zt.remove(1);
		zt.remove(1);
		zt.add(zt1.get(0));
		zt.add(zt1.get(1));
		// 获取所有专题，所有流程完成天数
		Map<String, Object> m1 = new HashMap<>();
		for (int i = 0; i < zt.size(); i++) {
			// 获取专题名称
			String ztnm = zt.get(i).getZtNm();
			int id = zt.get(i).getZtId();
			// 预先计算各流程完成最大值
			int dBuleEndTime = 0;
			int yellowEndTime = 0;
			int rRedEndTime = 0;
			int bl=32;
			int yl=32;
			int re=32;
			
			Map<String, Object> m2 = new HashMap<>();
			for (int j = 0; j < collec.size(); j++) {
				
				if (id==collec.get(j).getSubjectId()) {
					if ("数据入库".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > dBuleEndTime) {
							dBuleEndTime = collec.get(j).getQueryDt();
						}
						if (collec.get(j).getQueryDt() < bl) {
							bl = collec.get(j).getQueryDt();
						}
						
					}
					if ("模型执行".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > yellowEndTime) {
							yellowEndTime = collec.get(j).getQueryDt();
						}
						if (collec.get(j).getQueryDt() < yl) {
							yl = collec.get(j).getQueryDt();
						}
					}
					if ("报告生成".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > rRedEndTime) {
							rRedEndTime = collec.get(j).getQueryDt();
						}
						if (collec.get(j).getQueryDt() < re) {
							re = collec.get(j).getQueryDt();
						}
					}
				}
			}
			int lBuleEndTime = 0;
			if(dBuleEndTime!=0){
				for (int j = 3; j > 0; j--) {
					if ((dBuleEndTime + j) <= dayOfMonth) {
						lBuleEndTime = dBuleEndTime + j; 
						break;
					}
				}
			}
			
			int pRedEndTime = 0;
			if(rRedEndTime!=0){
				for (int j = 3; j > 0; j--) {
					if ((rRedEndTime + j) <= dayOfMonth) {
						pRedEndTime = rRedEndTime + j;
						break;
					}
				}
			}
			
			String dBuleEndTime1 = "";
			String lBuleEndTime1 = "";
			String yellowEndTime1 = "";
			String rRedEndTime1 = "";
			String pRedEndTime1 = "";
			if (dBuleEndTime < 10) {
				dBuleEndTime1 = month + "0" + dBuleEndTime;
			} else {
				dBuleEndTime1 = month + dBuleEndTime;
			}
			if (lBuleEndTime < 10) {
				lBuleEndTime1 = month + "0" + lBuleEndTime;
			} else {
				lBuleEndTime1 = month + lBuleEndTime;
			}
			if (yellowEndTime < 10) {
				yellowEndTime1 = month + "0" + yellowEndTime;
			} else {
				yellowEndTime1 = month + yellowEndTime;
			}
			if (rRedEndTime < 10) {
				rRedEndTime1 = month + "0" + rRedEndTime;
			} else {
				rRedEndTime1 = month + rRedEndTime;
			}
			if (pRedEndTime < 10) {
				pRedEndTime1 = month + "0" + pRedEndTime;
			} else {
				pRedEndTime1 = month + pRedEndTime;
			}
			
			m2.put("数据入库", dBuleEndTime1);
			m2.put("数据入库核查", lBuleEndTime1);
			m2.put("模型执行", yellowEndTime1);
			m2.put("报告生成", rRedEndTime1);
			m2.put("报告生成核查", pRedEndTime1);
			m2.put("blmin", bl+"");//蓝色最小值
			if(lBuleEndTime>dBuleEndTime){
				m2.put("blmax", lBuleEndTime+"");//蓝色最大值
			}else{
				m2.put("blmax", dBuleEndTime+"");//蓝色最大值
			}
			m2.put("blmax", lBuleEndTime+"");//蓝色最大值
			m2.put("ylmin", yl+"");//黄色最小值
			m2.put("ylmax", yellowEndTime+"");//黄色最大值
			m2.put("remin", re+"");//红色最小值
			if(pRedEndTime>rRedEndTime){
				m2.put("remax", pRedEndTime+"");//红色最大值
			}else{
				m2.put("remax", rRedEndTime+"");//红色最大值
			}
			m1.put(ztnm, m2);
		}
		// 获取所有专题，所有流程完成天数
		Object[] out = new Object[zt.size()];
		// 建立最外侧map
		Map<String, Object> tableData = new HashMap<>();
		tableData.put("tableData", out);
		for (int i = 0; i < zt.size(); i++) { // 开始循环专题了
			String ztnm = zt.get(i).getZtNm();
			int id = zt.get(i).getZtId();
			Map<String, Object> mapzt = new HashMap<>();
			mapzt.put("dateSum", dayOfMonth);
			mapzt.put("id", zt.get(i).getZtId());
			mapzt.put("subjectName", zt.get(i).getZtNm());
			Map<String, String> m11 = (Map<String, String>) m1.get(ztnm);
			String dBuleEndTime = m11.get("数据入库");

			String lBuleEndTime = m11.get("数据入库核查");

			String yellowEndTime = m11.get("模型执行");

			String rRedEndTime = m11.get("报告生成");

			String pRedEndTime = m11.get("报告生成核查");
			//获取各种颜色最小最大值
			String blumin=m11.get("blmin");
			int blmin=Integer.parseInt(blumin);
			
			String blumax=m11.get("blmax");
			int blmax=Integer.parseInt(blumax);//蓝色
			
			String yelmin=m11.get("ylmin");
			int ylmin=Integer.parseInt(yelmin);
			
			String yelmax=m11.get("ylmax");
			int ylmax=Integer.parseInt(yelmax);//黄色
			
			String redmin=m11.get("remin");
			int remin=Integer.parseInt(redmin);
			
			String redmax=m11.get("remax");
			int remax=Integer.parseInt(redmax);//红色
			//<<<<<<>>>>>>>>>
			int blueday = 0;
			int redday = 0;
			for (int j = 1; j < dayNum.length + 1; j++) {
				Map<String, String> bluemap = new HashMap<>();
				bluemap.put("color", "0");
				bluemap.put("fleg", "0");
				bluemap.put("stop", "0");
				bluemap.put("dateNum", j + "");
				bluemap.put("proId", "");
				bluemap.put("name", "");
				bluemap.put("dayMax", "");
				bluemap.put("subjectnm", "");

				Map<String, String> yellowmap = new HashMap<>();
				yellowmap.put("color", "0");
				yellowmap.put("fleg", "0");
				yellowmap.put("stop", "0");
				yellowmap.put("dateNum", j + "");
				yellowmap.put("proId", "");
				yellowmap.put("name", "");
				yellowmap.put("dayMax", "");
				yellowmap.put("subjectnm", "");
				
				Map<String, String> redmap = new HashMap<>();
				redmap.put("color", "0");
				redmap.put("fleg", "0");
				redmap.put("stop", "0");
				redmap.put("dateNum", j + "");
				redmap.put("proId", "");
				redmap.put("name", "");
				redmap.put("dayMax", "");
				redmap.put("subjectnm", "");
				
				//获取每个专题各个流程的最大值和最小值，添加标记
				if(j==blmin){
					bluemap.put("startDay", "");
				}
				if(j==blmax){
					bluemap.put("endDay", "");
				}
				if(j==ylmin){
					yellowmap.put("startDay", "");
				}
				if(j==ylmax){
					yellowmap.put("endDay", "");
				}
				if(j==remin){
					redmap.put("startDay", "");
				}
				if(j==remax){
					redmap.put("endDay", "");
				}
				
				int blue = 0;
				int yellow = 0;
				int red = 0;
				for (int k = 0; k < collec.size(); k++) {
					if (collec.get(k).getQueryDt() == j && id==collec.get(k).getSubjectId()) {
						if ("数据入库".equals(collec.get(k).getProcess())) {
							if (collec.get(k).getStatus() > blue) {
								blue = collec.get(k).getStatus();
								blueday = j;
							}
							bluemap.put("dayMax", dBuleEndTime);
							bluemap.put("proId", "1");
							bluemap.put("name", "数据入库");
							bluemap.put("subjectnm", ztnm);
						}
						if ("模型执行".equals(collec.get(k).getProcess())) {
							if (collec.get(k).getStatus() > yellow) {
								yellow = collec.get(k).getStatus();
							}
							yellowmap.put("dayMax", yellowEndTime);
							yellowmap.put("proId", "3");
							yellowmap.put("name", "模型执行");
							yellowmap.put("subjectnm", ztnm);
						}
						if ("报告生成".equals(collec.get(k).getProcess())) {
							if (collec.get(k).getStatus() > red) {
								red = collec.get(k).getStatus();
								redday = j;
							}
							redmap.put("proId", "4");
							redmap.put("name", "报告生成");
							redmap.put("dayMax", rRedEndTime);
							redmap.put("subjectnm", ztnm);
						}
					}
					// 数据入库
				}
				// 根据参数判断集合需要封装的具体值
				if (blue == 1) {
					bluemap.put("color", "1");
				}
				if (blue == 2) {
					bluemap.put("color", "1");
					bluemap.put("fleg", "1");
				}
				if (blue == 3) {
					bluemap.put("stop", "1");
				}
				// 模型执行判断
				if (yellow == 1) {
					yellowmap.put("color", "1");
				}
				if (yellow == 2) {
					yellowmap.put("color", "1");
					yellowmap.put("fleg", "1");
				}
				if (yellow == 3) {
					yellowmap.put("stop", "1");
				}
				// 报告生成判断
				if (red == 1) {
					redmap.put("color", "1");
				}
				if (red == 2) {
					redmap.put("color", "1");
					redmap.put("fleg", "1");
				}
				if (red == 3) {
					redmap.put("stop", "1");
				}
				Map<String, Object> hashMap = new HashMap<>();
				hashMap.put("blue", bluemap);// 这是封装数据的map，基本map
				hashMap.put("yellow", yellowmap);
				hashMap.put("red", redmap);
				Object[] obj = new Object[1];
				obj[0] = hashMap;
				mapzt.put("dateNum" + j, obj);
			}
			// 数据入库核查显示
			for (int k = 3; k > 0; k--) {
				if (blueday != 0) {
					if (blueday + k <= dayOfMonth) {
						Object[] object = (Object[]) mapzt.get("dateNum" + (blueday + k));
						if (object != null) {
							Map<String, Object> a = (Map<String, Object>) object[0];
							Object object2 = a.get("blue");
							Map<String, String> b = (Map<String, String>) object2;
							if((blueday + k)==blmax){
								b.put("endDay", "");
							}
							
							b.put("color", "2");
							b.put("proId", "2");
							b.put("name", "数据入库核查");
							b.put("dayMax", lBuleEndTime);
							b.put("subjectnm", ztnm);
						}
					}
				}
			}
			// 报告生成核查显示
			for (int k = 3; k > 0; k--) {
				if (redday != 0) {
					if (redday + k <= dayOfMonth) {
						Object[] object = (Object[]) mapzt.get("dateNum" + (redday + k));
						if (object != null) {
							Map<String, Object> a = (Map<String, Object>) object[0];
							Object object2 = a.get("red");
							Map<String, String> b = (Map<String, String>) object2;
							if((redday + k)==remax){
								b.put("endDay", "");
							}
							b.put("color", "2");
							b.put("proId", "5");
							b.put("name", "报告生成核查");
							b.put("dayMax", pRedEndTime);
							b.put("subjectnm", ztnm);
						}
					}
				}
			}
			out[i] = mapzt;
		}
		return tableData;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 下钻方法查询三个主要流程
	public Object getDep(String month, String ztId, String process) {
		// 获取到专题名称
		String ztmc = "";
		List<GRSJzt> getzt = getzt();
		for (int i = 0; i < getzt.size(); i++) {
			Map<String, String> ma = (Map<String, String>) getzt.get(i);
			if (ztId.equals(ma.get("id"))) {
				ztmc = ma.get("name");
			}
		}
		GRSJMapper grsjMapper = mybatisDao.getSqlSession().getMapper(GRSJMapper.class);
		List<GRSJData> collec = grsjMapper.getDep(month, ztId);
		int dayOfMonth = getTime(month);
		// 修改最大时间
		// 修改最大时间
		List<GRSJsf> sf = grsjMapper.getsf();
		// ******1次调用数据库，储存所有省份所有审计流程的最大天数
		if (13 == Integer.parseInt(ztId)) {
			// 获取最大时间map
			Map<String, Object> maxDay = getMaxDay(month, ztId, process);
			GRSJsf g1 = new GRSJsf();
			g1.setSfId(13200);
			g1.setSfNm("广州支撑中心");
			sf.add(g1);
			GRSJsf g2 = new GRSJsf();
			g2.setSfId(13300);
			g2.setSfNm("深圳支撑中心");
			sf.add(g2);
			GRSJsf g3 = new GRSJsf();
			g3.setSfId(13400);
			g3.setSfNm("北京支撑中心");
			sf.add(g3);
			GRSJsf g4 = new GRSJsf();
			g4.setSfId(13500);
			g4.setSfNm("集团网络部");
			sf.add(g4);
			GRSJsf g5 = new GRSJsf();
			g5.setSfId(20008);
			g5.setSfNm("中移在线公司");
			sf.add(g5);
			Object[] out = new Object[sf.size()];
			// 建立最外侧map
			Map<String, Object> tableData = new HashMap<>();
			tableData.put("tableData", out);
			for (int i = 0; i < sf.size(); i++) {
				// 开始循环省份了
				String sfnm = sf.get(i).getSfNm();
				// 获取省份Id
				int sfId = sf.get(i).getSfId();
				Map<String, String> maxV = (Map<String, String>) maxDay.get(sfId + "");
				String sjrk = maxV.get("数据入库");
				String mxzx = maxV.get("模型执行");
				String bgsc = maxV.get("报告生成");
				Map<String, Object> mapsf = new HashMap<>();
				mapsf.put("id", sfId);
				mapsf.put("subjectName", sfnm);
				mapsf.put("dateSum", dayOfMonth);
				// 定义30个map装进去
				// 获取省份的各流程最大值
				for (int j = 1; j < dayOfMonth + 1; j++) {
					Map<String, Object> evday = new HashMap<>();
					// 定义一个数组，装color
					int[] color = new int[3];
					// 定义一个数组长度为3 ，数组中装map。map中放color。zxlc，完成时间
					Object[] sj = new Object[3];
					Map<String, String> lmap1 = new HashMap<>();
					Map<String, String> lmap2 = new HashMap<>();
					Map<String, String> lmap3 = new HashMap<>();
					// 初始化map，加入相关键值
					lmap1.put("color", "0");
					lmap1.put("zxlc", "");
					lmap1.put("time", "");
					lmap2.put("color", "0");
					lmap2.put("zxlc", "");
					lmap2.put("time", "");
					lmap3.put("color", "0");
					lmap3.put("zxlc", "");
					lmap3.put("time", "");
					// 将map放入数组
					sj[0] = lmap1;
					sj[1] = lmap2;
					sj[2] = lmap3;
					evday.put("color", 0);
					evday.put("fleg", "0");
					evday.put("stop", "0");
					evday.put("prvdId", sf.get(i).getSfId() + "");
					evday.put("prvdName", sfnm);
					evday.put("dayNum", j + "");
					evday.put("ztmc", ztmc);
					evday.put("process", "");
					evday.put("data", sj);
					// 定义五种流程的完成时间
					int sum = 0;
					for (int k = 0; k < collec.size(); k++) {
						if (collec.get(k).getQueryDt() == j && sfId==collec.get(k).getPrvdId()) {
							if ("数据入库".equals(collec.get(k).getProcess())) {
								sum = sum + (int) Math.pow(1, 2);
								evday.put("process", "数据入库");
								evday.put("dayMax", sjrk);
								lmap1.put("color", "1");
								lmap1.put("zxlc", "数据入库");
								lmap1.put("time", sjrk);
								if (collec.get(k).getStatus() == 2) {
									lmap1.put("zxlc", "数据入库异常");
									evday.put("fleg", "1");
									lmap1.put("time", "");
								}
								if (collec.get(k).getStatus() == 3) {
									evday.put("stop", "1");
								}
								// **************************如果暂停？
							}
							if ("模型执行".equals(collec.get(k).getProcess())) {
								sum = sum + (int) Math.pow(3, 2);
								evday.put("process", "模型执行");
								evday.put("dayMax", mxzx);
								lmap2.put("color", "3");
								lmap2.put("zxlc", "模型执行");
								lmap2.put("time", mxzx);
								if (collec.get(k).getStatus() == 2) {
									lmap2.put("zxlc", "模型执行异常");
									lmap2.put("time", "");
									evday.put("fleg", "1");
								}
								if (collec.get(k).getStatus() == 3) {
									evday.put("stop", "1");
								}
								// **************************如果暂停？
							}
							if ("报告生成".equals(collec.get(k).getProcess())) {
								sum = sum + (int) Math.pow(4, 2);
								lmap3.put("color", "4");
								lmap3.put("zxlc", "报告生成");
								lmap3.put("time", bgsc);
								evday.put("process", "报告生成");
								evday.put("dayMax", bgsc);
								if (collec.get(k).getStatus() == 2) {
									lmap3.put("zxlc", "报告生成异常");
									lmap3.put("time", "");
									evday.put("fleg", "1");
								}
								if (collec.get(k).getStatus() == 3) {
									evday.put("stop", "1");
								}
							}
						}
					}
					evday.put("color", sum);
					Object[] obj = new Object[1];
					obj[0] = evday;
					mapsf.put("dateNum" + j, obj);
				}
				out[i] = mapsf;
			}
			return tableData;
		}
		// 客户信息安全流程结束
		// ********
		// 从数据库获取专题id和名称，存在一个list中
		// 获取省份id与nm
		Map<String, Object> maxDay = getMaxDay(month, ztId, process);
		Object[] out = new Object[sf.size()];
		// 建立最外侧map
		Map<String, Object> tableData = new HashMap<>();
		tableData.put("tableData", out);
		for (int i = 0; i < sf.size(); i++) {
			// 开始循环省份了
			String sfnm = sf.get(i).getSfNm();
			// 获取省份Id
			int sfId = sf.get(i).getSfId();
			Map<String, String> maxV = (Map<String, String>) maxDay.get(sfId + "");
			String sjrk = maxV.get("数据入库");
			String mxzx = maxV.get("模型执行");
			String bgsc = maxV.get("报告生成");
			Map<String, Object> mapsf = new HashMap<>();
			mapsf.put("id", sfId);
			mapsf.put("subjectName", sfnm);
			mapsf.put("dateSum", dayOfMonth);
			// 定义30个map装进去
			// 获取省份的各流程最大值
			for (int j = 1; j < dayOfMonth + 1; j++) {
				Map<String, Object> evday = new HashMap<>();
				// 定义一个数组，装color
				int[] color = new int[3];
				// 定义一个数组长度为3 ，数组中装map。map中放color。zxlc，完成时间
				Object[] sj = new Object[3];
				Map<String, String> lmap1 = new HashMap<>();
				Map<String, String> lmap2 = new HashMap<>();
				Map<String, String> lmap3 = new HashMap<>();
				// 初始化map，加入相关键值
				lmap1.put("color", "0");
				lmap1.put("zxlc", "");
				lmap1.put("time", "");
				lmap2.put("color", "0");
				lmap2.put("zxlc", "");
				lmap2.put("time", "");
				lmap3.put("color", "0");
				lmap3.put("zxlc", "");
				lmap3.put("time", "");
				// 将map放入数组
				sj[0] = lmap1;
				sj[1] = lmap2;
				sj[2] = lmap3;
				evday.put("color", 0);
				evday.put("fleg", "0");
				evday.put("stop", "0");
				evday.put("prvdId", sf.get(i).getSfId() + "");
				evday.put("prvdName", sfnm);
				evday.put("dayNum", j + "");
				evday.put("ztmc", ztmc);
				evday.put("process", "");
				evday.put("data", sj);
				// 封装数据
				int sum = 0;
				for (int k = 0; k < collec.size(); k++) {
					if (collec.get(k).getQueryDt() == j && sfId==collec.get(k).getPrvdId()) {
						if ("数据入库".equals(collec.get(k).getProcess())) {
							sum = sum + (int) Math.pow(1, 2);
							evday.put("process", "数据入库");
							evday.put("dayMax", sjrk);
							lmap1.put("color", "1");
							lmap1.put("zxlc", "数据入库");
							lmap1.put("time", sjrk);
							if (collec.get(k).getStatus() == 2) {
								lmap1.put("zxlc", "数据入库异常");
								lmap1.put("time", "");
								evday.put("fleg", "1");
							}
							if (collec.get(k).getStatus() == 3) {
								evday.put("stop", "1");
							}
							// **************************如果暂停？
						}
						if ("模型执行".equals(collec.get(k).getProcess())) {
							sum = sum + (int) Math.pow(3, 2);
							evday.put("process", "模型执行");
							evday.put("dayMax", mxzx);
							lmap2.put("color", "3");
							lmap2.put("zxlc", "模型执行");
							lmap2.put("time", mxzx);
							if (collec.get(k).getStatus() == 2) {
								lmap2.put("zxlc", "模型执行异常");
								lmap2.put("time", "");
								evday.put("fleg", "1");
							}
							if (collec.get(k).getStatus() == 3) {
								evday.put("stop", "1");
							}
							// **************************如果暂停？
						}
						if ("报告生成".equals(collec.get(k).getProcess())) {
							sum = sum + (int) Math.pow(4, 2);
							lmap3.put("color", "4");
							lmap3.put("zxlc", "报告生成");
							lmap3.put("time", bgsc);
							evday.put("process", "报告生成");
							evday.put("dayMax", bgsc);
							if (collec.get(k).getStatus() == 2) {
								lmap3.put("zxlc", "报告生成异常");
								lmap3.put("time", "");
								evday.put("fleg", "1");
							}
							if (collec.get(k).getStatus() == 3) {
								evday.put("stop", "1");
							}
						}
					}
				}
				evday.put("color", sum);
				Object[] obj = new Object[1];
				obj[0] = evday;
				mapsf.put("dateNum" + j, obj);
			}
			out[i] = mapsf;
		}
		return tableData;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 获取审计月
	public List<Object> getSJY() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < 6; i++) {
			Map<String, String> hashMap = new HashMap<>();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -i);
			Date m = c.getTime();
			String mon = format.format(m);
			String year = mon.substring(0, 4);
			String month = mon.substring(4);
			String vl = year + "年" + month + "月";
			hashMap.put("id", mon);
			hashMap.put("name", vl);
			list.add(hashMap);
		}
		return list;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 获取专题
	public List getzt() {
		GRSJMapper grsjMapper = mybatisDao.getSqlSession().getMapper(GRSJMapper.class);
		List<GRSJzt> zt = new ArrayList<GRSJzt>();// 专题数组
		// 建立外层数组
		zt = grsjMapper.getZt();
		List<GRSJzt> zt1 = new ArrayList<>();
		zt1.add(zt.get(1));
		zt1.add(zt.get(2));
		zt.remove(1);
		zt.remove(1);
		zt.add(zt1.get(0));
		zt.add(zt1.get(1));
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < zt.size(); i++) {
			Map<String, String> map = new HashMap<>();
			Integer ztId = zt.get(i).getZtId();
			String ztIds = ztId + "";
			String ztNm = zt.get(i).getZtNm();
			map.put("id", ztIds);
			map.put("name", ztNm);
			list.add(map);
		}
		return list;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 各流程最大完成时间
	public Map<String, Object> getMaxDay(String month, String ztid, String process) {
		// 获取总数据
		GRSJMapper grsjMapper = mybatisDao.getSqlSession().getMapper(GRSJMapper.class);
		List<GRSJData> collec = grsjMapper.getDep(month, ztid);
		// 获取下月天数
		int numOfmon = getTime(month);
		// 获取省份列表
		List<GRSJsf> sf = grsjMapper.getsf();
		// 客户信心安全专题
		if (Integer.parseInt(ztid) == 13) {
			GRSJsf g1 = new GRSJsf();
			g1.setSfId(13200);
			g1.setSfNm("广州支撑中心");
			sf.add(g1);
			GRSJsf g2 = new GRSJsf();
			g2.setSfId(13300);
			g2.setSfNm("深圳支撑中心");
			sf.add(g2);
			GRSJsf g3 = new GRSJsf();
			g3.setSfId(13400);
			g3.setSfNm("北京支撑中心");
			sf.add(g3);
			GRSJsf g4 = new GRSJsf();
			g4.setSfId(13500);
			g4.setSfNm("集团网络部");
			sf.add(g4);
			GRSJsf g5 = new GRSJsf();
			g5.setSfId(20008);
			g5.setSfNm("中移在线公司");
			sf.add(g5);
			Map<String, Object> m1 = new HashMap<>();
			// 遍历省份
			for (int i = 0; i < sf.size(); i++) {
				int sfId = sf.get(i).getSfId();
				int dBuleEndTime = 0;
				int yellowEndTime = 0;
				int rRedEndTime = 0;
				int interEndTime = 0;
				Map<String, String> m2 = new HashMap<>();
				// 遍历数据，查询该省最大完成天数
				for (int j = 0; j < collec.size(); j++) {
					if (sfId==collec.get(j).getPrvdId()) {
						if ("数据入库".equals(collec.get(j).getProcess())) {
							if (collec.get(j).getQueryDt() > dBuleEndTime) {
								dBuleEndTime = collec.get(j).getQueryDt();
							}
						}
						if ("模型执行".equals(collec.get(j).getProcess())) {
							if (collec.get(j).getQueryDt() > yellowEndTime) {
								yellowEndTime = collec.get(j).getQueryDt();
							}
						}
						if ("报告生成".equals(collec.get(j).getProcess())) {
							if (collec.get(j).getQueryDt() > rRedEndTime) {
								rRedEndTime = collec.get(j).getQueryDt();
							}
						}
						if ("接口重传".equals(collec.get(j).getProcess())) {
							if (collec.get(j).getQueryDt() > interEndTime) {
								interEndTime = collec.get(j).getQueryDt();
							}
						}
					}
				}
				int lBuleEndTime = 0;
				for (int j = 3; j > 0; j--) {
					if ((dBuleEndTime + j) <= numOfmon) {
						lBuleEndTime = dBuleEndTime + j;
						break;
					}
				}
				int pRedEndTime = 0;

				for (int j = 3; j > 0; j--) {
					if ((rRedEndTime + j) <= numOfmon) {
						pRedEndTime = rRedEndTime + j;
						break;
					}
				}
				String dBuleEndTime1 = "";
				String lBuleEndTime1 = "";
				String yellowEndTime1 = "";
				String rRedEndTime1 = "";
				String pRedEndTime1 = "";
				String interEndTime1 = "";
				if (dBuleEndTime < 10) {
					dBuleEndTime1 = month + "0" + dBuleEndTime;
				} else {
					dBuleEndTime1 = month + dBuleEndTime;
				}
				if (lBuleEndTime < 10) {
					lBuleEndTime1 = month + "0" + lBuleEndTime;
				} else {
					lBuleEndTime1 = month + lBuleEndTime;
				}
				if (yellowEndTime < 10) {
					yellowEndTime1 = month + "0" + yellowEndTime;
				} else {
					yellowEndTime1 = month + yellowEndTime;
				}
				if (rRedEndTime < 10) {
					rRedEndTime1 = month + "0" + rRedEndTime;
				} else {
					rRedEndTime1 = month + rRedEndTime;
				}
				if (pRedEndTime < 10) {
					pRedEndTime1 = month + "0" + pRedEndTime;
				} else {
					pRedEndTime1 = month + pRedEndTime;
				}
				if (interEndTime < 10) {
					interEndTime1 = month + "0" + interEndTime;
				} else {
					interEndTime1 = month + interEndTime;
				}
				m2.put("数据入库", dBuleEndTime1);
				m2.put("sjmax", dBuleEndTime + "");
				m2.put("数据入库核查", lBuleEndTime1);
				m2.put("模型执行", yellowEndTime1);
				m2.put("报告生成", rRedEndTime1);
				m2.put("报告生成核查", pRedEndTime1);
				m2.put("bgmax", rRedEndTime + "");
				m2.put("接口重传", interEndTime1);
				m1.put(sfId + "", m2);
			}
			return m1;
		}
		// 其他专题
		Map<String, Object> m1 = new HashMap<>();
		// 遍历省份
		for (int i = 0; i < sf.size(); i++) {
			int sfId = sf.get(i).getSfId();
			int dBuleEndTime = 0;
			int yellowEndTime = 0;
			int rRedEndTime = 0;
			int interEndTime = 0;
			Map<String, String> m2 = new HashMap<>();
			// 遍历数据，查询该省最大完成天数
			for (int j = 0; j < collec.size(); j++) {
				if (sfId==collec.get(j).getPrvdId()) {
					if ("数据入库".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > dBuleEndTime) {
							dBuleEndTime = collec.get(j).getQueryDt();
						}
					}
					if ("模型执行".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > yellowEndTime) {
							yellowEndTime = collec.get(j).getQueryDt();
						}
					}
					if ("报告生成".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > rRedEndTime) {
							rRedEndTime = collec.get(j).getQueryDt();
						}
					}
					if ("接口重传".equals(collec.get(j).getProcess())) {
						if (collec.get(j).getQueryDt() > interEndTime) {
							interEndTime = collec.get(j).getQueryDt();
						}
					}
				}
			}
			int lBuleEndTime = 0;
			for (int j = 3; j > 0; j--) {
				if ((dBuleEndTime + j) <= numOfmon) {
					lBuleEndTime = dBuleEndTime + j;
					break;
				}
			}
			int pRedEndTime = 0;

			for (int j = 3; j > 0; j--) {
				if ((rRedEndTime + j) <= numOfmon) {
					pRedEndTime = rRedEndTime + j;
					break;
				}
			}
			String dBuleEndTime1 = "";
			String lBuleEndTime1 = "";
			String yellowEndTime1 = "";
			String rRedEndTime1 = "";
			String pRedEndTime1 = "";
			String interEndTime1 = "";
			if (dBuleEndTime < 10) {
				dBuleEndTime1 = month + "0" + dBuleEndTime;
			} else {
				dBuleEndTime1 = month + dBuleEndTime;
			}
			if (lBuleEndTime < 10) {
				lBuleEndTime1 = month + "0" + lBuleEndTime;
			} else {
				lBuleEndTime1 = month + lBuleEndTime;
			}
			if (yellowEndTime < 10) {
				yellowEndTime1 = month + "0" + yellowEndTime;
			} else {
				yellowEndTime1 = month + yellowEndTime;
			}
			if (rRedEndTime < 10) {
				rRedEndTime1 = month + "0" + rRedEndTime;
			} else {
				rRedEndTime1 = month + rRedEndTime;
			}
			if (pRedEndTime < 10) {
				pRedEndTime1 = month + "0" + pRedEndTime;
			} else {
				pRedEndTime1 = month + pRedEndTime;
			}
			if (interEndTime < 10) {
				interEndTime1 = month + "0" + interEndTime;
			} else {
				interEndTime1 = month + interEndTime;
			}
			m2.put("数据入库", dBuleEndTime1);
			m2.put("数据入库核查", lBuleEndTime1);
			m2.put("sjmax", dBuleEndTime + "");
			m2.put("模型执行", yellowEndTime1);
			m2.put("报告生成", rRedEndTime1);
			m2.put("报告生成核查", pRedEndTime1);
			m2.put("接口重传", interEndTime1);
			m2.put("bgmax", rRedEndTime + "");
			m1.put(sfId + "", m2);
		}
		return m1;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 获取本月天数
	public int getTime(String month) {
		String year1 = month.substring(0, 4);
		String month1 = month.substring(4);
		int iyear = Integer.parseInt(year1);
		int imonth = Integer.parseInt(month1);
		Calendar c = Calendar.getInstance();
		c.set(iyear, imonth, 0);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		return dayOfMonth;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 获取核查流程信息
	public Object getDepChe(String month, String ztid, String process) {
		GRSJMapper grsjMapper = mybatisDao.getSqlSession().getMapper(GRSJMapper.class);
		// 获取下个月天数
		int numOfmon = getTime(month);
		// 获取入库和核查的最大完成天数
		Map<String, Object> maxDay = getMaxDay(month, ztid, process);
		// 获取省份集合
		// 建立最外侧map
		// 判断专题是否是客户信息安全
		if (Integer.parseInt(ztid) == 13) {
			List<GRSJsf> sf = grsjMapper.getsf();
			// 获取专题并判断专题名称
			List<GRSJzt> zt = grsjMapper.getZt();
			String ztnm = "";
			for (int i = 0; i < zt.size(); i++) {
				if ((zt.get(i).getZtId() + "").equals(ztid)) {
					ztnm = zt.get(i).getZtNm();
				}
			}
			GRSJsf g1 = new GRSJsf();
			g1.setSfId(13200);
			g1.setSfNm("广州支撑中心");
			sf.add(g1);
			GRSJsf g2 = new GRSJsf();
			g2.setSfId(13300);
			g2.setSfNm("深圳支撑中心");
			sf.add(g2);
			GRSJsf g3 = new GRSJsf();
			g3.setSfId(13400);
			g3.setSfNm("北京支撑中心");
			sf.add(g3);
			GRSJsf g4 = new GRSJsf();
			g4.setSfId(13500);
			g4.setSfNm("集团网络部");
			sf.add(g4);
			GRSJsf g5 = new GRSJsf();
			g5.setSfId(20008);
			g5.setSfNm("中移在线公司");
			sf.add(g5);
			Object[] out = new Object[sf.size()];
			Map<String, Object> tableData = new HashMap<>();
			tableData.put("tableData", out);
			for (int i = 0; i < sf.size(); i++) {
				// 开始循环省份
				String sfnm = sf.get(i).getSfNm();
				int sfid = sf.get(i).getSfId();
				// 获取该省核查流程的最大值
				Map<String, String> maxV = (Map<String, String>) maxDay.get(sfid + "");
				// 获取省份Id
				Integer sfId = sf.get(i).getSfId();
				Map<String, Object> mapsf = new HashMap<>();
				mapsf.put("id", sfId);
				mapsf.put("subjectName", sfnm);
				mapsf.put("dateSum", numOfmon);
				String sjrkhc = maxV.get("数据入库核查");
				String sjrkNum = maxV.get("sjmax");
				String bgschc = maxV.get("报告生成核查");
				String bgscNum = maxV.get("bgmax");
				
				for (int j = 1; j < numOfmon + 1; j++) {
					Map<String, Object> evday = new HashMap<>();
					evday.put("fleg", "0");
					evday.put("stop", "0");
					evday.put("prvdId", sfId + "");
					evday.put("prvdName", sfnm);
					evday.put("dayNum", j + "");
					evday.put("ztmc", ztnm);
					evday.put("dayMax", "");
					
					Object[] sj = new Object[2];
					Map<String, String> lmap1 = new HashMap<>();
					Map<String, String> lmap2 = new HashMap<>();
					// 初始化map，加入相关键值
					lmap1.put("color", "0");
					lmap1.put("zxlc", "");
					lmap1.put("time", "");
					lmap2.put("color", "0");
					lmap2.put("zxlc", "");
					lmap2.put("time", "");
					// 将map放入数组
					sj[0] = lmap1;
					sj[1] = lmap2;
					evday.put("data",sj);
					int sum = 0;
					//添加data
					if (Integer.parseInt(sjrkNum) != 0) {
						if (j > Integer.parseInt(sjrkNum) && j <= (Integer.parseInt(sjrkNum) + 3)) {
							sum = sum + (int) Math.pow(2, 2);
							lmap1.put("color", "2");
							lmap1.put("zxlc", "数据入库核查");
							lmap1.put("time", sjrkhc);
							evday.put("dayMax", sjrkhc);
						}
					}
					if (Integer.parseInt(bgscNum) != 0) {
						if (j > Integer.parseInt(bgscNum) && j <= (Integer.parseInt(bgscNum) + 3)) {
							sum = sum + (int) Math.pow(5, 2);
							lmap2.put("color", "5");
							lmap2.put("zxlc", "报告生成核查");
							lmap2.put("time", bgschc);
							evday.put("dayMax", bgschc);
						}
					}
					evday.put("color", sum);
					Object[] obj = new Object[1];
					obj[0] = evday;
					mapsf.put("dateNum" + j, obj);
				}
				out[i] = mapsf;
			}
			return tableData;
		}
		// 客户信息安全流程结束
		// 开始普通流程
		List<GRSJsf> sf = grsjMapper.getsf();
		// 获取专题并判断专题名称
		List<GRSJzt> zt = grsjMapper.getZt();
		String ztnm = "";
		for (int i = 0; i < zt.size(); i++) {
			if ((zt.get(i).getZtId() + "").equals(ztid)) {
				ztnm = zt.get(i).getZtNm();
			}
		}
		Object[] out = new Object[sf.size()];
		Map<String, Object> tableData = new HashMap<>();
		tableData.put("tableData", out);
		for (int i = 0; i < sf.size(); i++) {
			// 开始循环省份
			String sfnm = sf.get(i).getSfNm();
			Integer sfid = sf.get(i).getSfId();
			// 获取该省核查流程的最大值
			Map<String, String> maxV = (Map<String, String>) maxDay.get(sfid + "");
			// 获取省份Id
			int sfId = sf.get(i).getSfId();
			Map<String, Object> mapsf = new HashMap<>();
			mapsf.put("id", sfId);
			mapsf.put("subjectName", sfnm);
			mapsf.put("dateSum", numOfmon);
			String sjrkhc = maxV.get("数据入库核查");
			String sjrkNum = maxV.get("sjmax");
			String bgschc = maxV.get("报告生成核查");
			String bgscNum = maxV.get("bgmax");
			for (int j = 1; j < numOfmon + 1; j++) {
				Map<String, Object> evday = new HashMap<>();
				evday.put("fleg", "0");
				evday.put("stop", "0");
				evday.put("prvdId", sfId + "");
				evday.put("prvdName", sfnm);
				evday.put("dayNum", j + "");
				evday.put("ztmc", ztnm);
				evday.put("dayMax", "");
				Object[] sj = new Object[2];
				Map<String, String> lmap1 = new HashMap<>();
				Map<String, String> lmap2 = new HashMap<>();
				// 初始化map，加入相关键值
				lmap1.put("color", "0");
				lmap1.put("zxlc", "");
				lmap1.put("time", "");
				lmap2.put("color", "0");
				lmap2.put("zxlc", "");
				lmap2.put("time", "");
				// 将map放入数组
				sj[0] = lmap1;
				sj[1] = lmap2;
				evday.put("data",sj);
				int sum = 0;
				if (Integer.parseInt(sjrkNum) != 0) {
					if (j > Integer.parseInt(sjrkNum) && j <= (Integer.parseInt(sjrkNum) + 3)) {
						sum = sum + (int) Math.pow(2, 2);
						lmap1.put("color", "2");
						lmap1.put("zxlc", "数据入库核查");
						lmap1.put("time", sjrkhc);
						evday.put("dayMax", sjrkhc);
					}
				}
				if (Integer.parseInt(bgscNum) != 0) {
					if (j > Integer.parseInt(bgscNum) && j <= (Integer.parseInt(bgscNum) + 3)) {
						sum = sum + (int) Math.pow(5, 2);
						lmap2.put("color", "5");
						lmap2.put("zxlc", "报告生成核查");
						lmap2.put("time", bgschc);
						evday.put("dayMax", bgschc);
					}
				}
				evday.put("color", sum);
				Object[] obj = new Object[1];
				obj[0] = evday;
				mapsf.put("dateNum" + j, obj);
			}
			out[i] = mapsf;
		}
		return tableData;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// 接口重传封装
	public Object getDepInter(String month, String ztid, String process) {
		GRSJMapper grsjMapper = mybatisDao.getSqlSession().getMapper(GRSJMapper.class);
		List<GRSJData> inter = grsjMapper.getInter(month, ztid);
		// 获取下个月天数
		int numOfmon = getTime(month);
		// 获取入库和核查的最大完成天数
		Map<String, Object> maxDay = getMaxDay(month, ztid, process);
		// 获取省份集合
		List<GRSJsf> sf = grsjMapper.getsf();
		// 获取专题并判断专题名称
		List<GRSJzt> zt = grsjMapper.getZt();
		String ztnm = "";
		for (int i = 0; i < zt.size(); i++) {
			if ((zt.get(i).getZtId() + "").equals(ztid)) {
				ztnm = zt.get(i).getZtNm();
			}
		}
		// 建立最外侧map
		// 判断专题是否是客户信息安全
		if (Integer.parseInt(ztid) == 13) {
			GRSJsf g1 = new GRSJsf();
			g1.setSfId(13200);
			g1.setSfNm("广州支撑中心");
			sf.add(g1);
			GRSJsf g2 = new GRSJsf();
			g2.setSfId(13300);
			g2.setSfNm("深圳支撑中心");
			sf.add(g2);
			GRSJsf g3 = new GRSJsf();
			g3.setSfId(13400);
			g3.setSfNm("北京支撑中心");
			sf.add(g3);
			GRSJsf g4 = new GRSJsf();
			g4.setSfId(13500);
			g4.setSfNm("集团网络部");
			sf.add(g4);
			GRSJsf g5 = new GRSJsf();
			g5.setSfId(20008);
			g5.setSfNm("中移在线公司");
			sf.add(g5);
			Object[] out = new Object[sf.size()];
			Map<String, Object> tableData = new HashMap<>();
			tableData.put("tableData", out);
			for (int i = 0; i < sf.size(); i++) {
				// 开始循环省份
				String sfnm = sf.get(i).getSfNm();
				// 获取该省核查流程的最大值
				// 获取省份Id
				int sfId = sf.get(i).getSfId();
				Map<String, String> maxV = (Map<String, String>) maxDay.get(sfId + "");
				Map<String, Object> mapsf = new HashMap<>();
				mapsf.put("id", sfId);
				mapsf.put("subjectName", sfnm);
				mapsf.put("dateSum", numOfmon);
				String jkcc = maxV.get("接口重传");
				for (int j = 1; j < numOfmon + 1; j++) {
					Map<String, Object> evday = new HashMap<>();
					evday.put("fleg", "0");
					evday.put("stop", "0");
					evday.put("prvdId", sfId + "");
					evday.put("prvdName", sfnm);
					evday.put("dayNum", j + "");
					evday.put("ztmc", ztnm);
					evday.put("dayMax", "");
					Object[] sj = new Object[1];
					Map<String, String> lmap1 = new HashMap<>();
					// 初始化map，加入相关键值
					lmap1.put("color", "0");
					lmap1.put("zxlc", "");
					lmap1.put("time", "");
					// 将map放入数组
					sj[0] = lmap1;
					evday.put("data",sj);
					int sum = 0;
					for (int k = 0; k < inter.size(); k++) {
						if (sfId == inter.get(k).getPrvdId() && j == inter.get(k).getQueryDt()) {
							sum = sum + (int) Math.pow(6, 2);
							lmap1.put("color", "6");
							lmap1.put("zxlc", "接口重传");
							lmap1.put("time", jkcc);
							evday.put("dayMax", jkcc);
						}
					}
					evday.put("color", sum);
					Object[] obj = new Object[1];
					obj[0] = evday;
					mapsf.put("dateNum" + j, obj);
				}
				out[i] = mapsf;
			}
			return tableData;
		}
		Object[] out = new Object[sf.size()];
		Map<String, Object> tableData = new HashMap<>();
		tableData.put("tableData", out);
		// 客户信息安全流程结束
		// 开始普通流程
		for (int i = 0; i < sf.size(); i++) {
			// 开始循环省份
			String sfnm = sf.get(i).getSfNm();
			Integer sfid = sf.get(i).getSfId();
			// 获取该省核查流程的最大值
			Map<String, String> maxV = (Map<String, String>) maxDay.get(sfid + "");
			// 获取省份Id
			int sfId = sf.get(i).getSfId();
			Map<String, Object> mapsf = new HashMap<>();
			mapsf.put("id", sfId);
			mapsf.put("subjectName", sfnm);
			mapsf.put("dateSum", numOfmon);
			String jkcc = maxV.get("接口重传");
			for (int j = 1; j < numOfmon + 1; j++) {
				Map<String, Object> evday = new HashMap<>();
				evday.put("fleg", "0");
				evday.put("stop", "0");
				evday.put("prvdId", sfId + "");
				evday.put("prvdName", sfnm);
				evday.put("dayNum", j + "");
				evday.put("ztmc", ztnm);
				evday.put("dayMax", "");
				Object[] sj = new Object[1];
				Map<String, String> lmap1 = new HashMap<>();
				// 添加data
				lmap1.put("color", "0");
				lmap1.put("zxlc", "");
				lmap1.put("time", "");
				sj[0] = lmap1;
				evday.put("data",sj);
				int sum = 0;
				for (int k = 0; k < inter.size(); k++) {
					int dt = inter.get(k).getQueryDt();
					int id = inter.get(k).getPrvdId();
					if (sfId == id && j == dt) {
						sum = sum + (int) Math.pow(6, 2);
						lmap1.put("color", "6");
						lmap1.put("zxlc", "接口重传");
						lmap1.put("time", jkcc);
						evday.put("dayMax", jkcc);
					}
				}
				evday.put("color", sum);
				Object[] obj = new Object[1];
				obj[0] = evday;
				mapsf.put("dateNum" + j, obj);
			}
			out[i] = mapsf;
		}
		return tableData;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// 获取审计月下个月的年月字段
	public String getNextMon(String month) {
		String year1 = month.substring(0, 4);
		String month1 = month.substring(4);
		int iyear = Integer.parseInt(year1);
		int imonth = Integer.parseInt(month1);
		String nemon = "";
		if ((imonth + 1) > 12) {
			iyear = iyear + 1;
			imonth = (imonth + 1) % 12;
		} else {
			imonth = imonth + 1;
		}
		if (imonth < 10) {
			nemon = iyear + "" + "0" + imonth;
		} else {
			nemon = iyear + "" + imonth;
		}
		return nemon;
	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
