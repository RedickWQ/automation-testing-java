<style >
  #app {
    min-height: 800px;
    height: 100%;
  }
  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
        from { transform: rotate(0deg);}
        50%  { transform: rotate(180deg);}
        to   { transform: rotate(360deg);}
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
                  <Icon type="ios-list-outline"></Icon> Test Suite Execution Result
              </Breadcrumb-item>


            </Breadcrumb>
          </Col>
        </Row>
          <Row type="flex" style="margin-top:20px;">
            <Col :span="22" style="background-color:white;" offset="1">
              <div v-if="testsuite">

                <!--testsuite basic info -->
                <Card :padding="5" style="margin-top: 5px;">
                  <p slot="title" style="height: 30px;">
                    <Icon type="compose"></Icon>
                    {{testsuite.name}}
                  </p>

                  <Form style="margin-left: 10px;">
                    <Row type="flex">
                      <Col :span="5">
                        <Form-item  label="Case Run Mode:">
                            <span v-text="caseRunMode"></span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="Iteration Run Mode:" >
                            <span v-text="iterationRunMode"></span>
                        </Form-item>
                      </Col>
                    </Row>
                  </Form>

                  <Form style="margin-left: 10px;">
                    <Row type="flex">
                      <Col :span="5">
                        <Form-item  label="OverallStatus:">
                            <span v-if="testsuite.status === 'Passed'" style="color: #19be6b" >Passed</span>
                            <span v-if="testsuite.status === 'Failed'" style="color: #ed3f14" >Failed</span>
                            <span v-if="testsuite.status === 'Running'" style="color: #2d8cf0" >Running</span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="StartTime:" >
                            <span v-text="testsuite.startTime" ></span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="EndTime:" >
                            <span v-text="(testsuite.endTime || '--')" ></span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="Duration:" >
                            <span v-text="getTestSuiteDuration()" ></span>
                        </Form-item>
                      </Col>
                    </Row>
                  </Form>



                  <Form style="margin-left: 10px;">
                    <Row type="flex">
                      <Col :span="5">
                        <Form-item  label="Passed:" style="color: #19be6b">
                            <span v-text="passedCaseCount" ></span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="Failed:" style="color: #ed3f14">
                            <span v-text="failedCaseCount" ></span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="Running:" style="color: #2d8cf0">
                            <span v-text="runningCaseCount" ></span>
                        </Form-item>
                      </Col>
                      <Col :span="5">
                        <Form-item  label="Total:" >
                            <span v-text="testsuite.testcaseList.length" ></span>
                        </Form-item>
                      </Col>
                    </Row>
                  </Form>

                </Card>

                <!-- testcase detail-->
                <Card :padding="5" style="margin-top: 5px;"
                v-for="(testcaseRuntimeInstance, index) in testsuite.testcaseRuntimeInstanceList"
                :key="index"
                >
                    <p slot="title" style="height: 30px;">
                      <Icon type="compose"></Icon>
                      {{testcaseRuntimeInstance.testcase.name}}
                      <Button size="small" type="ghost" @click="toggleCaseDetail(index)" >
                        <span v-if="testcaseRuntimeInstance.status === 'Failed'" style="color: #ed3f14;"> {{testcaseRuntimeInstance.status}} </span>
                        <span v-if="testcaseRuntimeInstance.status === 'Passed'" style="color: #19be6b;"> {{testcaseRuntimeInstance.status}} </span>
                        <span v-if="testcaseRuntimeInstance.status === 'Running'" style="color: #2d8cf0;"> {{testcaseRuntimeInstance.status}} </span>
                      </Button>
                    </p>

                    <div :id='"testcase-" + index' style="display: none">
                      <Tabs v-if="testcaseRuntimeInstance && testcaseRuntimeInstance.iterationList.length > 0">
                         <TabPane v-for="(result, index) in testcaseRuntimeInstance.iterationList"  :key="index" :label="renderResultTabLabel(result, index)">
                           <resultViewer :result="result">

                           </resultViewer>
                         </TabPane>
                      </Tabs>
                    </div>
                </Card>

            </div>
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
  import resultViewer from '../testcaseDetail/resultViewer'
  import $ from 'jQuery'
  import utils from '../../lib/utils'
  export default {
    data () {
      return {
      }
    },
    components: {
      Layout,
      resultViewer
    },
    created: function () {
      var vm = this
      this.action_getTestSuiteExecutionResult(function (res) {
        // only for error
        vm.$Message.error(res.errMsg)
        setTimeout(function () {
          window.location.href = '/testcaseList'
        }, 2000)
      })
    },
    mounted: function () {

    },
    computed: {
      ...mapGetters({
        testsuite: 'testsuiteExecutionResult/testsuite',
        passedCaseCount: 'testsuiteExecutionResult/passedCaseCount',
        failedCaseCount: 'testsuiteExecutionResult/failedCaseCount',
        runningCaseCount: 'testsuiteExecutionResult/runningCaseCount',
        caseRunMode: 'testsuiteExecutionResult/caseRunMode',
        iterationRunMode: 'testsuiteExecutionResult/iterationRunMode'
      })
    },
    methods: {
      ...mapActions({
        action_getTestSuiteExecutionResult: 'testsuiteExecutionResult/action_getTestSuiteExecutionResult'
      }),
      renderResultTabLabel: function (result, index) {
        var vm = this
        return function (h) {
          return h('div', [
            h('Icon', {
              props: vm.getResultIconProps(result),
              'class': {
                'demo-spin-icon-load': (result.status === 'Running')
              }
            }),
            h('span', 'Result_' + (index + 1))
          ])
        }
      },
      getResultIconProps: function (result) {
        var iconType
        var iconColor
        var status = result.status
        switch (status) {
          case 'Passed':
            iconType = 'checkmark'
            iconColor = '#19be6b'
            break
          case 'Failed':
            iconType = 'close'
            iconColor = 'red'
            break
          case 'Warning':
            iconType = 'alert'
            iconColor = '#ff9900'
            break
          case 'Error':
            iconType = 'close'
            iconColor = 'red'
            break
          case 'Running':
            iconType = 'load-c'
            iconColor = '#5cadff'
            break
          default:
            iconType = 'information'
            iconColor = 'blue'
            break
        }
        return {
          type: iconType,
          size: 15,
          color: iconColor
        }
      },
      toggleCaseDetail: function (index) {
        $('div#testcase-' + index).toggle('fast')
      },
      getTestSuiteDuration: function () {
        if (this.testsuite.status === 'Running') {
          return '--'
        }
        var milliSecond = this.testsuite.duration
        return utils.formatDurationFromMilliseconds(milliSecond)
      }
    }

  }
</script>
