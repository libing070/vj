<template>
  <div class="products-detail" style="margin: 20px">
    <el-row>
    <el-col :span="2" style="float: right">
      <el-button type="danger" size="mini" @click="productDetailsClick()">返回</el-button>
    </el-col>
    </el-row>
    <el-row style="margin: 10px">
      <el-col :span="10" >
        <img style="height: 280px;width:280px" :src="imageurl">
      </el-col>
      <el-col :span="14">
        <span style="display:block;font-size: 18px;font-weight: bold;padding: 5px;margin-bottom: 10px">产品特色</span>
        <span  v-for="(list ,index) in productsList.feature" :key="index">
          <span style="display: block;padding: 3px;">{{index+1}}.{{list}}</span>
        </span>
      </el-col>
    </el-row>
    <el-row style="background-color: #f56c6c;text-align: center;border-radius: 5px 5px 0 0;color: #fff;padding: 10px 0 ">
      <el-col :span="8">规格</el-col><el-col :span="16"></el-col>
    </el-row>
    <el-row class="tablecontent"><el-col :span="8">输出功率</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">输出电压</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">输入电压</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">浪涌电流</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">漏电流</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">无载功率损耗	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">负载调节率	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">连波与噪声	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">能效等级	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">电磁干扰	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">抗静电等级	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">突波电压	</el-col><el-col :span="16"></el-col></el-row>
    <el-row class="tablecontent"><el-col :span="8">平均失效间隔时间</el-col><el-col :span="16"></el-col></el-row>
  </div>
</template>

<script>
    export default {
        name: "detail",
      data () {
        return {
          flag:false,
          productsList:{},
          imageurl:""
        }
      },
      mounted() {
        this.productsList=this.$store.state.productsList;
        console.log(this.productsList);
       this.imageurl=require('./../../assets/images/products/icon/'+this.productsList.imageurl+'.png');//这里图片单独处理

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
  .products-detail .tablecontent{
    border: 1px solid #ebeef5;
    padding:10px 0;
    text-align: center;
    color: #606266;
   }
</style>
