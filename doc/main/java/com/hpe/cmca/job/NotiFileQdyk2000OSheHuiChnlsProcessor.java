package com.hpe.cmca.job;

import org.springframework.stereotype.Service;

@Service("NotiFileQdyk2000OSheHuiChnlsProcessor")
public class NotiFileQdyk2000OSheHuiChnlsProcessor extends NotiFileQdyk2000AllChnlsProcessor {
	@Override
	public boolean generate() throws Exception{
		// TODO Auto-generated method stub
		this.setChnlClass("2");
		return super.generate();
	}
}
