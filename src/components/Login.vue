<template>
  <div class="login">
    <el-row>
      <el-button  style="float:right" type=""  @click="changeLangEvent">{{$t('lang.languageChangeBtn')}}</el-button>
    </el-row>
    <el-card class="login-box-card">
      <el-tabs :tab-position="tabPosition" v-model="activeTabIndex">
        <el-tab-pane name="first" :label="$t('lang.loginNav1')">
          <el-form ref="AccountForm" :rules="rules" :model="form">
            <el-form-item prop="account">
                <el-col :span="24">
                  <el-input class="" placeholder="请输入账号" v-model="form.account" auto-complete="true">
                    <template slot="prepend">
                      <i class="iconfont icon-login-zhanghaoguanli-copy"></i>
                    </template>
                  </el-input>
                </el-col>
            </el-form-item>
            <el-form-item prop="password">
                <el-col :span="24">
                  <el-input class="" type="password"placeholder="请输入密码" v-model="form.password" auto-complete="true">
                    <template slot="prepend">
                      <i class="iconfont icon-login-mima"></i>
                    </template>
                  </el-input>
                </el-col>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane name="second" :label="$t('lang.loginNav2')">
          <el-form ref="AccountForm1" :rules="rules" :model="form1">
            <el-form-item prop="phone">
              <el-input class="" placeholder="请输入手机号码" v-model="form1.phone" auto-complete="true">
                <template slot="prepend">
                  <i class="iconfont icon-login-shoujihaoma"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-row :gutter="10">
                <el-col :span="14">
                  <el-input class="" placeholder="请输入验证码" v-model="form1.yzm">
                    <template slot="prepend">
                      <i class="iconfont icon-login-yzm"></i>
                    </template>
                  </el-input>
                </el-col>
                <el-col :span="10">
                  <el-button size="small" type="primary" :class="{yzmBtnDisabled: !this.canClick}" @click="countDown"> {{content}}</el-button>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <el-row>
        <el-col :span="24">
          <el-button size="small" style="width: 100%" type="primary" @click.native.prevent="handleLogin">{{$t('lang.loginBtn')}}</el-button>
        </el-col>
      </el-row>
      <el-row style="margin-top: 20px">
        <el-col :span="6">
          <a href="#" style="font-size: 12px">{{$t('lang.loginforgetPwd')}}?</a>
        </el-col>
        <el-col :span="18">
          <el-button size="small" @click.native.prevent="handleRegister">{{$t('lang.loginregister')}}</el-button>
          <el-button size="small"  @click="changePwdDialogVisible  = true">{{$t('lang.loginchangePwd')}}</el-button>
        </el-col>
      </el-row>
    </el-card>


    <el-dialog
      title="修改密码"
      :visible.sync="changePwdDialogVisible"
      width="40%"
      center>
      <el-form ref="ChangePwdForm" :rules="rules" :model="changePwdform">
        <el-form-item label="账号"  prop="account" label-width="120px">
          <el-input v-model="changePwdform.account"></el-input>
        </el-form-item>
        <el-form-item label="原密码"  prop="password" label-width="120px">
          <el-input v-model="changePwdform.password"></el-input>
        </el-form-item>
        <el-form-item label="设置新密码"  prop="newPassword" label-width="120px">
          <el-input v-model="changePwdform.newPassword"></el-input>
        </el-form-item>
        <el-form-item lot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="changePwdDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="onChangePwd">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
  import API from '../api/api_accounts';
  import APISMS from '../api/api_sendsms';
  export default {
    name: "login",
    data() {
      let accountIdReg=/^[a-zA-Z]{1}([a-zA-Z0-9]|[_]){4,19}$/;
      let validateAccountId= (rule, value, callback) => {
        if (!value) {
          return callback(new Error('账号不能为空!'))
        }
        setTimeout(() => {
          if (!accountIdReg.test(value)) {
            callback(new Error('账号格式有误!'))
          } else {
            callback()
          }
        }, 500)
      };
      var accountPwdReg=/^(?![A-Z]+$)(?![a-z]+$)(?!\d+$)\S{8,}$/;//长度不小于8
      let validateAccountPwd= (rule, value, callback) => {
        if (!value) {
          return callback(new Error('密码不能为空!'))
        }
        setTimeout(() => {
          if (!accountPwdReg.test(value)) {
            callback(new Error('密码格式有误!'))
          } else {
            callback()
          }
        }, 500)
      };
      let phoneReg = /^[1][3,4,5,7,8][0-9]{9}$/;
      let validatePhone = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('号码不能为空!'))
        }
        setTimeout(() => {
          if (!phoneReg.test(value)) {
            callback(new Error('号码格式有误!'))
          } else {
            callback()
          }
        }, 500)
      };
      return {
        tabPosition: 'top',
        changePwdDialogVisible: false,
        activeTabIndex:'first',
        content: '发送验证码',
        totalTime: 60,
        canClick: true,//添加canClick
        form: {
          account: '',
          password: ''
        },
        form1: {
          phone: '',
          yzm: ''
        },
        changePwdform:{
          account: '',
          password: ''
        },
        // 校验规则
        rules: {
          // 校验手机号码，主要通过validator来指定验证器名称
          account: [{required: true, validator: validateAccountId, trigger: 'blur'}],
          password: [{required: true, validator: validateAccountPwd, trigger: 'blur'}],
          newPassword: [{required: true, validator: validateAccountPwd, trigger: 'blur'}],
          phone: [{required: true, validator: validatePhone, trigger: 'blur'}]
        },

      }
    },
    methods: {
      //中英文切换
      changeLangEvent(){
          let lang=this.$i18n.locale==='zh'?'en':'zh';
          this.$i18n.locale=lang;
      },
      handleLogin() {//登录
        let that = this;
        let AccountForm="";
        if(this.activeTabIndex=="first"){
          AccountForm="AccountForm";
        }else{
          AccountForm="AccountForm1";
        }
        this.$refs[AccountForm].validate((valid) => {
            if (valid) {
              if(this.activeTabIndex=="first"){
                let loginParams={"accountId":this.form.account,"accountPassword":this.$md5(this.form.password)};
                API.login(loginParams).then(function (result) {
                  that.loading = false;
                  if (result && result.status==1) {
                    localStorage.setItem('access-user', JSON.stringify({"nickname":result.accountId}));
                    that.$router.push({path: '/mains'});
                  } else {
                    that.$message({showClose: true,type: result.type, message: result.errmsg || '登录失败', duration: 2000});
                  }
                }, function (err) {
                  that.loading = false;
                  that.$message({showClose: true, type: err.type, message: err.toString(), duration: 2000});
                }).catch(function (error) {
                  that.loading = false;
                  console.log(error);
                  that.$message({showClose: true,type: 'error', message: '请求出现异常', duration: 2000});
                });
              }else{
                //this.canClick=true;
              }
            }
        });
      },
      handleRegister(){//注册
        let that = this;
        that.$router.push({path: '/register'});
      },
      onChangePwd(){//修改密码
        let that=this;
        this.$refs.ChangePwdForm.validate((valid) => {
          if (valid) {
            let changePwdParams={"accountId":this.changePwdform.account,"accountPassword":this.$md5(this.changePwdform.password),"accountNewPassword":this.$md5(this.changePwdform.newPassword)};
            API.changePwd(changePwdParams).then(function (result) {
              that.loading = false;
              if(result.errcode=="40000"){
                that.changePwdDialogVisible = false;
              }
              that.$message({showClose: true,type: result.type, message: result.errmsg, duration: 2000});
            }, function (err) {
              that.loading = false;
              that.$message({showClose: true, type: err.type, message: err.toString(), duration: 2000});
            }).catch(function (error) {
              that.loading = false;
              console.log(error);
              that.$message({showClose: true,type: 'error', message: '请求出现异常', duration: 2000});
            });
          }
        });

      },
      countDown () {//生成6位随机码
        let that = this;
        if (!this.canClick) return ;
        this.canClick = false;
        this.content = this.totalTime + 'S后重新发送';
        let clock = window.setInterval(() => {
          this.totalTime--;
          this.content = this.totalTime + 'S后重新发送';
          if (this.totalTime < 0) {
            window.clearInterval(clock);
            this.content = '重新发送验证码';
            this.totalTime = 60;
            this.canClick = true; //这里重新开启
          }
        },1000);
        let sendSmsParams={'phone':this.form1.phone};
        APISMS.sendsms(sendSmsParams).then(function (result) {
             console.log("result:"+result);
         /* if (result && result.status==1) {
            localStorage.setItem('access-user', JSON.stringify({"nickname":result.accountId}));
            that.$router.push({path: '/index'});
          } else {
            that.$message({showClose: true,type: result.type, message: result.errmsg || '登录失败', duration: 2000});
          }*/
        }, function (err) {
          that.loading = false;
          that.$message({showClose: true, type: err.type, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.loading = false;
          console.log(error);
          that.$message({showClose: true,type: 'error', message: '请求出现异常', duration: 2000});
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "~css/login";
  .login {
    position: relative;
    height: 100%;
    background-size: cover;
    background-image: url("~img/login-bg.png");
    background-color: #52B8F2;
    .login-box-card {
      position: absolute;
    }
  }
  .el-row {
    margin: 0px 0px 10px 0px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .yzmBtnDisabled{
    background-color: #F5F7FA;
    border-color: #F5F7FA;
    color:#57a3f3;
    cursor: not-allowed; // 鼠标变化
  }
</style>
