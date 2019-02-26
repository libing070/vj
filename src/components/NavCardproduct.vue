<template>
  <el-row style="margin: 20px">
    <el-col :span="4" v-for="(o, index) in cardDate" :key="index" :offset="index > 0 ? 1 : 0">
      <el-card :body-style="{ padding: '0px' }">
        <img :src="require('./../assets/images/navcardproduct/h_pro'+index+'.png')" class="image">
        <div style="padding: 10px;">
          <span style="font-weight: bold">{{o.title}}</span>
          <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px" v-for="i in o.list"><i class="el-icon-caret-right"></i>{{i}}</dt>
              </dl>

            <el-button type="text" class="button">更多</el-button>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
  export default {
    data() {
      return {
        cardDate:[
          {title:'低功率/数位3C',list:['网络电源','无线电话电源','其他']},
          {title:'高功率电源',list:['家电类电源','锂电池充电器','工业电源']},
          {title:'磁性组件',list:['变压器/电感','动态应用: 变频驱动,客制组件','静态应用: 电能转换,客制组件']},
          {title:'代工制造',list:['电源相关','无线电话电源','非电源相关']},
          {title:'照明',list:['产品','品质认证条款','其他']},
        ]
      };
    }
  }
</script>

<style scoped>
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
