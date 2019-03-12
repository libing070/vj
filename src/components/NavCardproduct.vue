<template>
  <el-row style="margin: 20px 0;">
    <el-col :span="4" v-for="(o, index) in cardDate" :key="index" :offset="index > 0 ? 1 : 0">
      <el-card :body-style="{ padding: '0px' }">
        <img :src="require('./../assets/images/navcardproduct/h_pro'+index+'.png')" class="image">
        <div style="padding: 10px;">
          <span style="font-weight: bold">{{o.title}}</span>
          <div class="bottom clearfix">
              <dl>
                <dt style="padding: 5px 0;font-size:12px;cursor: pointer;" v-for="i in o.list"><i class="el-icon-caret-right"></i>{{i}}</dt>
              </dl>

            <el-button type="text" class="button" @click="productsPage()">更多</el-button>
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
        /*cardDate:[
          {title:'低功率/数位3C',list:['网络电源','无线电话电源','其他']},
          {title:'高功率电源',list:['家电类电源','锂电池充电器','工业电源']},
          {title:'磁性组件',list:['变压器/电感','动态应用: 变频驱动,客制组件','静态应用: 电能转换,客制组件']},
          {title:'代工制造',list:['电源相关','无线电话电源','非电源相关']},
          {title:'照明',list:['产品','品质认证条款','其他']},
        ]*/
        cardDate:[
          {title:'立式',list:['网络电源','无线电话电源','其他']},
          {title:'卧式',list:['家电类电源','锂电池充电器','工业电源']},
          {title:'桌面式',list:['变压器/电感','动态应用: 变频驱动,客制组件','静态应用: 电能转换,客制组件']},
          {title:'活动脚',list:['电源相关','无线电话电源','非电源相关']},
          {title:'其他',list:['产品','品质认证条款','其他']},
        ]
      };
    },
    methods:{
      productsPage(){
        this.$router.push({path:'/products'});
      }
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
    padding: 5px;
    float: right;
  }

  .image {
    width: 100%;
    max-width: 216px;
    max-height: 104px;
    display: block;
    cursor: pointer;
    transition: all 0.6s;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .image:after {
    clear: both
  }

  .image:hover{
    transform: scale(1.1); /* 放大1倍 */
  }
</style>
