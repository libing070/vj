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
    export default {
        name: "mains",
      components:{
        NavHeader:NavHeader,
        NavFooter:NavFooter
      },
    }
</script>

<style lang="scss" scoped>
  @import "~css/mains";

</style>
