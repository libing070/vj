<template>
  <div class="promap">
    <el-row style="margin: 20px">
      <el-col :span="24" style="">
        <el-card :body-style="{ padding: '0px' }">
          <el-carousel :interval="4000" type="card" height="200px">
            <el-carousel-item v-for="item in 3" :key="item">
              <a href="#" >
                <img class="" style="width: 100%;height: 100%;objec-fit: cover;" :src="require('./../../assets/images/about/promap1_'+item+'.png')" alt="">
              </a>
            </el-carousel-item>
          </el-carousel>
          <div style="padding: 14px;text-align: center">
            <span>智能扫地机器人机顶盒</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin: 20px">
      <el-col :span="24" style="">
        <el-card :body-style="{ padding: '0px' }">
          <el-carousel :interval="4000" type="card" height="200px">
            <el-carousel-item v-for="item in 3" :key="item">
              <a href="#" >
                <img class="" style="width: 100%;height: 100%;objec-fit: cover;" :src="require('./../../assets/images/about/promap2_'+item+'.png')" alt="">
              </a>
            </el-carousel-item>
          </el-carousel>
          <div style="padding: 14px;text-align: center">
            <span>安防监控摄像头控制板</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin: 20px">
      <el-col :span="24" style="">
        <el-card :body-style="{ padding: '0px' }">
          <el-carousel :interval="4000" type="card" height="200px">
            <el-carousel-item v-for="item in 5" :key="item">
              <a href="#" >
                <img class="" style="width: 100%;height: 100%;objec-fit: cover;" :src="require('./../../assets/images/about/promap3_'+item+'.png')" alt="">
              </a>
            </el-carousel-item>
          </el-carousel>
          <div style="padding: 14px;text-align: center">
            <span>开关电源5W-60W医疗电源400W车充类</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "chanpinshitu"
    }
</script>

<style scoped>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
