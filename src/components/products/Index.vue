<template>
  <div class="products-index">
    <carousel></carousel>
    <el-card class="box-card" style="">
      <el-row>
        <el-col :span="4">
          <serach></serach>
        </el-col>
        <el-col :span="20">
          <list>1</list>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  import Carousel from './../../components/Carousel'
  import Serach from './Serach'
  import List from './list'
  import Detail from './detail'
    export default {
      name: "index",
      components:{
        Carousel:Carousel,
        Serach:Serach,
        List:List,
        Detail:Detail
      },
    }
</script>

<style lang="scss" scoped>
  @import "~css/products";

</style>
