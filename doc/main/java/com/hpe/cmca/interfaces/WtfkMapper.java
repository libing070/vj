package com.hpe.cmca.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hpe.cmca.pojo.WtfkData;
import com.hpe.cmca.pojo.WtfkDealPhoto;
import com.hpe.cmca.pojo.WtfkDealPojo;
import com.hpe.cmca.pojo.WtfkPhotod;
import com.hpe.cmca.pojo.WtfkPojo;
import com.hpe.cmca.pojo.WtfkProPhoto;

public interface WtfkMapper {

	public List<WtfkPojo> selectByUserId(HashMap<String, Object> params);

	public int insertQue(HashMap<String, Object> params);

	public List<Map<String, Object>> findPriority();

	public List<Map<String, Object>> findClass();

	public int deleteProblem(HashMap<String, Object> params);

	public List<WtfkData> dealProblemOne(HashMap<String, Object> params);

	public int dealProblemTwo(HashMap<String, Object> params);

	public List<WtfkDealPojo> selreqProblem(String pro_encod);

	public void insertDealPhoto(@Param("pro_encod") String pro_encod, @Param("deal_photo_name") Object object);

	public void insertDealAndBack(@Param("pro_encod") Object pro_encod, @Param("deal_pro_id") Object object);


	public int resolvedPro(Map<String, Object> params);

	/*public void updatePro(String pro_encod);*/

	public void updateProStatus(HashMap<String, Object> params);


	public int updateProblem(Map<String, Object> params);

	public List<WtfkPojo> selectByUserIdExport(String pro_encod);

	public List<WtfkPojo> selectByDown(HashMap<String, Object> params);

	public void insertProPhoto(@Param("pro_encod") Object object, @Param("pro_photo_name") String string);

	public void backProblemUploader(@Param("pro_encod") Object object, @Param("deal_photo_name") String string);

	public List<WtfkProPhoto> selectByCode(@Param("pro_encod") Object object);

	public List<WtfkDealPhoto> selectByDealCode(@Param("pro_encod") Object object);

	public void updateByCode(@Param("pro_encod") Object object, @Param("pro_photo_name") String string);

	public void updateByDealCode(@Param("pro_encod") Object object, @Param("deal_photo_name") String string);

	public List<WtfkPhotod> getProPhoto(@Param("pro_encod")String pro_encod);



	

}
