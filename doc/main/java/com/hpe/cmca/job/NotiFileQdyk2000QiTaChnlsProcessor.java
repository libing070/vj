package com.hpe.cmca.job;

import org.springframework.stereotype.Service;

@Service("NotiFileQdyk2000QiTaChnlsProcessor")
public class NotiFileQdyk2000QiTaChnlsProcessor extends NotiFileQdyk2000AllChnlsProcessor {
	@Override
	public boolean generate() throws Exception{
		// TODO Auto-generated method stub
		this.setChnlClass("qt");
		return super.generate();
	}
}
