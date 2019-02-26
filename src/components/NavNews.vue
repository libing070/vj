<template>
  <el-row style="margin: 60px;" >
    <el-col :span="8">
      <h2>鹏诚最新消息</h2>
      <dl>
        <dt style="padding:5px;font-size: 14px"><i class="el-icon-caret-right"></i>鹏诚最新消息</dt>
        <dt style="padding:5px;font-size: 14px"><i class="el-icon-caret-right"></i>鹏诚最新消息.鹏诚最新消息.鹏诚最新消息</dt>
        <dt style="padding:5px;font-size: 14px"><i class="el-icon-caret-right"></i>鹏诚最新消息鹏诚最新消息鹏诚最新消息</dt>
      </dl>
    </el-col>
    <el-col :span="8">
      <img class="imgbanner" :src="require('./../assets/images/navnews/h_ir.png')" alt="">

    </el-col>
    <el-col :span="8">
      <img class="imgbanner"  :src="require('./../assets/images/navnews/h_tl.jpg')" alt="">
    </el-col>


  </el-row>
</template>

<script>
    export default {
        name: "nav-news"
    }
</script>

<style scoped>
  .imgbanner {
    cursor: pointer;
    transition: all 0.6s;
  }
  .imgbanner:hover{
    transform: scale(1.1); /* 放大1倍 */
  }

</style>
