<template>
    <div class="hello">
      <nav-header></nav-header>
      <router-view></router-view>
    </div>
</template>

<script>
  import NavHeader from './../components/NavHeader'
    export default {
        name: "mains",
      components:{
        NavHeader:NavHeader,
      },
    }
</script>

<style scoped>

</style>
