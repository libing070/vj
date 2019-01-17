package com.hpe.cmca.interfaces;

import java.util.List;

import com.hpe.cmca.pojo.PrvdInfoData;


public interface PrvdInfoMapper {

	//获取全国31个省份的ID和名字
	public List<PrvdInfoData> getPrvdInfoData (int prvdId);
}
