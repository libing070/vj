<template>
  <div style="margin: 20px">
    <span v-if="productsList.length<=0">
      <el-card>
        <el-row>
          <el-col :span="24"><h1 style="text-align: center">暂无数据！</h1></el-col>
        </el-row>
      </el-card>
    </span>
    <span v-else>
       <el-card  class="products-list" style="margin: 5px 0" v-for="(list ,index) in productsList" :key="index">
       <el-row>
        <el-col :span="6" style="height: 120px">
          <img style="height: 100%" class="" :src="require('./../../assets/images/products/icon/'+list.imageurl+'.png')" alt="">
        </el-col>
        <el-col :span="16" style="height: 120px">
          <span class="text title">{{list.name}} </span>
          <span class="text">输入电压:{{list.dimensions.OutputVoltage}}</span>
          <span class="text">额定输出: {{list.dimensions.OutputCurrent}}</span>
          <span class="text">输出功率: {{list.wattage}}w</span>
        </el-col>
        <el-col :span="2" style="height: 120px;line-height: 120px">
          <el-button type="danger" size="mini" @click="productListsClick(list)">详情</el-button>
        </el-col>
      </el-row>
      </el-card>

    </span>

  </div>
</template>

<script>
  import API from '../../api/api_products';
  import ElRow from "element-ui/packages/row/src/row";
    export default {
      components: {ElRow},
      name: "list",
      data () {
        return {
        }
      },
     computed:{
       productsList:{
         get(){
           return  this.$store.state.productsList;
         },
         set: function (newValue) {
           this.$store.state.productsList = newValue;
         }
       }
     } ,
      mounted() {
          this.search()
      },
      created(){


      },
      methods:{
        search(){
          let that = this;
          that.loading = true;
          API.findList().then(function (result) {
            that.loading = false;
            if(result){
              that.$store.commit('ProductsListStore',result.result.list);
              that.productsList=that.$store.state.productsList;
            }
          }, function (err) {
            that.loading = false;
            that.$message.error({showClose: true, message: err.toString(), duration: 2000});
          }).catch(function (error) {
            that.loading = false;
            that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
          });
        },
        productListsClick(currlist){
          this.$store.commit('productListOrDetailPageStore',1);//详情页面
          this.$store.commit('ProductsDetailsStore',currlist);
        }
      }
    }
</script>

<style scoped>
  .products-list .text{
   display: block;
    padding: 5px 0;
  }
  .products-list .title{
    font-size: 20px;
    font-weight: bold;
  }
</style>
