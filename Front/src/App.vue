<template>
  <div id="app">
     <Navbar></Navbar>
      <div id="main-content"><router-view></router-view></div>

  </div>
</template>

<script>
    import Navbar from "@/components/Navbar";
    export default {
   name: 'App',
   components: {
      Navbar
   }


}


</script>

<style>
    @import "CSS/MainCSS.css";
</style>
