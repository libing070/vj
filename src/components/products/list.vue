<template>
  <div class="products-list" style="margin: 25px 50px;">
    <span v-for=" list in productsList">
      <el-card style="margin: 5px 0">
       <el-row>
        <el-col :span="6" style="height: 130px">
          <img style="height: 100%" class="" :src="require('./../../assets/images/about/aboutcomp1.png')" alt="">
        </el-col>
        <el-col :span="16" style="height: 130px">
          <span class="text title">{{list.name}} </span>
          <span class="text">输入电压:{{list.dimensions.OutputVoltage}}</span>
          <span class="text">额定输出: {{list.dimensions.OutputCurrent}}</span>
          <span class="text">输出功率: 60W</span>
        </el-col>
        <el-col :span="2" style="height: 130px;line-height: 130px">
          <el-button type="danger">详情</el-button>
        </el-col>
      </el-row>
     </el-card>
    </span>
  </div>
</template>

<script>
  import API from '../../api/api_products';
    export default {
        name: "list",
      data () {
        return {
         productsList:[]
        }
      },
      mounted() {
        this.search()
      },
      methods:{
        search(){
          let that = this;
          that.loading = true;
          API.findList().then(function (result) {
            that.loading = false;
            if(result){
              console.log(result.result.list);
              that.productsList=result.result.list;
              console.log(that.productsList);
            }
          }, function (err) {
            that.loading = false;
            that.$message.error({showClose: true, message: err.toString(), duration: 2000});
          }).catch(function (error) {
            that.loading = false;
            console.log(error);
            that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
          });
        },
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
