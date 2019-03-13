<template>
  <div class="products-index">
    <carousel></carousel>
    <el-card class="box-card" style="">
      <el-row>
        <el-col :span="4">
          <serach></serach>
        </el-col>
        <el-col :span="20">
          <span v-show="whichPage==0">
            <list></list>
          </span>
          <span v-if="whichPage==1">
             <detail ></detail>
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
        }
      },
      watch:{
       },
      computed:{
        whichPage:{
          get(){
            return  this.$store.state.productListOrDetailPage;
          },
          set: function (newValue) {
            this.$store.state.productListOrDetailPage = newValue;
          }
        }
      },
      mounted() {
      },
      created(){
      },
      methods:{
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
