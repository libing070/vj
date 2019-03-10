<template>
  <div class="about-eqint">
    <el-row>
      <el-col :span="8" style="padding: 10px">
        <el-card class="el-card-1" :body-style="{ padding: '0px' }">
          <span class="span-title">DEK 印刷机</span>
          <img :src="require('./../../assets/images/about/eqint1.png')" class="image">
          <div style="padding:10px;">
            <div class="bottom clearfix">
              <el-row>
                <el-col :span="15" style="">
                  <span>参数：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px;"><i class="el-icon-star-on"></i><span>印刷精度：0.004mm</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>印刷速度：2～150mm/sec</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>刮刀压力：0～20kg</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>分离速度：0.1～20mm/sec</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>分离距离：0～3mm</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>特點：印刷速度快，精度高，品质稳定。</span></dt>
                  </dl>
                </el-col>
                <el-col :span="9" style="">
                  <span>制程控制重点：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>1、印刷偏移</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>2、少锡、漏印</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>3、連锡、塌锡</span></dt>
                  </dl>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="padding: 10px">
        <el-card class="el-card-1" :body-style="{ padding: '0px' }">
          <span class="span-title">日立高速模组机</span>
          <img :src="require('./../../assets/images/about/eqint2.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <el-row>
                <el-col :span="14" style="">
                  <span>参数：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px;"><i class="el-icon-star-on"></i><span>品牌：HITACHI（日立）</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>型号：GXH-3　</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>年份：2011</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>贴片速度：96000PCS/H</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>理论日均产能：200万点/天</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>贴片精度：±50μm</span></dt>
                  </dl>
                </el-col>
                <el-col :span="10" style="">
                  <span style="font-weight: bold">&nbsp;</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>适用基板：最大460*460mm</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>厚度：0.5-5.0mm</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>料架支持：400个站位（8*4）</span></dt>
                  </dl>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="padding: 10px">
        <el-card class="el-card-1" :body-style="{ padding: '0px' }">
          <span class="span-title">日立多功能机</span>
          <img :src="require('./../../assets/images/about/eqint3.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <el-row>
                <el-col :span="14" style="">
                  <span>参数：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px;"><i class="el-icon-star-on"></i><span>品牌：HITACHI（日立）</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>型号：GXH-1JS</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>年份：2011</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>贴片速度：24000PCS/H</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>理论日均产能：57万点/天</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>贴片精度：±50μm</span></dt>
                  </dl>
                </el-col>
                <el-col :span="10" style="">
                  <span style="font-weight: bold">&nbsp;</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>适用基板：最大460*460mm</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>厚度：0.5-5.0mm</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>料架支持：116个站位（8*4）</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span> 托盘：20个</span></dt>
                  </dl>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12" style="padding: 10px">
        <el-card class="el-card-1" :body-style="{ padding: '0px' }">
          <span class="span-title">回流焊</span>
          <img :src="require('./../../assets/images/about/eqint4.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <el-row>
                <el-col :span="24" style="">
                  <span>参数：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px;"><i class="el-icon-star-on"></i><span>功能：通过对锡膏高温加热,使锡膏熔化,再通过冷卻使焊錫重新固化,达到零件与PCB板PAD焊接的效果</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>温控精度：0.1℃</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>均温性：0.2℃</span></dt>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>温度設定范围：25℃～300℃</span></dt>
                  </dl>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" style="padding: 10px">
        <el-card class="el-card-1" :body-style="{ padding: '0px' }">
          <span class="span-title">AOI</span>
          <img :src="require('./../../assets/images/about/eqint5.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <el-row>
                <el-col :span="24" style="">
                  <span>参数：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px;"><i class="el-icon-star-on"></i><span>功能：对零件焊点的品质狀況,零件极性,有无偏移,錯件,空焊等進行自动检验确认</span></dt>
                  </dl>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" style="">
                  <span>制程控制重点：</span>
                  <dl>
                    <dt style="padding: 5px 0;font-size:12px" ><i class="el-icon-star-on"></i><span>1、程序编程的精度</span></dt>
                  </dl>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "gongsijieshao"
    }
</script>

<style scoped>
.about-eqint .image{
  width: 100%;
  max-height: 200px;
  min-width: 460px;
}
.about-eqint .el-card-1{
  min-height: 490px;
}
  .el-icon-star-on{
    padding-right: 10px;
  }
.about-eqint .span-title{
  font-weight: bold;
  display: block;
  padding: 10px;
  background-color: #f5f5f5
}
</style>
