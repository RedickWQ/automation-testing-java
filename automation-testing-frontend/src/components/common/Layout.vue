<style scoped>
</style>

<template>
  <div id="app" class="layout" theme="light" >


    <vhead>

    </vhead>
    <Row type="flex" style="margin-top:5px;">

      <Col :span="spanLeft" class="layout-menu-left">
        <left-side-menu iconSize="14"></left-side-menu>
      </Col>

      <!-- 主页面区域 -->
      <Col :span="spanRight"  style="background-color:white;" >
          <slot name="mainBody"></slot>

          <vfooter> </vfooter>
      </Col>   <!-- end of 主页面区域 -->
      </Row>

  </div>
</template>
<script>
  import vhead from './Header'
  import leftSideMenu from './LeftSideMenu'
  import vfooter from './Footer'
  export default {
    props: [],
    data () {
      return {
        spanLeft: 3,
        spanRight: 21
      }
    },
    methods: {

    },
    computed: {
      // iconSize () {
      //   return this.spanLeft === 4 ? 14 : 24
      // }
    },
    components: {
      vhead,
      leftSideMenu,
      vfooter
    }
  }
</script>
