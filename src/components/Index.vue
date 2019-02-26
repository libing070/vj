<template>
  <div class="hello">
    <nav-header></nav-header>
    <carousel></carousel>
    <nav-cardproduct></nav-cardproduct>
    <nav-news></nav-news>
    <!--<h1>{{ nickname }}</h1>

    <div>
      <p>FullName: {{fullName}}</p>
      <p>FirstName: <input type="text" v-model="firstName"></p>
    </div>

    <el-row>
      <el-button>默认按钮</el-button>
    </el-row>
    <div>{{res}}</div>-->
  </div>
</template>

<script>
  import NavHeader from './../components/NavHeader'
  import Carousel from './../components/Carousel'
  import NavCardproduct from './../components/NavCardproduct'
  import NavNews from './../components/NavNews'
  import API from '../api/api_goods';
  export default {
  name: 'index',
    components:{
      NavHeader:NavHeader,
      Carousel:Carousel,
      NavCardproduct:NavCardproduct,
      NavNews:NavNews
    },
    data () {
      return {
        nickname: '',
        res:{},
        firstName: 'li',
        lastName: 'bing',
        fullName: ''
      }
    },
    watch: {
      firstName(newName, oldName) {
        this.fullName = newName + ' ' + this.lastName;
      }
    },
    created(){
      this.$root.Bus.$on('goto', (url) => {
        if (url === "/login") {
          localStorage.removeItem('access-user');
        }
        this.$router.push(url);
      })
    },
    mounted() {
      let user = localStorage.getItem('access-user');
      if (user) {
        user = JSON.parse(user);
        this.nickname ="Welcome "+ user.nickname || '';
      }

      this.search()
    },
    methods:{
      search(){
        let that = this;
        that.loading = true;
        API.findList().then(function (result) {
          that.loading = false;
          if(result){
            that.res=result;
            console.log(result);
          }
        }, function (err) {
          that.loading = false;
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.loading = false;
          console.log(error);
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });
      },
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
  /*$color: red;
  div {color:$color;}*/
</style>
