<template>
  <div>
  <!--  <carousel></carousel>-->
    <div class="joinin-bg1">
    <el-row >
      <el-col :span="12" class="title" style="color: red">

          1

    </el-col>
      <el-col :span="12">
2
      </el-col>
    </el-row>
    </div>
    <nav-footer></nav-footer>
  </div>
</template>

<script>

  import Carousel from './../components/Carousel'
  import NavFooter from './../components/NavFooter'

  export default {
        name: "joinin",
      components:{
        Carousel:Carousel,
        NavFooter:NavFooter
      },
    }
</script>

<style lang="scss" scoped>
  .joinin-bg1{
    height: 630px;
    background: url("~img/joinin/joinin_bg.jpg") no-repeat;
    background-size: 100% auto;
    .el-row1{
      position: absolute;
    }
  }
</style>
