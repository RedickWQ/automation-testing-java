<style scoped>
  #app {
    min-height: 800px;
    height: 100%;
  }


</style>
<template>
<div id="app" class="layout" theme="light" >

    <Layout>
      <div slot="mainBody">

        <Row type="flex" style="margin-top:20px">
          <Col :span="22" style="background-color:white;" offset="1">
            <Breadcrumb>
              <Breadcrumb-item href="/">
                  <Icon type="ios-home-outline"></Icon> Home
              </Breadcrumb-item>
              <Breadcrumb-item >
                  <Icon type="ios-list-outline"></Icon> Testcase List
              </Breadcrumb-item>


            </Breadcrumb>
          </Col>
        </Row>
          <Row type="flex" style="margin-top:20px;">

            <Col :span="22" style="background-color:white;" offset="1">




            </Col>
          </Row>
      </div>
    </Layout>

  </div>
</template>
<script>
  import {mapGetters, mapActions} from 'vuex'
  // import requestHelper from '../../lib/requestHelper'
  import Layout from '../../components/common/Layout'
  export default {
    data () {
      return {
      }
    },
    components: {
      Layout
    },
    created: function () {
    },
    mounted: function () {

    },
    computed: {
      ...mapGetters({
      })
    },
    methods: {
      ...mapActions({})
    }

  }
</script>
