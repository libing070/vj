<template>
  <div class="products-index">
    <carousel></carousel>
    <el-card class="box-card" style="">
      <el-row>
        <el-col :span="4">
          <serach></serach>
        </el-col>
        <el-col :span="20">
          <span v-if="isProductListPage">
            <list v-on:listentoProductListPageEvent="fromProductListPage"></list>
          </span>
          <span v-if="isProductDetailPage">
          <detail v-on:listentoProductDetailPageEvent="fromProductDetailPage"></detail>
        </span>
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
      data () {
        return {
          isProductListPage:true,
          isProductDetailPage:false
        }
      },
      watch:{
       },

      computed:{
      },
      mounted() {
      },
      created(){
      },
      methods:{
        fromProductListPage(data){
          this.isProductListPage=data;
          this.isProductDetailPage=true;
        },
        fromProductDetailPage(data){
          this.isProductDetailPage=data;
          this.isProductListPage=true;
        }
      },
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
