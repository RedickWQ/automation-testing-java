<style scoped>
  #app {
    min-height: 800px;
    height: 100%;
  }
  .settings-form-item {
      margin-bottom: 1px;
      vertical-align: top;
      zoom: 1;
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
                  <Icon type="ios-list-outline"></Icon> TestSuite List
              </Breadcrumb-item>


            </Breadcrumb>
          </Col>
        </Row>
          <Row  style="margin-top:20px;">
            <Col :span="22" style="background-color:white;" offset="1">
              <Row style="margin-top: 10px;" :gutter="16" >
                <Col :span="2">
                  <Button
                    type="primary"
                    @click="openSuiteCreateModal"
                  >
                  Create
                  </Button>
                </Col>
              </Row>
              <Row style="margin-top:10px;">
                <Col >
                  <Table stripe :columns="testSuiteListTableOptions.columns" :data="testSuiteList"

                    :loading="testSuiteListTableOptions.isLoading"
                    ref="testSuiteListTable"
                  ></Table>
                </Col>
              </Row>
            </Col>
          </Row>

          <!-- create testsuite modal-->
          <Modal width='1000'
              v-model='testSuiteEditModalOption.display'
              @on-cancel="closeTestSuiteEditModal"
              :mask-closable="false"
              :closable="false"
                >
                <p slot="header" style="text-align:center">
                  <Icon type="gear-a"></Icon>
                  <span>Test Suite</span>
                </p>

                <div v-if="currentTestSuite">
                <Card :padding="5" style="margin-top: 5px;" >
                    <p slot="title">
                      <Icon type="ios-list-outline"></Icon>
                      Case List
                      <span>(Total: {{this.currentTestSuite.testcaseList.length}})</span>
                    </p>

                    <Form ref="testSuiteInfoForm"
                    :rules="testSuiteInfoValidateRules"
                    style="margin-left: 5px;"
                    label-position="left"
                    :label-width="120"
                    :model="this.currentTestSuite"
                    >
                      <Row >
                        <Col  >
                          <Form-item  label="TestSuiteName:" prop="name" >
                            <Input  v-model="currentTestSuite.name" placeholder="required"
                                         >
                            </Input>
                          </Form-item>
                        </Col>
                      </Row>
                    </Form>

                    <Row style="margin-top: 10px;" :gutter="16" type="flex" align="middle" >
                      <Col :span="6" >
                        <Select
                          ref="testcaseSelector"

                          filterable
                          remote
                          transfer
                          clearable
                          placeholder="Search testcase.."
                          :remote-method="searchTestCase"
                          :loading="testSuiteEditModalOption.isLoadingForSearching"
                          @on-change="onTestcaseSelected"

                          >
                          <Option v-for="(testcase, index) in testSuiteEditModalOption.testcaseOptionList"
                            :value="testcase.name"
                            :key="index"
                            :label="testcase.name"
                            >
                          </Option>

                        </Select>
                      </Col>

                      <Col :span="2">
                        <Button
                          type="primary"
                          size="small"
                          @click="addTestCase"
                        >
                        Add
                        </Button>
                      </Col>
                    </Row>

                    <Row style="margin-top:10px;">
                      <Col>
                        <Table stripe
                        :columns="testSuiteEditModalOption.testcaseListTableColumns"
                        :data="currentTestSuite.testcaseList"

                        ></Table>
                      </Col>
                    </Row>
                </Card>

                <Card :padding="5" style="margin-top: 5px;" >
                    <p slot="title">
                      <Icon type="settings"></Icon>
                      Run settings
                    </p>
                    <Form :show-message="false">
                    <Row type="flex" justify="start" :gutter="16" align="middle">
                      <!-- test env -->
                      <Col :span="22" :offset="1">

                          <Form-item  label="TestEnv:" class="settings-form-item">
                            <RadioGroup v-model='testSuiteEditModalOption.selectedEnv'>
                              <Radio v-for="(env, index) in environmentList" :label="env.envName" :key="index">
                                   <span>{{env.envName}}</span>
                               </Radio>
                            </RadioGroup>
                          </Form-item>

                      </Col>

                    </Row>

                    <Row type="flex" justify="start" :gutter="16" align="middle">
                      <!-- test env -->
                       <Col :span="6" :offset="1">
                          <Form-item  label="Case Parallel Run:"  class="settings-form-item">
                            <i-switch v-model="this.currentTestSuite.isCaseParallel" size="small">

                            </i-switch>
                          </Form-item>
                      </Col>

                      <Col :span="6" :offset="1">
                         <Form-item  label="Iteration Parallel Run:"  class="settings-form-item">
                           <i-switch v-model="this.currentTestSuite.isIterationParallel" size="small">

                           </i-switch>
                         </Form-item>
                      </Col>

                    </Row>

                    <Row >

                      <Col :span="11" :offset="1">
                       <Form-item  label="Browser:"  class="settings-form-item">

                          <RadioGroup v-model='testSuiteEditModalOption.selectedBrowserType'  >
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
                        </Form-item>
                      </Col>
                      <Col :span="11" :offset="1">
                        <Form-item  label="WebDriver:"  class="settings-form-item">
                          <RadioGroup v-model='testSuiteEditModalOption.selectedWebDriverType'>
                            <Radio label="Local" >

                            </Radio>
                            <Radio label="Remote" >

                            </Radio>
                          </RadioGroup>
                        </Form-item>
                      </Col>
                  </Row>
                  </Form>
                </Card>




              </div>
              <div slot="footer">
                  <Button  @click="closeTestSuiteEditModal">Close</Button>
                  <Button  @click="validateTestSuite(saveTestSuite)"
                  :loading="testSuiteEditModalOption.isLoadingForSubmit"
                  type="primary"

                  >Save
                </Button>
              </div>
          </Modal>

      </div>
    </Layout>

  </div>
</template>
<script>
  import {mapGetters, mapActions} from 'vuex'
  // import requestHelper from '../../lib/requestHelper'
  import Layout from '../../components/common/Layout'
  import utils from '../../lib/utils'
  export default {
    data () {
      return {
        currentTestSuite: undefined,
        testSuiteListTableOptions: {
          isLoading: false,
          columns: [
            {
              title: 'Name',
              // width: 200,
              key: 'name'
            },
            {
              title: 'Case Count',
              align: 'center',
              render: this.renderTestcaseCount
            },
            {
              title: 'Environment',
              align: 'center',
              render: this.renderEnvironment
            },
            {
              title: 'Actions',
              align: 'center',
              render: this.renderTestSuiteActions
            }
          ]
        },
        testSuiteEditModalOption: {
          selectedTestcase: undefined,
          testcaseOptionList: [],
          isLoadingForSearching: false,
          display: false,
          isLoadingForSubmit: false,
          selectedEnv: undefined,
          selectedBrowserType: 'Chrome',
          selectedWebDriverType: 'Local',
          testcaseListTableColumns: [
            {
              title: 'name',
              key: 'name'
            },
            {
              title: 'Type',
              width: 150,
              align: 'center',
              render: this.renderTestcaseType
            },
            {
              title: 'action',
              align: 'center',
              render: this.renderTestcaseListOption
            }
          ]
        },
        testSuiteInfoValidateRules: {
          name: [
            {required: true, message: 'Please set testsuite name', trigger: 'blur', type: 'string'}
          ]
        }
      }
    },
    components: {
      Layout
    },
    created: function () {
      this.loadTestSuiteList()
      this.action_getEnvironmentList().then(res => {
      })
    },
    mounted: function () {

    },
    computed: {
      ...mapGetters({
        testSuiteList: 'testSuiteList/testSuiteList',
        environmentList: 'common/environmentList',
        method_getTypeDescription: 'testcaseList/method_getTypeDescription'
      })
    },
    methods: {
      ...mapActions({
        action_getTestSuiteList: 'testSuiteList/action_getTestSuiteList',
        action_deleteTestSuite: 'testSuiteList/action_deleteTestSuite',
        action_getEnvironmentList: 'common/action_getEnvironmentList',
        action_saveTestSuite: 'testSuiteList/action_saveTestSuite',
        action_searchTestcaseByKeyword: 'testSuiteList/action_searchTestcaseByKeyword',
        action_runTestSuite: 'testSuiteList/action_runTestSuite',
        action_getTestSuiteExecutionResult: 'testSuiteList/action_getTestSuiteExecutionResult'
      }),
      loadTestSuiteList: function () {
        var vm = this
        vm.testSuiteListTableOptions.isLoading = true
        this.action_getTestSuiteList().then(res => {
          vm.testSuiteListTableOptions.isLoading = false
        })
      },
      renderTestcaseCount: function (h, params) {
        var testsuite = params.row
        return h('div', [
          testsuite.testcaseList.length
        ])
      },
      renderEnvironment: function (h, params) {
        var testsuite = params.row
        return h('div', [
          testsuite.environmentVariable.envName
        ])
      },
      renderTestSuiteActions: function (h, params) {
        return h('div', [
          this.renderRunOption(h, params),
          this.renderEditOption(h, params),
          this.renderDeletePopTip(h, params)
        ])
      },
      renderEditOption: function (h, params) {
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
              // console.log('edit account')
              this.openSuiteEditModal(params.row)
            }
          }
        }, 'Edit')
      },
      renderDeletePopTip: function (h, params) {
        var vm = this
        return h('Poptip', {
          props: {
            placement: 'bottom',
            confirm: true,
            title: 'Comfirm'
          },
          on: {
            'on-ok': () => {
              vm.deleteTestSuite(params.row)
            },
            'on-cancel': () => {
              // do nothing
            }
          }
        }, [vm.renderDeleteOption(h, params)])
      },
      renderDeleteOption: function (h, params) {
        return h('Button', {
          attrs: {
            style: 'margin: 1px;'
          },
          props: {
            type: 'primary',
            size: 'small'
          }
        }, 'Delete')
      },
      renderRunOption: function (h, params) {
        var vm = this
        return h('Button', {
          attrs: {
            style: 'margin: 1px;'
          },
          props: {
            type: 'primary',
            size: 'small',
            loading: params.row.submitToRun || false
          },
          on: {
            click: () => vm.beforeRunTestSuite(params.row)
          }
        }, 'Run')
      },
      renderTestcaseType: function (h, params) {
        var type = params.row.caseType
        return h('div', [
          this.method_getTypeDescription(type)
        ])
      },
      renderTestcaseListOption: function (h, params) {
        return h('Button', {
          props: {
            type: 'primary',
            size: 'small'
          },
          on: {
            click: () => {
              var index = params.index
              this.currentTestSuite.testcaseList.splice(index, 1)
            }
          }
        }, 'Remove')
      },
      openSuiteCreateModal: function () {
        var vm = this
        vm.currentTestSuite = {
          name: '',
          testcaseList: [],
          isCaseParallel: true,
          isIterationParallel: true
        }
        vm.testSuiteEditModalOption.selectedEnv = undefined
        vm.testSuiteEditModalOption.selectedBrowserType = 'Chrome'
        vm.testSuiteEditModalOption.selectedWebDriverType = 'Local'
        vm.testSuiteEditModalOption.display = true
        vm.$nextTick(function () {
          vm.$refs.testSuiteInfoForm.resetFields()
        })
      },
      openSuiteEditModal: function (testsuite) {
        // console.log(testsuite)
        var vm = this
        vm.currentTestSuite = utils.deepCloneObject(testsuite)
        var envName = testsuite.environmentVariable.envName
        var browserType = testsuite.environmentVariable.applicationVariables.common.BrowserType
        var WebDriverType = testsuite.environmentVariable.applicationVariables.common.WebDriverType
        vm.testSuiteEditModalOption.selectedEnv = envName
        vm.testSuiteEditModalOption.selectedBrowserType = browserType
        vm.testSuiteEditModalOption.selectedWebDriverType = WebDriverType
        this.testSuiteEditModalOption.display = true
        vm.$nextTick(function () {
          vm.$refs.testSuiteInfoForm.validate()
        })
      },
      closeTestSuiteEditModal: function () {
        this.testSuiteEditModalOption.display = false
        this.currentTestSuite = undefined
        this.testSuiteEditModalOption.selectedTestcase = undefined
      },
      deleteTestSuite: function (testsuite) {
        this.action_deleteTestSuite(testsuite.id)
      },
      validateTestSuite: function (callback) {
        var vm = this
        this.$refs.testSuiteInfoForm.validate(function (valid) {
          if (valid) {
            if (vm.currentTestSuite.testcaseList.length === 0) {
              vm.$Message.error('Testsuite cannot be empty!')
              return
            }

            callback()
          }
        })
      },
      saveTestSuite: function () {
        var env = this.environmentList.find(e => e.envName === this.testSuiteEditModalOption.selectedEnv)
        if (!env) {
          this.$Message.error('Please select test environment')
          return
        }
        // start execution
        env.applicationVariables.common = {
          BrowserType: this.testSuiteEditModalOption.selectedBrowserType,
          WebDriverType: this.testSuiteEditModalOption.selectedWebDriverType
        }
        this.currentTestSuite.environmentVariable = env
        this.testSuiteEditModalOption.isLoadingForSubmit = true
        var vm = this
        vm.action_saveTestSuite(vm.currentTestSuite).then(res => {
          if (res.success) {
            vm.$Message.success('TestSuite saved successfully.')
            setTimeout(function () {
              vm.closeTestSuiteEditModal()
              vm.loadTestSuiteList()
            }, 1000)
          } else {
            vm.$Message.error(res.errMsg)
          }
          this.testSuiteEditModalOption.isLoadingForSubmit = false
        })
      },
      searchTestCase: function (keyword) {
        var vm = this
        this.testSuiteEditModalOption.isLoadingForSearching = true
        if (keyword !== '') {
          this.action_searchTestcaseByKeyword(keyword).then(res => {
            vm.testSuiteEditModalOption.testcaseOptionList = res.result
            this.testSuiteEditModalOption.isLoadingForSearching = false
          })
        }
      },
      onTestcaseSelected: function (testcaseName) {
        if (testcaseName !== '') {
          console.log(testcaseName)
          this.testSuiteEditModalOption.selectedTestcase =
             this.testSuiteEditModalOption.testcaseOptionList.filter(t => t.name === testcaseName)[0] || undefined
          console.log(this.testSuiteEditModalOption.selectedTestcase)
        }
      },
      addTestCase: function () {
        if (this.testSuiteEditModalOption.selectedTestcase) {
          this.currentTestSuite.testcaseList.push(this.testSuiteEditModalOption.selectedTestcase)
        }
      },
      beforeRunTestSuite: function (testsuite) {
        var vm = this
        testsuite.submitToRun = true
        vm.action_getTestSuiteExecutionResult().then(res => {
          if (res.success) {
            if (res.result.isRunning) {
              vm.$Message.error('The previous suite is still running.')
              setTimeout(() => {
                window.location.href = '/testsuiteExecutionResult'
              }, 1000)
            } else {
              vm.runTestSuite(testsuite)
            }
          } else {
            if (res.errMsg === 'No suite is running') {
              vm.runTestSuite(testsuite)
            }
          }
        })
      },
      runTestSuite: function (testsuite) {
        this.action_runTestSuite(testsuite).then(res => {
          this.$Message.success('Test suite is successfully started')
          // this.batchRunSettingModalOptions.isLoadingForSubmit = false
          // redirect
          setTimeout(() => {
            window.location.href = '/testsuiteExecutionResult'
          }, 1000)
        }).catch(res => {
          this.$Message.error(res.errMsg)
          // this.batchRunSettingModalOptions.isLoadingForSubmit = false
          testsuite.submitToRun = false
        })
      }
    }
  }
</script>
