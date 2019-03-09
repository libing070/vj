<template>
  <div class="about">
    <carousel></carousel>
    <el-card class="box-card" style="margin: 40px 30px">
      <el-row class="tac">
        <el-col :span="4">
          <el-menu
            style="min-height: 700px"
            active-text-color="#ffc908"
            text-color=""
            :unique-opened="true"
            default-active="1-1">
            <el-submenu index="1" >
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>关于我们</span>
              </template>
              <el-menu-item index="1-1" @click="jump(1)"><i class="iconfont icon-gongsijieshao"  style="padding-right: 5px;"></i><span>公司介绍</span></el-menu-item>
              <el-menu-item index="1-2" @click="jump(2)"><i class="iconfont icon-zuzhijiagou"  style="padding-right: 5px;"></i><span>组织架构</span></el-menu-item>
              <el-menu-item index="1-3" @click="jump(3)"><i class="iconfont icon-shebeijieshao"  style="padding-right: 5px;"></i><span>设备介绍</span></el-menu-item>
              <el-menu-item index="1-4" @click="jump(4)"><i class="iconfont icon-icon-shebeiqingdan"  style="padding-right: 5px;"></i><span>设备清单</span></el-menu-item>
              <el-menu-item index="1-5" @click="jump(5)"><i class="iconfont icon-liucheng"  style="padding-right: 5px;"></i ><span>工艺流程</span></el-menu-item>
              <el-menu-item index="1-6" @click="jump(6)"><i class="iconfont icon-tushi1"  style="padding-right: 5px;"></i><span>产品图示</span></el-menu-item>
              <el-menu-item index="1-7" @click="jump(7)"><i class="iconfont icon-lianxiwomen"  style="padding-right: 5px;"></i><span>联系我们</span></el-menu-item>
            </el-submenu>
          </el-menu>
        </el-col>
        <el-col :span="20">
          <router-view></router-view>
        </el-col>
      </el-row>
    </el-card>
    <nav-footer></nav-footer>
  </div>
</template>


<script>

  import Carousel from './../../components/Carousel'
  import NavFooter from './../../components/NavFooter'

  export default {
    name: "about",
    components:{
      Carousel:Carousel,
      NavFooter:NavFooter
    },
    methods: {
      jump(index){
        let path="/about/comp";
        if(index==1){
          path="/about/comp"//公司介绍
        }else if(index==2){
          path="/about/org"//组织架构
        }else if(index==3){
          path="/about/eqint"//设备介绍
        }else if(index==4){
          path="/about/eqlist"//设备清单
        }else if(index==5){
          path="/about/proflow"//工艺流程
        }else if(index==6){
          path="/about/promap"//产品图示
        }else if(index==7){
          path="/about/conus"//联系我们
        }
        this.$router.push({path:path});
      },
    }
  }
</script>

<style scoped>

</style>
