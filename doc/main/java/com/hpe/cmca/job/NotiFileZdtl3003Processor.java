package com.hpe.cmca.job;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("NotiFileZdtl3003Processor")
public class NotiFileZdtl3003Processor extends NotiFileZdtl3000Processor {
	
	public boolean generate() throws Exception{
		this.setFocusCd("3003");
		this.setFileName("社会渠道终端异常销售及套利（代理商空串码）排名");
		writeSheet3003(notiFileGenService.getNotiFile3000Data(month, "3003"));
		return true;
	}
	
	public void writeSheet3003(List<Map<String, Object>> data){
		String[] columnNames = {"省份名称","代理商空串码异常销售数量","代理商空串码涉及渠道数量","代理商空串码套利终端数量","套利金额","代理商空串码套利涉及渠道数量","代理商空串码占总销量比","代理商空串码套利终端占代理商空串码终端比","代理商空串码占比排名","代理商空串码套利终端占比排名"};
		writeSheetCommon(data, "代理商空串码套利", "社会渠道代理商空串码终端及套利情况", columnNames);
	}
}
