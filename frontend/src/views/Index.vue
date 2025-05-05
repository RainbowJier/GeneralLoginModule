<template>
   <div class="index-wrapper">
      <RouterView></RouterView>
   </div>

</template>

<script setup lang="ts" name="Index">

</script>

<style scoped>
.index-wrapper {
   width: 100%;
   height: 100vh;
   /* 子元素 */
   display: flex;
   justify-content: center;
   align-items: center;
}
</style>