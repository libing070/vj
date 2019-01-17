package com.hpe.cmca.interfaces;

import com.hpe.cmca.pojo.CompareTag;

import java.util.List;
import java.util.Map;

public interface CompareTagMapper {

    public List<CompareTag>  getCompareTag(Map<String,Object> tpMap);

    public int  updateTagValue(Map<String,Object> tpMap);

    public List<CompareTag>  getValueByTagId(Map<String,Object> tpMap);

    public List<CompareTag>  getChinaValueByTagId(Map<String,Object> tpMap);

    public List<Map<String,Object>>  getCompareRule(Map<String, Object> paraMap);

    public int  updateCompStatus(Map<String,Object> tpMap);

    public List<Map<String,Object>>  getCompareResult(Map<String, Object> paraMap);

    public int  updateCompStatusBy4Ids(Map<String,Object> tpMap);

    public int  updateCompareResultItr(List<Map<String, Object>> compareResultMapList);

    public int  updateTagValueItr(List<Map<String, Object>> tagValueMapList);
}
