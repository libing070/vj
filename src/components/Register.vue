<template>
  <div id="registerDiv" style="height: 100%;background-color: #52B8F2">
  <el-card class="box-card">
    <el-row>
      <el-col :span="24">
        <h2 style="text-align: center">用户注册</h2>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form ref="regForm" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号码"></el-input>
          </el-form-item>
       <!--   <el-form-item label="邮箱"  prop="email">
            <el-input v-model="form.email"></el-input>
          </el-form-item>-->
          <el-form-item label="账号"  prop="account">
            <el-input v-model="form.account" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="登录密码"  prop="password">
            <el-input v-model="form.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item label="确认密码"  prop="passwordok" >
            <el-input v-model="form.passwordok" placeholder="请输入密码"></el-input>
          </el-form-item>
         <!-- <el-form-item label="所在城市">
            <el-cascader
              :options="options2"
              @active-item-change="handleItemChange"
              :props="props"
            ></el-cascader>
          </el-form-item>-->
          <!--<el-form-item label="性别"  prop="sex">
              <el-radio v-model="form.sex" label="0">小哥哥</el-radio>
              <el-radio v-model="form.sex" label="1">小姐姐</el-radio>
          </el-form-item>-->
          <el-form-item label="验证码"  prop="yzm">
            <el-col :span="13">
              <el-input v-model="form.yzm" placeholder="请输入验证码"></el-input>
            </el-col>
            <el-col :span="1">
              &nbsp;
            </el-col>
            <el-col :span="10">
              <el-button type="primary" id="currCode" @click.native="createCode"></el-button>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onRegSubmit">立即注册</el-button>
            <el-button @click="onCallBackLogin">返回</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
  </div>
</template>
<script>
  import API from '../api/api_accounts';
  export default {
        name: "register",
      data() {
        let accountIdReg=/^[a-zA-Z]{1}([a-zA-Z0-9]|[_]){4,19}$/;
        let validateAccountId= (rule, value, callback) => {
          if (!value) {
            return callback(new Error('请输入用户名!'))
          }
          setTimeout(() => {
            if (!accountIdReg.test(value)) {
              callback(new Error('账号格式有误! (只能输入5-20个以字母开头、可带数字、“_”的字串 )'))
            } else {
              callback()
            }
          }, 500)
        };
        let accountEmailReg=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        let validateAccountEmail= (rule, value, callback) => {
          if (!value) {
            return callback(new Error('请输入邮箱!'))
          }
          setTimeout(() => {
            if (!accountEmailReg.test(value)) {
              callback(new Error('邮箱格式有误!'))
            } else {
              callback()
            }
          }, 500)
        };
        var accountPwdReg=/^(?![A-Z]+$)(?![a-z]+$)(?!\d+$)\S{8,}$/;//长度不小于8
        let validateAccountPwd= (rule, value, callback) => {
          if (!value) {
            return callback(new Error('请输入密码!'))
          }
          setTimeout(() => {
            if (!accountPwdReg.test(value)) {
              callback(new Error('密码格式有误!(长度不小于8，且不能为纯数字)'))
            } else {
              callback()
            }
          }, 500)
        };
        let validateAccountPwdOk= (rule, value, callback) => {
          if (!value) {
            return callback(new Error('请输入密码!'))
          }
          setTimeout(() => {
            if (this.form.password!=this.form.passwordok) {
              callback(new Error('密码不一致!'))
            } else {
              callback()
            }
          }, 500)
        };

        let phoneReg = /^[1][3,4,5,7,8][0-9]{9}$/;
        let validatePhone = (rule, value, callback) => {
          if (!value) {
            return callback(new Error('请输入手机号码!'))
          }
          setTimeout(() => {
            if (!phoneReg.test(value)) {
              callback(new Error('号码格式有误!'))
            } else {
              callback()
            }
          }, 500)
        };
        let validateYzm = (rule, value, callback) => {
          if (!value) {
            return callback(new Error('请输入验证码!'))
          }
          setTimeout(() => {
            let checkCode=document.getElementById("currCode");
            if (checkCode.innerText.toLowerCase()!=this.form.yzm.toLowerCase()) {//不区分大小写
              callback(new Error('验证码错误!'))
            } else {
              callback()
            }
          }, 500)
        };
        return {
          code:'',
          form: {
            phone:'',
            email:'',
            account: '',
            password:'',
            passwordok:'',
            like: [],
            sex: '0',
            yzm:''
          },
          options2: [{
            label: '江苏',
            cities: []
          }, {
            label: '浙江',
            cities: []
          }],
          props: {
            value: 'label',
            children: 'cities'
          },
          // 校验规则
          rules: {
            // 校验，主要通过validator来指定验证器名称
            account: [{required: true, validator: validateAccountId, trigger: 'blur'}],
            password: [{required: true, validator: validateAccountPwd, trigger: 'blur'}],
            passwordok: [{required: true, validator: validateAccountPwdOk, trigger: 'blur'}],
            email: [{required: true, validator: validateAccountEmail, trigger: 'blur'}],
            phone: [{required: true, validator: validatePhone, trigger: 'blur'}],
            yzm: [{required: true, validator: validateYzm, trigger: 'blur'}]

          },
        }
      },
      mounted(){
        this.createCode();
      },
      methods: {
        createCode(){//生成6位随机码
          this.code="";
          let codeLength = 6;//定义验证码的长度
          let checkCode=document.getElementById("currCode");
          let  random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
            'S','T','U','V','W','X','Y','Z');//随机数
          for(let i = 0; i < codeLength; i++) {//循环操作
            let index = Math.floor(Math.random()*36);//取得随机数的索引（0~35）
            this.code += random[index];//根据索引取得随机数加到code上
          }
          checkCode.innerText = this.code;//把code值赋给验证码
        },
        onRegSubmit() {
          let that = this;
          this.$refs.regForm.validate((valid) => {
            if (valid) {
              let registerParams={"accountId":this.form.account,
                                  "accountPassword":this.$md5(this.form.password),
                                  "accountPhone":this.form.phone,
                                  "accountEmail":this.form.email
                                 };
              API.register(registerParams).then(function (result) {
                that.loading = false;
                if (result.errcode=="40000") {
                  that.$message({showClose: true, type: result.type, message: result.errmsg });
                  setTimeout(() => {
                    that.$router.push({path: '/login'});
                  }, 1000)
                } else {
                  that.$message({showClose: true,type: result.type, message: result.errmsg || '注册失败', duration: 2000});
                }
              }, function (err) {
                that.loading = false;
                that.$message({showClose: true,type: err.type, message: err.toString(), duration: 2000});
              }).catch(function (error) {
                that.loading = false;
                console.log(error);
                that.$message({showClose: true,type: err.type, message: '请求出现异常', duration: 2000});
              });
            }
          });
        },
        handleItemChange(val) {
          console.log('active item:', val);
          setTimeout(_ => {
            if (val.indexOf('江苏') > -1 && !this.options2[0].cities.length) {
              this.options2[0].cities = [{
                label: '南京'
              }];
            } else if (val.indexOf('浙江') > -1 && !this.options2[1].cities.length) {
              this.options2[1].cities = [{
                label: '杭州'
              }];
            }
          }, 300);
        },
        onCallBackLogin(){
          this.$router.push({path: '/login'});
        }
      }
    }
</script>

<style lang="scss" scoped>
  .registerDiv{
    position: relative;
  }
   .box-card{
     width:450px;
     height: 500px;
     position: absolute;
     top: 0;
     bottom: 0;
     left: 0;
     right: 0;
     margin:50px auto;
   }
</style>
