<template>
  <div class="carousel">
    <el-row>
      <el-col>
        <el-carousel :interval="5000" arrow="always">
          <el-carousel-item v-for="item in 5" :key="item">
            <a href="#" >
              <img style="width: 100%;height: 100%;objec-fit: cover;" :src="require('./../assets/images/carousel/banner'+item+'_h_m.jpg')" alt=""></a>
          </el-carousel-item>
        </el-carousel>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "carousel",
      data () {
        return {
        }
      },
      created(){
        this.loadBanner();
      },
      methods:{
          loadBanner(){

          }
      }
    }
</script>

<style scoped>
  .carousel{
/*
    margin-top: 3px;
*/

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

</style>
