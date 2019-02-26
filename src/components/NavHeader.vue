<template>
  <div>
    <el-row class="nav-header">
      <el-col :span="5">
        <img style="vertical-align: middle;" src="./../assets/images/navheader/yuming_pic10.png" />
      </el-col>
      <el-col :span="15">
        <el-menu :default-active="activeIndex" class="" mode="horizontal" @select="handleSelect">
          <el-submenu index="1">
            <template slot="title">相关产品</template>
              <el-row class="el-menu-list">
                <el-col style="text-align: center;font-weight: bold">待开放中。。。。。</el-col>
              </el-row>
          </el-submenu>
          <el-menu-item index="2">技术文库</el-menu-item>
          <el-menu-item index="3"><a>关于鹏诚</a></el-menu-item>
          <el-menu-item index="4"><a>诚聘英才</a></el-menu-item>
          <el-menu-item index="5"><a>投资人专区</a></el-menu-item>
          <el-menu-item index="6"><a>联系我们</a></el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="4"></el-col>
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
         },
         methods:{
          initNav(){
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
    height: 80px !important;
    line-height: 80px !important;
  }
  .el-menu--horizontal>.el-menu-item.is-active{
    border-bottom: 2px solid #F3782A;
    color: #F3782A;
  }
  .el-menu-list{
    width: 600px;
    height: 300px;
    background-color: #F5F5F5;
    margin-top: -6px;
    margin-bottom: -5px;
  }
</style>
