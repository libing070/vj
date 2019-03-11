<template>
  <div>
    <!--<el-row class="">
      <el-col :span="24">
        <img style="vertical-align: middle;width: 70px;" src="./../assets/images/navheader/loggs.png" />
      </el-col>
    </el-row>-->
    <el-row class="nav-header">
      <el-col :span="4">
        <img style="vertical-align: middle;width: 70px;" src="./../assets/images/navheader/logs.png" />
      </el-col>
      <el-col :span="20">
        <el-menu :default-active="activeIndex" class="" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1" @click="jump(1)">首页</el-menu-item>
        <!--  <el-submenu index="2">
            <template slot="title">相关产品</template>
              <el-row class="el-menu-list">
                <el-col style="text-align: center;font-weight: bold">待开放中。。。。。</el-col>
              </el-row>
          </el-submenu>-->
          <el-menu-item index="2" @click="jump(2)">相关产品</el-menu-item>
          <el-menu-item index="3" @click="jump(3)">技术文库</el-menu-item>
          <el-menu-item index="4" @click="jump(4)"><a>关于鹏诚</a></el-menu-item>
          <el-menu-item index="5" @click="jump(5)"><a>诚聘英才</a></el-menu-item>
          <el-menu-item index="6" @click="jump(6)"><a>投资人专区</a></el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "nav-header",
        data() {
          return {
            activeIndex: '1',
          };
        },
        created(){

         },
         mounted() {
           this.initNav();
           var path=window.location.href;
           if(path.indexOf("index")!=-1){
             this.activeIndex=1;
           }else if(path.indexOf("products")!=-1){
             this.activeIndex=2;
           }else if(path.indexOf("about")!=-1){
             this.activeIndex=4;
           }else if(path.indexOf("joinin")!=-1){
             this.activeIndex=5;
           }
         },
      watch: {
        $route(to, from) {
          console.log(this.$route.path);
        }
      },
      methods:{
           jump(index){
             let path="/index";
             if(index==1){
                path="/index"//默认首页
             }else if(index==2){
               path="/products"//相关产品
             }else if(index==3){
              this.alertMsg();
              return;
             }else if(index==4){
               path="/about"//关于我们
             }else if(index==5){
                path="/joinin"//诚聘英才
             }else if(index==6){
               this.alertMsg();
               return;
             }

             this.$router.push({path:path});
           },
          initNav(){
          },
           alertMsg(){
             this.$message({
               message: '暂未开放，敬请期待！',
               type: 'warning'
             });
           },
           handleSelect(key, keyPath) {
             console.log(key, keyPath);
           }
      }
    }
</script>
<style lang="scss">
  @import "~css/navheader";
  .el-menu--horizontal>.el-submenu .el-submenu__title,
  .el-menu--horizontal>.el-menu-item{
    font-size: 16px;
    height: 120px !important;
    line-height: 120px !important;
  }
  .el-menu--horizontal>.el-menu-item.is-active{
    //border-bottom: 2px solid #FFC908;
    border-bottom: 2px solid #409EFF;
    color: #409EFF;
  }
  .el-menu-list{
    width: 600px;
    height: 300px;
    background-color: #F5F5F5;
    margin-top: -6px;
    margin-bottom: -5px;
  }
</style>
