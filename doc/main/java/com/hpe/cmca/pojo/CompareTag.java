package com.hpe.cmca.pojo;

public class CompareTag {

    public Integer id;
    public String tagId;   //比对标签编码
    public String tagName;   //比对标签名称
    public String tagBelongPrvdId;   //标签所属省份编码
    public String tagBelongSubId;   //标签所属专题编码
    public Integer tagType;   //标签类型0-文件取值1-现有标签计算
    public Integer tagFileType;   //标签文件类型0-排名汇总1-审计报告
    public String tagZipPath;   //标签文件压缩包路径
    public String tagFileNm;   //标签文件名称
    public String tagSheetNm;   //标签sheet名称报告不填
    public Integer tagLocationType;   //标签坐标类型0-直取（汇总）1-动态判断（分省）报告不填
    public String tagPrvdLocation;   //标签省份首坐标报告、直取不填
    public String tagLocation;   //标签首坐标报告、直取不填
    public Integer tagParagr; //标签所处段落序号 0开始
    public String tagAffix;   //标签前后缀排名汇总不填
    public String tagComputRule;   //标签计算规则
    public String tagValue;   //标签值

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagBelongPrvdId() {
        return tagBelongPrvdId;
    }

    public void setTagBelongPrvdId(String tagBelongPrvdId) {
        this.tagBelongPrvdId = tagBelongPrvdId;
    }

    public String getTagBelongSubId() {
        return tagBelongSubId;
    }

    public void setTagBelongSubId(String tagBelongSubId) {
        this.tagBelongSubId = tagBelongSubId;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getTagFileType() {
        return tagFileType;
    }

    public void setTagFileType(Integer tagFileType) {
        this.tagFileType = tagFileType;
    }

    public String getTagZipPath() {
        return tagZipPath;
    }

    public void setTagZipPath(String tagZipPath) {
        this.tagZipPath = tagZipPath;
    }

    public String getTagFileNm() {
        return tagFileNm;
    }

    public void setTagFileNm(String tagFileNm) {
        this.tagFileNm = tagFileNm;
    }

    public String getTagSheetNm() {
        return tagSheetNm;
    }

    public void setTagSheetNm(String tagSheetNm) {
        this.tagSheetNm = tagSheetNm;
    }

    public Integer getTagLocationType() {
        return tagLocationType;
    }

    public void setTagLocationType(Integer tagLocationType) {
        this.tagLocationType = tagLocationType;
    }

    public String getTagPrvdLocation() {
        return tagPrvdLocation;
    }

    public void setTagPrvdLocation(String tagPrvdLocation) {
        this.tagPrvdLocation = tagPrvdLocation;
    }

    public String getTagLocation() {
        return tagLocation;
    }

    public void setTagLocation(String tagLocation) {
        this.tagLocation = tagLocation;
    }

    public Integer getTagParagr() {
        return tagParagr;
    }

    public void setTagParagr(Integer tagParagr) {
        this.tagParagr = tagParagr;
    }

    public String getTagAffix() {
        return tagAffix;
    }

    public void setTagAffix(String tagAffix) {
        this.tagAffix = tagAffix;
    }

    public String getTagComputRule() {
        return tagComputRule;
    }

    public void setTagComputRule(String tagComputRule) {
        this.tagComputRule = tagComputRule;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
