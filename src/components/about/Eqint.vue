<template>
  <div class="about-eqint">
    <!--<el-row>
      <el-col :span="11" v-for="(o, index) in 4" :key="index" :offset="1">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="require('./../../assets/images/about/eqint'+Number(index+1)+'.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold">{{o}}</span>
            <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px;cursor: pointer;" v-for="i in o.list"><i class="el-icon-caret-right"></i>{{i}}</dt>
              </dl>

              <el-button type="text" class="button">更多</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>-->

    <el-row>
      <el-col :span="12" style="padding: 10px">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="require('./../../assets/images/about/eqint1.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px;cursor: pointer;" ><i class="el-icon-caret-right"></i></dt>
              </dl>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" style="padding: 10px">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="require('./../../assets/images/about/eqint2.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px;cursor: pointer;" ><i class="el-icon-caret-right"></i></dt>
              </dl>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12" style="padding: 10px">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="require('./../../assets/images/about/eqint3.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px;cursor: pointer;" ><i class="el-icon-caret-right"></i></dt>
              </dl>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" style="padding: 10px">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="require('./../../assets/images/about/eqint4.png')" class="image">
          <div style="padding: 10px;">
            <span style="font-weight: bold"></span>
            <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px;cursor: pointer;" ><i class="el-icon-caret-right"></i></dt>
              </dl>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        name: "gongsijieshao"
    }
</script>

<style scoped>
.about-eqint .image{
  width: 100%;
  max-height: 200px;
  min-width: 460px;
}
</style>
