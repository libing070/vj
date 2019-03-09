<template>
  <div class="about-comp">
    <el-row class="">
      <el-col :span="14" class="msg">
        <p style="font-size: 30px;color: rgb(0, 0, 0);display: block;margin: 20px 0px;text-align:center">认识鹏诚</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;东莞市鹏诚电子有限公司成立于2018年12月，公司生产基地位于东莞市横沥镇石涌工业区上文前二路22号，公司主要经营PCBA OEM（例：开关电源OEM，智能家居类PCBA OEM，机顶盒PCBA成品组装OEM 等）;</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMT配置三条1+1 日立 高速贴片生产线，单日产能400万点，人员配置20人；</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DIP线配置二条流水线，单日产能24K电源产品，人员配置70人;</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;公司配置多名具备行业10年以上工作经验的PE/IE/ME/QE工程师，另其中部分员工具备多岗位的工作技能（如：总检/焊锡/测试等），正因具备这些全能型的员工Team，才让我们保证低成本、高品质的运作模式，且收获客户的认可。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们秉承"保质保量，服务顾客"的经营宗旨，发扬"研于本业，精益求精"的工作精神，致力于对PCBA加工的高品质追求。</p>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们以精良的设备和雄厚的技术力量充分保证产品的品质，使产品的性能和外观达到同行业先进水平。以精益求精提升产品质量，以诚信务实完善客户服务。</p>
      </el-col>
      <el-col :span="10"style="margin: 90px 0">
        <img class="imglog" :src="require('./../../assets/images/about/aboutcomp1.png')" alt="">
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8" class="el-col-img">
        <img class="img" :src="require('./../../assets/images/about/aboutcomp2.png')" alt="">
      </el-col>
      <el-col :span="8" class="el-col-img">
        <img class="img" :src="require('./../../assets/images/about/aboutcomp3.png')" alt="">
      </el-col>
      <el-col :span="8" class="el-col-img">
        <img class="img" :src="require('./../../assets/images/about/aboutcomp4.png')" alt="">
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
  .msg p{
    margin: 10px 30px;
  }
  .about-comp .imglog{
    width: 100%;
    max-width: 500px;
    max-height: 500px;
  }
  .about-comp .el-col-img{
    text-align: center;
  }
  .about-comp .img{
    width: 100%;
    max-width: 250px;
    max-height: 187px;
  }
</style>
