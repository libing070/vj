<template>
  <div class="products-detail" style="margin: 20px">
    <el-row>
    <el-col :span="2" style="float: right">
      <el-button type="danger" size="mini" @click="productDetailsClick()">返回</el-button>
    </el-col>
    </el-row>
    <el-row style="margin: 10px">
      <el-col :span="10" >
        <img style="height: 280px;width:280px" :src="require('./../../assets/images/products/icon/'+productsList.imageurl+'.png')">
      </el-col>
      <el-col :span="14">
        <span style="display:block;font-size: 18px;font-weight: bold;padding: 5px;margin-bottom: 10px">产品特色</span>
          <span style="display: block;padding: 3px;">{{productsList.feature.feature3}}</span>
        <span style="display: block;padding: 3px;">{{productsList.feature.feature5}}</span>
          <span style="display: block;padding: 3px;">{{productsList.feature.feature6}}突波电压耐受能力</span>
        <span style="display: block;padding: 3px;">机壳尺寸: {{productsList.dimensions.LxWxH}}(长*宽*高)/厘米</span>
        <span style="display: block;padding: 3px;">安全认证：{{productsList.dimensions.Safety}}</span>
      </el-col>
    </el-row>
    <el-row style="background-color: #f56c6c;text-align: center;border-radius: 5px 5px 0 0;color: #fff;padding: 10px 0 ">
      <el-col :span="8">规格</el-col><el-col :span="16"></el-col>
    </el-row>
    <el-row class="warp">
      <el-row class="tablecontent "><el-col class="border-right" :span="8">输出功率</el-col><el-col class="text" :span="16">{{productsList.wattage}}</el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">输出电压</el-col><el-col  class="text" :span="16">{{productsList.dimensions.OutputVoltage}}</el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">输入电压</el-col><el-col  class="text" :span="16">{{productsList.dimensions.InputVoltage}}</el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">浪涌电流</el-col><el-col  class="text" :span="16"></el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">漏电流</el-col><el-col  class="text" :span="16"></el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">无载功率损耗	</el-col><el-col  class="text" :span="16">{{productsList.feature.feature4}}</el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">负载调节率	</el-col><el-col  class="text" :span="16"></el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">连波与噪声	</el-col><el-col  class="text" :span="16"></el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">能效等级	</el-col><el-col  class="text" :span="16">{{productsList.feature.feature3}}</el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">电磁干扰	</el-col><el-col  class="text" :span="16"><span  v-for="(l ,i) in productsList.safetyemc" :key="i">{{l}}</span></el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">抗静电等级	</el-col><el-col  class="text" :span="16"></el-col></el-row>
      <el-row class="tablecontent "><el-col class="border-right" :span="8">突波电压	</el-col><el-col  class="text" :span="16"></el-col></el-row>
      <el-row class="tablecontent ind"><el-col class="border-right" :span="8">平均失效间隔时间</el-col><el-col  class="text" :span="16"></el-col></el-row>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "detail",
      data () {
        return {

        }
      },
      computed:{
        productsList:{
          get(){
            return  this.$store.state.productsDetails;
          },
          set: function (newValue) {
            this.$store.state.productsDetails = newValue;
          }
        },
      } ,
      mounted() {
      },
      created(){
      },
      methods:{
        productDetailsClick(){
          this.$store.commit('productListOrDetailPageStore',0);//列表页面
        }
      }
    }
</script>

<style scoped>
  .warp{
    border: 1px solid #ebeef5;
  }
  .warp .tablecontent{

    text-align: center;
    color: #606266;
    border-bottom: 1px solid #ebeef5;
  }
  .warp .ind{
    border-bottom: none;
  }
  .warp .border-right{
     padding:10px 0;
    border-right: 1px solid #ebeef5;
  }
  .warp .text{
    padding:11px 0;
    text-align: center;
    font-size: 14px;
  }
</style>
