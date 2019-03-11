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
      <el-select size="small" v-model="valuetype" placeholder="请选择类别">
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
        <el-button style="width: 100%" size="small" type="primary">查询</el-button>
    </el-row>
  </div>
</template>

<script>
    export default {
      name: "serach",
      data() {
        return {
          input: '',
          valuews:'',
          valuetype:'',
          optionsws:[
            {
              value: '1',
              label: '<5W'
            }, {
              value: '2',
              label: '5~10W'
            }, {
              value: '3',
              label: '11~20W'
            }, {
              value: '4',
              label: '21~50W'
            }, {
              value: '5',
              label: '51~100W'
            }, {
              value: '6',
              label: '101~300W'
            }, {
              value: '7',
              label: '301~500W'
            }, {
              value: '8',
              label: '501~1000W'
            }, {
              value: '9',
              label: '10KVA Max.'
            }, {
              value: '10',
              label: '2500KVA Max.'
            }, {
              value: '11',
              label: 'NA'
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
          ]
        }
      },
      methods: {
        handleClick() {
          alert('button click');
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
