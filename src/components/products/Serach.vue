<template>
  <div class="products-search">
    <el-row>
      <el-select size="small" v-model="valuews" placeholder="请选择瓦数">
        <el-option
          v-for="item in optionsws"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </el-row>
    <el-row>
      <el-select disabled="" size="small" v-model="valuetype" placeholder="请选择类别">
        <el-option
          v-for="item in optionstype"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </el-row>
    <el-row>
        <el-input size="small" v-model="input" placeholder="请输入关键字"></el-input>
    </el-row>
    <el-row>
        <el-button style="width: 100%" size="small" type="primary" @click="searchProductsBtn()">查询</el-button>
    </el-row>
    <el-row>
      <el-tree
        style="height: 100%;"
        :data="treedata"
        :props="defaultProps"
        accordion
        @node-click="handleNodeClick">
      </el-tree>
    </el-row>
  </div>
</template>

<script>
  import API from '../../api/api_products';
    export default {
      name: "serach",
      data() {
        return {
          treedata:[
            {
              label: '低功率/数位3C',
              children: [{
                label: '网络电源'
              }, {
                  label: '无线电话电源',
              }, {
                  label: 'USB充电',
              }, {
                  label: '其他',
                children: [{
                  label: 'CPU中央处理器'
                },{
                  label: '主机板'
                }]
              }]
            }, {
              label: '高功率电源',
              children: [{
                label: '家电类电源',
              }, {
                label: '锂电池充电器',
              }, {
                label: '工业电源',
                children: [{
                  label: '客制化电源'
                }]
              }]
            }, {
              label: '磁性组件',
              children: [{
                label: '变压器/电感',
                children: [{
                  label: '高频率'
                },{
                  label: '低频率'
                }]
              }, {
                label: '动态应用：变频驱动，客制组件',
                children: [{
                  label: '电梯'
                }]
              }, {
                label: '静态应用：电能转换，客制组件',
                children: [{
                  label: '再生能源'
                },{
                  label: '逆变器&不间断电源'
                }]
              }]
            }, {
              label: '代工制造',
              children: [{
                label: '电源相关',
                children: [{
                  label: '车载配件'
                },{
                  label: '工业电源'
                },{
                  label: 'LED驱动器'
                },{
                  label: '轨道交通'
                },{
                  label: '智能家居（厨具）'
                },{
                  label: '智能家居（排插）'
                },{
                  label: '智能家居（电源）'
                },{
                  label: '交换式电源供应器'
                }]
              },{
                label: '非电源相关',
                children: [{
                  label: '水质监测笔'
                }]
              }]
            }, {
              label: '照明',
              children: [{
                label: '产品',
                children: [{
                  label: 'ASL系列'
                },{
                  label: 'uASL系列'
                },{
                  label: 'iDL系列'
                },{
                  label: 'G7系列'
                },{
                  label: 'uDL系列'
                }]
              },{
                label: '品质认证条款',
              }]
            },
          ],defaultProps: {
            children: 'children',
            label: 'label'
          },
          input: '',
          valuews:'',
          valuetype:'',
          optionsws:[
            {
              value: '0,2500',
              label: '全部'
            },
            {
              value: '0,5',
              label: '<5W'
            }, {
              value: '5,10',
              label: '5~10W'
            }, {
              value: '11,20',
              label: '11~20W'
            }, {
              value: '21,50',
              label: '21~50W'
            }, {
              value: '50,100',
              label: '51~100W'
            }, {
              value: '101,300',
              label: '101~300W'
            }, {
              value: '301,500',
              label: '301~500W'
            }, {
              value: '501,1000',
              label: '501~1000W'
            }, {
              value: '1000,2500',
              label: '10KVA Max.'
            }, {
              value: '2500',
              label: '2500KVA Max.'
            }
          ],
          optionstype:[
            {
              value: '1',
              label: 'Charger'
            }, {
              value: '2',
              label: 'Cordless Phone'
            }, {
              value: '3',
              label: 'Home Appliance Power'
            }, {
              value: '4',
              label: 'Industrial Power'
            }, {
              value: '5',
              label: 'Lighting'
            }, {
              value: '6',
              label: 'Network Communication'
            }, {
              value: '7',
              label: 'OEM'
            }, {
              value: '8',
              label: 'Power Conversion'
            }, {
              value: '9',
              label: 'Variable Frequency Driver'
            }, {
              value: '10',
              label: 'USB PD'
            }, {
              value: '11',
              label: 'Others'
            }
          ],
          productsList:[]
        }
      },
      methods: {
        handleClick() {
          alert('button click');
        },
        handleNodeClick(data) {
          console.log(data);
        },
        searchProductsBtn(){
          let that = this;
          this.$store.commit('productListOrDetailPageStore',0);//列表页面
          let ws=that.valuews,type=that.valuetype,inp=that.input;
          let arr=[],gte="",lte="";
          if(ws!=""){
            arr=ws.split(",");
           if(arr.length==1){
                gte=arr[0];
            }else if(arr.length==2){
                gte=arr[0];
                lte=arr[1];
              }
          }else {
            gte=0;
            lte=2500;
          }
          //小于"lt",大于等于"gte",小于等于"lte"
          let params={gte:Number(gte),lte:Number(lte),inp:inp};
          API.findProductsListByParams(params).then(function (result) {
            that.loading = false;
            if(result){
              that.$store.commit('ProductsListStore',result.result.list);
            } else {
              that.$message({showClose: true,type: result.type, message: result.errmsg || '查询失败', duration: 2000});
            }
          }, function (err) {
            that.loading = false;
            that.$message({showClose: true, type: err.type, message: err.toString(), duration: 2000});
          }).catch(function (error) {
            that.loading = false;
            that.$message({showClose: true,type: 'error', message: '请求出现异常', duration: 2000});
          });
        }
      }
    }
</script>

<style scoped>
  .products-search{
    margin: 20px;

  }
  .products-search .el-row{
    margin: 10px 0;
  }
  .el-dropdown {
    vertical-align: top;
  }
  .el-dropdown + .el-dropdown {
    margin-left: 15px;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
