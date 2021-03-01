<template>
<!--logs.wxml-->
<view class="container log-list">
  <block v-for="(log, index) in logs" :key="index">
    <text class="log-item">{{index + 1}}. {{log}}</text>
  </block>
</view>
</template>

<script>
//logs.js
const util = require("../../utils/util.js");

export default {
  data() {
    return {
      logs: []
    };
  },

  components: {},
  props: {},
  onLoad: function () {
    this.setData({
      logs: (uni.getStorageSync('logs') || []).map(log => {
        return util.formatTime(new Date(log));
      })
    });
  },
  methods: {}
};
</script>
<style>
@import "./logs.css";
</style>