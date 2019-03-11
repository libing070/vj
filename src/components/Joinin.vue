<template>
  <div class="joinin" >
    <carousel></carousel>
    <div>
      <el-row>
       <el-col :span="24" class="el-row1">
          <span style="font-size: 14px;color: rgb(169, 169, 169);display: block;margin-top: 50px">About us</span>
          <span style="font-size: 42px;color: rgb(0, 0, 0);display: block;margin: 20px 0px">关于我们</span>
          <span class="message-span">东莞市鹏诚电子有限公司是始创于2013年，成立于2018年。</span>
          <span class="message-span">公司地址位于东莞市横沥镇上文前二路。</span>
          <span class="message-span">公司主营主要生产电源适配器，开关电源，机顶盒，扫地机器人，LED驱动，网络摄像头等相关电子产品。</span>
          <span class="message-span">公司集产品研发，生产和销售一体，其产品产品广泛应用于通讯，灯饰，家电，医疗，玩具等领域。</span>
          <span class="message-span">现因生产需要，招聘如下岗位：</span>
        </el-col>
       </el-row>
      <el-row  style="background-color: #eeeeee;height: 640px">
        <!--<el-col :span="24">
          <el-card class=""  style="width: 90%;height: 450px;top: 0;left: 0;right: 0;bottom: 0;margin: auto;position: absolute">
            <el-row>
              <el-col :span="12">
                <img src="./../assets/images/joinin/1.png" />
              </el-col>
              <el-col :span="12">

              </el-col>
            </el-row>
          </el-card>
        </el-col>-->
        <el-col :span="12" style="margin-top:30px ">
          <el-card  style="margin:10px 30px;">
            <div slot="header" class="clearfix">
              <span style="font-weight: bold"> 一.招聘职位(Contact Us)：</span>
            </div>
            <div  class="text item">
              <p> 1.普通员工: 初中以上学历，曾在电子厂工作，服从安排，能吃苦耐劳，有焊锡或者LQC经验者优先录用。</p>
              <p> 2.品管员/仓管员/生产领班 初中以上学历，在电子厂同等职位工作一年以上，有电源工厂工作经验的优先录取。</p>
            </div>
          </el-card >
          <el-card  style="margin:10px 30px;">
            <div slot="header" class="clearfix">
              <span style="font-weight: bold">二.薪资福利（Wages  And Benefits)：</span>
            </div>
            <div  class="text item">
              <p>公司实行5天8小时工作制，底薪：1750元+加班费（加班费参照东莞市劳动法计算)。</p>
              <p>生活补贴：180元（如在公司就餐每月需扣170元就餐按照26天制)。</p>
              <p>岗位津贴：50-1000元，优秀员工奖:100元。</p>
              <p>全勤奖：50元.绩效奖：100-1000元。</p>
              <p>年资奖：1200-2400/年.特别津贴：0-500元。</p>
              <p>夜班津贴：15元/天。</p>
            </div>
          </el-card >
          <el-card  style="margin:10px 30px;">
            <div slot="header" class="clearfix">
              <span style="font-weight: bold"> 三.文化活动（Cultural Activities）：</span>
            </div>
            <div  class="text item">
              <p>公司是一个年轻态的公司，员工平均年龄30岁，公司经常组织户外活动，如户外烧烤，拓展，生日晚会等。 公司宿舍配合卫生间，冲凉房，衣柜等必要设施。</p>
            </div>
          </el-card >
        </el-col>
        <el-col :span="12">
          <el-carousel :interval="5000" arrow="always" style="margin: 60px 30px ;">
            <el-carousel-item v-for="item in 4" :key="item">
              <a href="#" >
                <img class="" style="width: 100%;height: 100%;objec-fit: cover;" :src="require('./../assets/images/joinin/'+item+'.png')" alt=""></a>
            </el-carousel-item>
          </el-carousel>
          <el-row style="margin: 15px 30px;color: #151515">
            <span style="width: 100px">招聘专线：</span><span>13128041736 </span>
          </el-row>
          <el-row style="margin: 15px 30px;color: #151515">
            <span style="width: 100px">联系人：</span><span>刘先生</span>
          </el-row>
          <el-row style="margin: 0 30px ;">
            <span style="font-size: 24px;line-height: 32px;color: #151515;letter-spacing: .5px;opacity: 1;">欢迎加入我们的团队!</span>
              <el-button style="float: right;background-color: #409EFF;color: #ffffff">加入我们</el-button>
          </el-row>
          <el-row style="margin: 15px 30px;height: 2px;background-color: #409EFF;">
          </el-row>
        </el-col>
      </el-row>

    </div>
  </div>
</template>

<script>

  import Carousel from './../components/Carousel'
  export default {
        name: "joinin",
      components:{
        Carousel:Carousel
      },
    }
</script>

<style lang="scss">
    .el-row1{
      text-align: center;
      .message-span{
        color :rgb(75, 75, 75);
        display: block;
        font-size: 20px;
        padding: 5px 0px;
      }
    }
    .el-carousel__item h3 {
      color: #475669;
      font-size: 18px;
      opacity: 0.75;
      line-height: 300px;
      margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
      background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
      background-color: #d3dce6;
    }
    .joinin .el-card__body,
    .joinin .el-card__header{
      padding: 10px 20px !important;
    }
    .text {
      font-size: 12px;
    }

    .clearfix:before,
    .clearfix:after {
      display: table;
      content: "";
    }
    .clearfix:after {
      clear: both
    }
</style>
