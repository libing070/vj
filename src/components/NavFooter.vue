<template>
  <span>
     <el-row class="footer" >
    <el-col :span="24">
        <img style="vertical-align: middle;width: 300px;" src="./../assets/images/navheader/loggs.png" />
    </el-col>
  </el-row>
  <el-row class="footer" >
    <el-col :span="24">
      110 110 110（周一至周五 09：00-18：00） 广东省东莞市横沥镇上文前二路
    </el-col>
  </el-row>
  <el-row class="footer">
    <el-col :span="24">
      Copyright © 2019 pc Inc. 保留所有权利。粤公网安备 02010802022978号 | 公共事务邮箱 xxxx@163.com 商务合作邮箱 xxxx@163.com
    </el-col>
  </el-row>
  </span>
</template>

<script>
    export default {
        name: "nav-footer"
    }
</script>

<style scoped>
  .footer{
    background-color: #F5F5F5;
    padding: 10px 0px 10px 0px;
    text-align: center;font-size: 12px;
  }
</style>
