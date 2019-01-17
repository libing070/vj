package com.hpe.cmca.util;

import com.hpe.cmca.service.SSOService;
import com.hpe.cmca.service.YwyjService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.junit.Test;

public class JunitTest extends BaseJunitTest{

	@Autowired
	YwyjService ywyjService;

	@Autowired
	SSOService ssoService;
//	@Test
//    public void test0(){
//        System.out.println("第一个测试方法*******");
//    }

	//@Test
	public void test1() throws JAXBException{

		System.out.println("第一个测试方法*******");

		//System.out.println(ywyjService.genSql("hpbus.aa","cc|dd","ff|gg|qq","a=0"));

//		YWYJParameterData parameterData = new YWYJParameterData();
//		parameterData.setControlId("T_00_002");
//		parameterData.setShowField("cc|dd");
//		parameterData.setTableName("hpbus.aa");
//		parameterData.setScreenField("prvd_id");
//		ywyjService.genSql(parameterData);

//		LoginData loginData = new LoginData();
//		loginData.setLoginTimes(1);
//		loginData.setPrvdId("10000");
//		loginData.setUserId("hp_test");

		//System.out.println(ssoService.getLoginInfo(loginData));
		//ssoService.updateLoginData("hp_test",10000,1);
		String text = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta http-equiv=\"Content-Style-Type\" content=\"text/css\" /><meta name=\"generator\" content=\"Aspose.Words for Java 18.9\" /><title></title></head><body><div><div style=\"-aw-headerfooter-type:header-primary\"><p style=\"margin-top:0pt; margin-bottom:10pt; line-height:115%; font-size:11pt\"><span style=\"height:0pt; display:block; position:absolute; z-index:-65536\"><img src=\"Aspose.Words.0d068499-18a6-4d7e-a1c5-3f4498cd0d2c.001.png\" width=\"576\" height=\"314\" alt=\"\" style=\"margin-top:239.66pt; -aw-left-pos:0pt; -aw-rel-hpos:margin; -aw-rel-vpos:margin; -aw-top-pos:0pt; -aw-wrap-type:none; position:absolute\" /></span><img src=\"Aspose.Words.0d068499-18a6-4d7e-a1c5-3f4498cd0d2c.002.png\" width=\"576\" height=\"314\" alt=\"\" style=\"-aw-left-pos:0pt; -aw-rel-hpos:margin; -aw-rel-vpos:margin; -aw-top-pos:0pt; -aw-wrap-type:none; position:absolute; z-index:0\" /><span style=\"font-family:Calibri; -aw-import:ignore\">&#xa0;</span></p></div><p style=\"margin-top:0pt; margin-bottom:10pt; line-height:115%; font-size:12pt\"><span style=\"font-family:Calibri; font-weight:bold; color:#ff0000\">Evaluation Only. Created with Aspose.Words. Copyright 2003-2018 Aspose Pty Ltd.</span></p><p style=\"margin-top:0pt; margin-bottom:10pt; text-align:center; line-height:115%; font-size:22pt\"><span style=\"font-family:黑体; vertical-align:10pt\">审计报告</span></p><p style=\"margin-top:30pt; margin-bottom:10pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt\">审计关注事项：客户信息安全</span></p><p style=\"margin-top:15pt; margin-bottom:10pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt\">被审计单位：</span><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt; -aw-import:spaces\">&#xa0; </span><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt\">全公司</span></p><p style=\"margin-top:15pt; margin-bottom:10pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt\">审计期间：</span><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt; -aw-import:spaces\">&#xa0;&#xa0;&#xa0; </span><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt\">2018年09月01日-2018年09月30日</span></p><p style=\"margin-top:15pt; margin-bottom:10pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; font-weight:bold; vertical-align:10pt\">审计发现：</span></p><p style=\"margin-top:15pt; margin-bottom:10pt; text-indent:30pt; line-height:115%; font-size:16pt\"><span style=\"font-family:黑体; vertical-align:10pt\">一、系统权限管理违规</span></p><p style=\"margin-top:7.5pt; margin-bottom:10pt; text-indent:30pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; vertical-align:10pt\">审计关注未经授权操作客户敏感信息、违规为厂商人员授予敏感信息操作权限等违规行为。审计期间，全公司共产生客户敏感信息操作日志0条，其中，未经授权操作客户敏感信息的日志8条，占比0%；厂商人员违规操作客户敏感信息的日志12条，占比150.00%。截至审计时点，全公司共0个工号具备敏感信息操作权限，其中违规为0个厂商人员授予敏感信息操作权限，占比0%。</span></p><p style=\"margin-top:15pt; margin-bottom:10pt; text-indent:30pt; line-height:115%; font-size:16pt\"><span style=\"font-family:黑体; vertical-align:10pt\">二、金库模式管理违规</span></p><p style=\"margin-top:7.5pt; margin-bottom:10pt; text-indent:30pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; vertical-align:10pt\">审计关注敏感操作未经金库模式审批、金库授权时长超规定、金库模式审批人设置不当等违规行为。审计期间，全公司共产生客户敏感信息操作日志0条，其中，未经金库模式审批的日志12条，占比0%；金库授权时长超规定的日志20条，占比0%;金库模式审批人设置不当的日志0条，占比0%。</span></p><p style=\"margin-top:40pt; margin-bottom:10pt; text-align:justify; line-height:115%; font-size:16pt\"><span style=\"font-family:仿宋_GB2312; vertical-align:10pt\">编制部门：总部内审部</span><span style=\"font-family:仿宋_GB2312; vertical-align:10pt; -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0; </span><span style=\"font-family:仿宋_GB2312; vertical-align:10pt\">编制日期：2018年09月28日</span></p><div style=\"-aw-headerfooter-type:footer-primary\"><p style=\"margin-top:0pt; margin-bottom:10pt; line-height:115%; font-size:12pt\"><span style=\"font-family:Calibri; font-weight:bold; color:#ff0000\">Created with an evaluation copy of Aspose.Words. To discover the full versions of our APIs please visit: https://products.aspose.com/words/</span></p></div></div></body></html>";

		Matcher m = Pattern.compile("(<[^<]+Aspose[^>]+>)").matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {

			System.out.println( m.group(1));

			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		System.out.println(sb.toString());

	}

}
