<style >
  #app {
    min-height: 800px;
    height: 100%;
  }
  .reportLink{
    color: #1c2438
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
                  <Icon type="ios-list-outline"></Icon> TestReport List
              </Breadcrumb-item>


            </Breadcrumb>
          </Col>
        </Row>
          <Row style="margin-top:20px;">

            <Col :span="22" style="background-color:white;" offset="1">

              <Row type="flex" :gutter="16">
                <Col span="5">
                  <Input v-model.trim="testReportListTableOptions.keyword" placeholder="Filter by name" icon="ios-search"> </Input>
                </Col>
              </Row>

              <Row style="margin-top: 10px;" :gutter="16">
                <Col>
                  <Table
                    stripe
                    :columns="testReportListTableOptions.columns"
                    :loading="testReportListTableOptions.isLoading"
                    :data="filteredTestReportList"
                    ref="testReportListTable"
                  >
                  </Table>
                </Col>
              </Row>
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
        testReportListTableOptions: {
          keyword: '',
          isLoading: false,
          columns: [
            {
              title: 'ReportName',
              // width: 200,
              key: 'name',
              render: this.renderReportItem
            }
          ]
        }
      }
    },
    components: {
      Layout
    },
    created: function () {
      this.testReportListTableOptions.isLoading = true
      this.action_getReportList(() => {
        this.testReportListTableOptions.isLoading = false
      })
    },
    mounted: function () {

    },
    computed: {
      ...mapGetters({
        testReportList: 'testReportList/testReportList'
      }),
      filteredTestReportList: function () {
        var keyword = this.testReportListTableOptions.keyword
        return this.testReportList.filter(report => {
          return report.name.toLowerCase().match(keyword)
        })
      }
    },
    methods: {
      ...mapActions({
        action_getReportList: 'testReportList/action_getReportList'
      }),
      renderReportItem: function (h, params) {
        return h('div', [
          h('a', {
            attrs: {
              href: '/AutomationReport/' + params.row.name,
              target: '_blank',
              class: 'reportLink'
            }
          }, [params.row.name])
        ])
      }
    }

  }
</script>
