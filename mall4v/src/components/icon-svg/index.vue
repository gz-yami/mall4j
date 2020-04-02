<template>
  <svg
    :class="getClassName"
    :width="width"
    :height="height"
    aria-hidden="true">
    <use :xlink:href="getName"></use>
  </svg>
</template>

<script>
  export default {
    name: 'icon-svg',
    props: {
      name: {
        type: String,
        required: true
      },
      className: {
        type: String
      },
      width: {
        type: String
      },
      height: {
        type: String
      }
    },
    computed: {
      getName () {
        return `#icon-${this.name}`
      },
      getClassName () {
        return [
          'icon-svg',
          `icon-svg__${this.name}`,
          this.className && /\S/.test(this.className) ? `${this.className}` : ''
        ]
      }
    }
  }
</script>

<style>
  .icon-svg {
    width: 1em;
    height: 1em;
    fill: currentColor;
    overflow: hidden;
  }
</style>
