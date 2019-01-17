package com.hpe.cmca.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YwyjOVData {

    List<Lv1Info> lv1= new ArrayList<Lv1Info>();

    public List<Lv1Info> getLv1() {
        return lv1;
    }

    public void setLv1(List<Lv1Info> lv1) {
        this.lv1 = lv1;
    }

    public class Lv1Info{
        String lv1Code;
        String lv1Name;
        List<Lv2Info> lv2= new ArrayList<Lv2Info>();
        public String getLv1Name() {
            return lv1Name;
        }

        public void setLv1Name(String lv1Name) {
            this.lv1Name = lv1Name;
        }

        public String getLv1Code() {
            return lv1Code;
        }

        public void setLv1Code(String lv1Code) {
            this.lv1Code = lv1Code;
        }

        public List<Lv2Info> getLv2() {
            return lv2;
        }

        public void setLv2(List<Lv2Info> lv2) {
            this.lv2 = lv2;
        }
    }
    public class Lv2Info{
        String lv2Code;
        String lv2Name;
        List<PointInfo> point= new ArrayList<PointInfo>();

        public String getLv2Name() {
            return lv2Name;
        }

        public void setLv2Name(String lv2Name) {
            this.lv2Name = lv2Name;
        }


        public String getLv2Code() {
            return lv2Code;
        }

        public void setLv2Code(String lv2Code) {
            this.lv2Code = lv2Code;
        }

        public List<PointInfo> getPoint() {
            return point;
        }

        public void setPoint(List<PointInfo> point) {
            this.point = point;
        }
    }
    public class PointInfo{
        String pointCode;
        String pointName;

        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getPointCode() {
            return pointCode;
        }

        public void setPointCode(String pointCode) {
            this.pointCode = pointCode;
        }
    }
}
