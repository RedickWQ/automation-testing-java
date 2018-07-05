<style>
  textarea.ivu-input {
    max-width: 100%;
    height: auto;
    vertical-align: bottom;
    font-size: 12px !important;
  }

  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  @keyframes ani-demo-spin {
        from { transform: rotate(0deg);}
        50%  { transform: rotate(180deg);}
        to   { transform: rotate(360deg);}
  }
  .spin-container{
    display: inline-block;
     width: 500px;
     height: 100px;
     position: relative;

  }
</style>
<style scoped>
  #app {
    min-height: 800px;
    height: 100%;
  }
  .info-text{
    color: #19be6b;
  }
  .ivu-row-flex {
    height: 36px;
  }

</style>
<template>
<div id="app" class="layout" theme="light" >

    <Layout>

      <div slot="mainBody"  >

        <Row style="margin-top:20px">
          <Col :span="22" style="background-color:white;" offset="1">
            <Breadcrumb>
              <Breadcrumb-item href="/">
                  <Icon type="ios-home-outline"></Icon> Home
              </Breadcrumb-item>
              <Breadcrumb-item href="/testcaseList">
                  <Icon type="ios-list-outline"></Icon> Testcase List
              </Breadcrumb-item>
              <Breadcrumb-item >
                  <Icon type="android-checkmark-circle"></Icon> <span v-text="testcase ? testcase.name : ''"></span>
              </Breadcrumb-item>

            </Breadcrumb>
          </Col>
        </Row>
          <Row   type="flex" justify="center" style="margin-top:20px;" v-if="!testcase">
            <!-- <Col :span="22"  offset="1"> -->
              <div class="spin-container">
                  <Spin fix></Spin>
              </div>
            <!-- </Col> -->
          </Row>

          <Row  style="margin-top:20px;" v-if="testcase">
            <Col :span="22" style="background-color:white;" offset="1">

                  <Card :padding="5" >
                      <p slot="title">
                        <Icon type="grid"></Icon>
                        Basic Info
                      </p>
                      <Form>
                        <Row type="flex" justify="start">

                            <Col span="7" offset="1">
                              <Form-item  label="TestComponent:" >
                                  <span v-text="testcase.componentName" class="info-text"></span>
                              </Form-item>
                            </Col>
                            <Col span="7" offset="1">
                              <Form-item  label="Testcase:" >
                                  <span v-text="testcase.name" class="info-text"></span>
                              </Form-item>
                            </Col>
                            <Col span="7" offset="1">
                              <Form-item  label="EntryPoint:" >
                                  <span v-text="testcase.entryPoint" class="info-text"></span>
                              </Form-item>
                            </Col>
                        </Row>
                        <Row type="flex" justify="start">
                          <Col span="7" offset="1">
                            <Form-item  label="Description:" >
                                <span v-text="testcase.description" class="info-text"></span>
                            </Form-item>
                          </Col>
                          </Row>
                      </Form>
                  </Card>

                  <Card :padding="5" style="margin-top: 5px;">
                      <p slot="title">
                        <Icon type="gear-a"></Icon>
                        Iterations
                      </p>

                    <Row justify="start" style="margin-top: 8px;">
                      <Col :span="10" >
                        <Button type="primary" size="small" @click="beginAddIteration"> Add </Button>
                        <Button size="small" @click="exportData">Export</Button>
                        <Button size="small" @click="saveIterationParameters"
                        :disabled="!isTestDataSavable"
                        :loading="isSavingTestData">Save</Button>
                      </Col>

                    </Row>

                    <Row style="margin-top: 8px;">
                      <Col>
                        <Table stripe :columns="iterationTableColumns" :data="testcase.iterationList"
                          @on-select="onIterationSelect"
                          @on-selection-change="onIterationSelectionChange"
                          ref="iterationTable"
                        ></Table>
                      </Col>
                    </Row>


                    <Modal width='1000'
                      :title="iterationEditModalOption.title"
                      v-model='iterationEditModalOption.display'
                      scrollable

                      @on-cancel="cancelEditIteration"
                        >
                      <div v-if="iterationEditModalOption.data">
                        <Form v-for="(parameter, index) in iterationEditModalOption.data.testParameterList" :key="index" label-position="left" >
                          <Row type="flex" justify="start">
                            <Col span="15" offset="1">
                              <Form-item  :label="parameter.name" :label-width='80'>
                                <Input  v-model="parameter.value" placeholder="Please set value"
                                              style="width: 500px">
                                </Input>
                              </Form-item>
                            </Col>
                            <Col span="7" offset="1">
                              <Form-item  label="DataType:" :label-width='100'>
                                <span v-text="parameter.type"> </span>
                              </Form-item>
                            </Col>
                          </Row>
                        </Form>

                      </div>
                      <div slot="footer">
                          <Button  size="large" type="primary" @click="finishEditIteration">ok</Button>
                      </div>
                    </Modal>

                  </Card>
                  <Card :padding="5" style="margin-top: 5px;">
                      <p slot="title" style="height: 30px;">
                        <Icon type="erlenmeyer-flask"></Icon>
                        <!-- <i-switch v-model="isRunWithStaticDataSource" size="small" ></i-switch><span style="margin-left: 5px;">Run with Static DataSource</span> -->
                        Execution
                      </p>

                        <Row type="flex" justify="start" :gutter="16" align="middle">
                          <!-- test env -->
                          <Col :span="2" :offset="1">
                            <span>Environment:</span>
                          </Col>
                          <Col :span="5" >

                             <RadioGroup v-model='selectedEnv' @on-change="onEnvSelected">
                               <Radio v-for="(env, index) in environmentList" :label="env.envName" :key="index">
                                    <span>{{env.envName}}</span>
                                </Radio>
                             </RadioGroup>
                          </Col>
                        </Row>

                        <Row type="flex" justify="start" :gutter="16" align="middle">

                          <Col :span="2" :offset="1">
                            <span>Parallel Run:</span>
                          </Col>
                          <Col :span="5" >
                              <i-switch v-model="isParallel" size="small">

                              </i-switch>
                          </Col>
                        </Row>

                        <Row type="flex" justify="start" :gutter="16" align="middle">
                          <Col :span="2" :offset="1">
                            <span>Log Level:</span>
                          </Col>
                          <Col :span="5" >
                            <Select v-model="logLevel" style="width:200px">
                               <Option v-for="logLevel in logLevels" :value="logLevel.value" :key="logLevel.name">{{ logLevel.name }}</Option>
                           </Select>
                          </Col>
                        </Row>
                          <!-- BrowserType -->
                          <Row v-if="testcase.caseType === 3">
                            <Col :span="2" :offset="1">
                              <span> Browser: </span>
                            </Col>
                            <Col :span="7" >

                                <RadioGroup v-model='selectedBrowserType'  @on-change="onBrowserTypeSelected" >
                                  <Radio label="Chrome" value="ch">

                                  </Radio>
                                  <Radio label="Firefox" value="ff">

                                  </Radio>
                                  <Radio label="IE" value="ie">

                                  </Radio>
                                  <Radio label="Safari" value="sf">

                                  </Radio>
                                  <Radio label="Edge" value="eg">

                                  </Radio>
                                </RadioGroup>

                            </Col>
                            <Col :span="2" :offset="1">
                              <span> DriverType: </span>
                            </Col>
                            <Col :span="5">
                              <RadioGroup v-model='selectedWebDriverType'>
                                <Radio label="Local" >

                                </Radio>
                                <Radio label="Remote" >

                                </Radio>
                              </RadioGroup>
                            </Col>
                        </Row>


                        <Row type="flex" justify="start" :gutter="16" align="middle">

                          <Col :span="2" :offset="1">
                            <Button size="large" style="position:relative;" type="ghost"
                                @click="runTestcase" :loading="isCaseRunning"
                                ><Icon type="play" size="15" color="green"></Icon>
                                Run
                            </Button>
                          </Col>
                          <Col >
                            <span v-if="executionResult && executionResult.totalDuration">Total Duration: {{executionResult.totalDuration}} ms</span>
                          </Col>
                        </Row>


                            <Tabs v-if="executionResult && executionResult.iterationList.length > 0">
                               <TabPane v-for="(result, index) in executionResult.iterationList"  :key="index" :label="renderResultTabLabel(result, index)">
                                 <resultViewer :result="result">

                                 </resultViewer>
                               </TabPane>
                            </Tabs>

                            <Form  v-if="errorMessage">
                              <Form-item  label="Error:" :label-width='100'>
                                <Input readonly="true" v-model="errorMessage" > </Input>
                              </Form-item>
                              <Form-item  label="stackTrace:" :label-width='100'>
                                <Input class="textarea" readonly="true" type="textarea" v-model="stackTrace" :autosize="{minRows: 5,maxRows: 50}"> </Input>
                              </Form-item>

                            </Form>

                  </Card>

            </Col>
          </Row>
      </div>
    </Layout>

  </div>
</template>
<script>
  import {mapGetters, mapMutations, mapActions} from 'vuex'
  import Layout from '../../components/common/Layout'
  import JSONEditor from '../../components/control/JsonEditor'
  import resultViewer from './resultViewer'
  import utils from '../../lib/utils'
  export default {
    data () {
      return {
        isRunning: false,
        testResult: {},
        isRunWithStaticDataSource: false,
        errorMessage: undefined,
        stackTrace: undefined,
        selectedIterations: [],
        iterationEditModalOption: {
          data: undefined,
          display: false,
          title: 'Test Parameter',
          index: undefined
        },
        isSavingTestData: false,
        selectedEnv: undefined,
        selectedBrowserType: 'Chrome',
        selectedWebDriverType: 'Local',
        isParallel: true,
        logLevel: 0,
        logLevels: [
          {
            name: 'debug',
            value: 0
          },
          {
            name: 'info',
            value: 1
          },
          {
            name: 'warning',
            value: 2
          },
          {
            name: 'error',
            value: 3
          },
          {
            name: 'fatal',
            value: 4
          }
        ]
      }
    },
    components: {
      Layout,
      'json-editor': JSONEditor,
      resultViewer
    },
    created: function () {

    },
    mounted: function () {
      var vm = this
      var params = {
        componentName: this.$route.params.componentName,
        entryPoint: this.$route.params.entryPoint
      }
      // console.log(params)
      vm.action_loadTestcaseDetail(params)
      vm.action_getEnvironmentList()
    },
    computed: {
      ...mapGetters([
        'testcase',
        'resultList',
        'isCaseRunning',
        'executionResult',
        'environmentList'
      ]),
      // iterationBasicDataList: function () {
      //   var iterationList = this.testcase.iterationList
      //   return iterationList.map(iteration => {
      //     var rowData = {
      //       iteration: iteration,
      //       parameterCount: iteration.testParameterList.length,
      //       _checked: false
      //     }
      //     var parameterList = iteration.testParameterList || []
      //     parameterList.forEach(p => {
      //       rowData[p.name] = p.value
      //     })
      //     return rowData
      //   })
      // },
      iterationTableColumns: function () {
        var columns = [
          {
            type: 'selection',
            width: 60,
            align: 'center',
            fixed: 'left'
          },
          {
            type: 'index',
            width: 50,
            align: 'center',
            fixed: 'left'
          }
        ]
        var parameterList = this.testcase.iterationList[0].testParameterList || []
        // var slicedParameterList = parameterList.slice(0, 4) || []
        var parameterInfoColumns = parameterList.map(p => {
          return {
            title: p.name,
            width: 200,
            render: this.getColumnRenderFunc(p.name),
            align: 'center',
            ellipsis: true,
            key: p.name
          }
        })
        // if (parameterInfoColumns.length < parameterList.length) {
        //   parameterInfoColumns.push({
        //     title: '...',
        //     width: 100,
        //     align: 'center',
        //     render: (h, params) => h('div', ['..'])
        //   })
        // }
        columns = columns.concat(parameterInfoColumns)
        var actionColumn = {
          title: 'Action',
          width: 140,
          fixed: 'right',
          aligh: 'center',
          render: this.renderIterationAction
        }
        columns.push(actionColumn)
        return columns
      },
      isTestDataSavable: function () {
        if (!this.testcase.isDatasourceConfigured) {
          return false
        }
        var parameterList = this.testcase.iterationList[0].testParameterList
        if (parameterList && parameterList.length !== 0) {
          return true
        } else {
          return false
        }
      }
    },
    methods: {
      ...mapMutations([
        'mutation_updateExecutionResult',
        'mutation_updateTestcase'
      ]),
      ...mapActions([
        'action_getEnvironmentList',
        'action_loadTestcaseDetail',
        'action_runTestcase',
        'action_getExecutionResult',
        'action_saveTestData'
      ]),
      onIterationSelect: function (selection, row) {
        // console.log(selection)
        // console.log(row)
      },
      onIterationSelectionChange: function (selection) {
        this.selectedIterations = selection
      },
      getParameterColumnWidth: function (p) {
        var length1 = p.name.length
        var length2 = p.value.length
        var length = length1 > length2 ? length1 : length2
        console.log(length)
        return length * 10 + 50
      },
      getColumnRenderFunc: function (columnName) {
        return function (h, params) {
          var iteration = params.row
          return h('div', [
            iteration.testParameterList.find(p => p.name === columnName).value
          ])
        }
      },
      renderIterationAction: function (h, params) {
        return h('div', [
          this.renderEditIteration(h, params),
          this.renderDeleteIteration(h, params)
        ])
      },
      renderEditIteration: function (h, params) {
        var iteration = params.row
        var index = params.index
        return h('Button', {
          attrs: {
            style: 'margin: 1px;'
          },
          props: {
            type: 'primary',
            size: 'small'
          },
          on: {
            click: () => {
              // console.log(iteration)
              this.beginEditIteration(iteration, index)
            }
          }
        }, 'Edit')
      },
      renderDeleteIteration: function (h, params) {
        var index = params.index
        var vm = this
        return h('Button', {
          attrs: {
            style: 'margin: 1px;'
          },
          props: {
            type: 'primary',
            size: 'small',
            disabled: vm.testcase.iterationList.length === 1
          },
          on: {
            click: () => {
              vm.testcase.iterationList.splice(index, 1)
            }
          }
        }, 'Remove')
      },
      beginAddIteration: function () {
        this.iterationEditModalOption.index = undefined
        var defaultIteration = this.testcase.iterationList[0]
        this.iterationEditModalOption.data = utils.deepCloneObject(defaultIteration)
        this.iterationEditModalOption.display = true
      },
      beginEditIteration: function (iteration, index) {
        this.iterationEditModalOption.index = index
        // console.log(index)
        this.iterationEditModalOption.data = utils.deepCloneObject(iteration)
        this.iterationEditModalOption.display = true
      },
      cancelEditIteration: function () {
        this.iterationEditModalOption.display = false
        this.iterationEditModalOption.data = undefined
      },
      finishEditIteration: function () {
        if (this.iterationEditModalOption.index !== undefined) {
          var index = this.iterationEditModalOption.index
          // console.log(this.iterationEditModalOption.data)
          var sourceParameterList = this.testcase.iterationList[index].testParameterList
          var updatedParameterlist = this.iterationEditModalOption.data.testParameterList
          sourceParameterList.forEach(p => {
            var updatedParameter = updatedParameterlist.find(i => i.name === p.name)
            p.value = updatedParameter.value
          })
        } else {
          this.testcase.iterationList.push(this.iterationEditModalOption.data)
        }
        this.mutation_updateTestcase(this.testcase)
        this.iterationEditModalOption.display = false
        this.iterationEditModalOption.data = undefined
        // console.log(this.testcase)
      },
      exportData: function () {
        // remove first 2 columns and the last column
        var totalLength = this.iterationTableColumns.length
        var columns = this.iterationTableColumns.filter((col, index) => index > 1 && index < totalLength - 1)
        // console.log(columns)
        this.$refs.iterationTable.exportCsv({
          filename: this.testcase.name,
          columns: columns,
          //  data: this.data7.filter((data, index) => index < 4)
          data: this.getExportData()
        })
      },
      getExportData: function () {
        var parameterCollectionList = this.testcase.iterationList.map(iteration => {
          var parameterValues = {}
          var parameterList = iteration.testParameterList
          parameterList.forEach(p => {
            parameterValues[p.name] = p.value
          })
          return parameterValues
        })
        // console.log(parameterCollectionList)
        return parameterCollectionList
      },
      runTestcase: function () {
        if (this.selectedIterations.length === 0) {
          this.$Message.error('Please select at lease one iteration!')
          return
        }
        if (!this.selectedEnv) {
          this.$Message.error('Please select test environment!')
          return
        }
        var vm = this
        vm.mutation_updateExecutionResult()
        // vm.isRunning = true
        var testcaseToRun = utils.deepCloneObject(this.testcase)
        // console.log(this.selectedIterations)
        testcaseToRun.iterationList = this.selectedIterations
        // append environment
        var environmentVariable = this.getSelectedEnv()
        // console.log(environmentVariable)

        // inject extra env form GUI test
        if (this.testcase.type === 3) {
          environmentVariable.applicationVariables.common = {
            BrowserType: this.selectedBrowserType,
            WebDriverType: this.selectedWebDriverType
          }
        }
        testcaseToRun.environmentVariable = environmentVariable
        vm.errorMessage = undefined
        vm.stackTrace = undefined
        // console.log(this.testcase)
        console.log('runcase')
        this.action_runTestcase({
          testcase: testcaseToRun,
          isParallel: vm.isParallel,
          logLevel: vm.logLevel
        }
        ).then(res => {
          if (res.success) {
            // console.log('res.result')
            // console.log(res.result)
            vm.mutation_updateExecutionResult(res.result)
          } else {
            vm.errorMessage = res.errMsg
            vm.stackTrace = res.stackTrace
            // this.$refs.resultEditor.editor.set({err: res.errMsg})
            // this.$Notice.error({
            //   title: 'Failed'
            // })
          }
          vm.action_getExecutionResult({
            componentName: vm.testcase.componentName,
            entryPoint: vm.testcase.entryPoint
          })
        })
      },
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
          case 'Timeout':
            iconType = 'close'
            iconColor = 'grey'
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
      onEnvSelected: function () {
        // console.log(this.selectedEnv)
      },
      getSelectedEnv: function () {
        var env = this.environmentList.find(e => e.envName === this.selectedEnv)
        return env
      },
      onBrowserTypeSelected: function () {
        // console.log(this.selectedBrowserType)
      },
      saveIterationParameters: function () {
        var vm = this
        var testcaseToSave = utils.deepCloneObject(this.testcase)
        this.isSavingTestData = true
        this.action_saveTestData(testcaseToSave).then(res => {
          if (res.success) {
            vm.$Message.success('Saved successfully!')
          } else {
            vm.$Message.error(res.errMsg)
          }
          this.isSavingTestData = false
        })
      }
    }
  }
</script>
