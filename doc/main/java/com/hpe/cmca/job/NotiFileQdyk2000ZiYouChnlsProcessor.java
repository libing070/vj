package com.hpe.cmca.job;

import org.springframework.stereotype.Service;

@Service("NotiFileQdyk2000ZiYouChnlsProcessor")
public class NotiFileQdyk2000ZiYouChnlsProcessor extends NotiFileQdyk2000AllChnlsProcessor {
	@Override
	public boolean generate() throws Exception{
		// TODO Auto-generated method stub
		this.setChnlClass("1");
		return super.generate();
	}
}
