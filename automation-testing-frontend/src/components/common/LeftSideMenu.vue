<style scoped>
  .menu-container{
    height:100%;
  }
</style>
<template>

  <div class="menu-container">
    <Menu active-name="1" width="auto" style="height:100%;" >
      <Menu-item name="index" v-for="(item, index) in menuItems" :key="index">
        <Icon :type="item.iconType" :size="15"></Icon>
        <span class="layout-text"><a  :href="item.url" >{{item.text}}</a></span>
      </Menu-item>

    </Menu>
  </div>

</template>

<script>
  // import $ from 'jQuery'
  export default {
    props: ['iconSize', 'currentRoute'],
    data () {
      return {
        menuItems: [
          {
            text: 'Test Case',
            iconType: 'ios-list-outline',
            url: '/testcaseList'
          },
          {
            text: 'Test Suite',
            iconType: 'ios-albums-outline',
            url: '/testSuiteList'
          },
          {
            text: 'Running Suite',
            iconType: 'ios-albums-outline',
            url: '/testsuiteExecutionResult'
          },
          {
            text: 'Test Report',
            iconType: 'stats-bars',
            url: '/testReportList'
          }
        ]
      }
    },
    methods: {

    },
    mounted: function () {

    }
  }

</script>
