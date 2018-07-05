<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import {mapGetters, mapActions, mapMutations} from 'vuex'
export default {
  name: 'app',
  created: function () {
    // 注入全局的ajax call 处理方式
    var vm = this
    this.setAjaxErrorHandler((error) => {
      if (error) {
        vm.$Message.error(error.message)
      }
    })
  },
  computed: {
    ...mapGetters([])
  },
  methods: {
    ...mapActions([
    ]),
    ...mapMutations([
      'setAjaxErrorHandler'
    ])
  }
}
</script>

<style>
/*#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}*/
</style>
