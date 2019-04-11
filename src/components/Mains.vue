<template>
    <div class="mains">
      <nav-header></nav-header>
      <router-view></router-view>
      <nav-footer></nav-footer>
    </div>
</template>

<script>

  import NavHeader from './../components/NavHeader'
  import NavFooter from './../components/NavFooter'
  import API_Accessinfos from '../api/api_accessinfos';
  import {BASE} from '../assets/js/common';
  import returnCitySN from 'returnCitySN'
  export default {
        name: "mains",
      components:{
        NavHeader:NavHeader,
        NavFooter:NavFooter
      },
    created(){
    },
    mounted(){
        this.accessinfos();
    },
    methods:{
      accessinfos(){
        let that=this;
        let accessinfosParams={
          "ip": returnCitySN["cip"],
          "cid":returnCitySN["cid"],
          "area":returnCitySN["cname"],
          "browserVersion":BASE.getBrowserInfo(),
        };
        API_Accessinfos.addAccessinfos(accessinfosParams);

      },
    }
    }
</script>

<style lang="scss" scoped>
  @import "~css/mains";

</style>
