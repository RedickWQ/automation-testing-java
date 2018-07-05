<style scoped>
  .logo{
    margin-top: 10px;
    margin-left: 10px;
    width: 200px;
    height: 50px;
  }
</style>

<template>
  <div >
    <Row  type="flex" class="layout-head-ceiling" align="middle"  >
      <Col span="4" class="layout-head-navbar">
        <img src="../../assets/patsnap.png" class="logo">

      </Col>
      <Col span="8">
        <span style="color: #19be6b; font-size: 30px;">Test Automation Server</span>
      </Col>
      <Col span="12" class="layout-head-right">
        <ul>
          <li style="width:50px;">
              <!-- <img :src="'https://work.alibaba-inc.com/photo/' + userInfo.empId +'.80x80.jpg'"
                  :alt="userInfo.name" class="s_circle_radius" style="height: 50px;width: 50px;">
              </img> -->
          </li>
          <li class="active" style="width:57px;">
              <!-- <div style="color:white; padding-top:15px; margin-left: 15px;">{{userInfo.name}}</div> -->
          </li>
        </ul>
      </Col>
    </Row>
  </div>

</template>

<script>
  import {mapGetters, mapActions} from 'vuex'
  export default {
    data () {
      return {
      }
    },
    components: {

    },
    created: function () {
    },
    mounted: function () {

    },
    computed: {
      ...mapGetters([
      ])
    },
    methods: {
      ...mapActions([])
    }

  }
</script>
