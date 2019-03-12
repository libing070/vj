<template>
  <div class="products-detail">
    <el-row>
    <el-col :span="24">
      <el-button type="danger" @click="productDetailsClick()">返回</el-button>
    </el-col>
    </el-row>
       {{productsList}}
  </div>
</template>

<script>
    export default {
        name: "detail",
      data () {
        return {
          flag:false,
          productsList:[]
        }
      },
      mounted() {
        this.productsList=this.$store.state.productsList;

      },
      created(){

      },
      methods:{
        productDetailsClick(){
          this.flag=this.$store.state.isProductListPage;
          this.$emit("listentoProductDetailPageEvent",this.flag);
        }
      }
    }
</script>

<style scoped>

</style>
