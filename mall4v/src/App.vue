<template>
  <transition name="fade">
    <router-view></router-view>
  </transition>
</template>

<script>
export default {
  computed: {
    key () {
      return this.$route.path + Math.random()
    }
  }
}
</script>
