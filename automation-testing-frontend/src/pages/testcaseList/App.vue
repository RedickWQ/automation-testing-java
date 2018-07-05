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
      <div slot="mainBody" >

        <Row  style="margin-top:20px">
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
          <Row  style="margin-top:20px;">

            <Col :span="22" style="background-color:white;" offset="1">

              <!-- search condition areas-->
              <Row type="flex" :gutter="16">
                <Col span="5">
                  <Input v-model.trim="searchOptions.keyword" placeholder="Search by name or tag" icon="ios-search"> </Input>
                </Col>
              </Row>

              <!-- operation buttons-->
              <Row style="margin-top: 10px;" :gutter="16">
                <Col :span="10">
                  <Button type="primary" @click="batchAddToSuite">
                    <Icon type="ios-plus-outline" size="13"></Icon>
                    <span>Batch Add</span>
                  </Button>

                  <Button type="ghost" @click="clearSuite">
                    <Icon type="ios-trash-outline" size="13"></Icon>
                    <span>Clear</span>
                  </Button>

                  <Badge :count="this.batchRunSettingModalOptions.testcaseList.length" >
                    <Button type="ghost" @click="beginBatchExecution">
                      <Icon type="ios-gear-outline" size="13"></Icon>
                      <span>Batch Run</span>
                    </Button>
                  </Badge>
                </Col>
                <Col :span="3" style="float:right">
                    <Tag type="border" ><span style="font-size: 15px">Total:  {{filteredTestcaseList.length}}</span></Tag>
                </Col>
              </Row>


              <!-- table list area-->
              <Row style="margin-top: 10px;">
                <Col :span="24">
                  <Table stripe :columns="testcaseListTableOption.columns" :data="filteredTestcaseList"
                    :no-data-text='testcaseListTableOption.noDataText'
                    @on-selection-change="onTestcaseListSelectChange"
                    :loading="testcaseListTableOption.isLoading"
                    ref="testcaseListTable" style="width: auto"
                  ></Table>
                </Col>
              </Row>

            </Col>
          </Row>


          <!-- Batch run setting modal-->
          <Modal width='1000'
            :title="batchRunSettingModalOptions.title"
            v-model='batchRunSettingModalOptions.display'

            @on-cancel="closeBatchRunSettingModal"
              >
              <p slot="header" style="text-align:center">
                <Icon type="gear-a"></Icon>
                <span>Test Suite</span>
              </p>

              <Card :padding="5" style="margin-top: 5px;" >
                  <p slot="title">
                    <Icon type="ios-list-outline"></Icon>
                    Case List
                    <span>(Total: {{this.batchRunSettingModalOptions.testcaseList.length}})</span>
                  </p>

                  <Form ref="testSuiteInfoForm"
                  :rules="testSuiteInfoValidateRules"
                  style="margin-left: 5px;"
                  label-position="left"
                  :label-width="120"
                  :model="batchRunSettingModalOptions"
                  >
                    <Row >
                      <Col  >
                        <Form-item  label="TestSuiteName:" prop="testSuiteName" >
                          <Input  v-model="batchRunSettingModalOptions.testSuiteName" placeholder="Your testsuite name"
                                       >
                          </Input>
                        </Form-item>
                      </Col>
                    </Row>
                  </Form>

                  <Table stripe
                  :columns="batchRunSettingModalOptions.columns"
                  :data="this.batchRunSettingModalOptions.testcaseList"

                  ></Table>

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
                          <RadioGroup v-model='batchRunSettingModalOptions.selectedEnv' @on-change="onEnvSelected">
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
                          <i-switch v-model="batchRunSettingModalOptions.isCaseParallel" size="small">

                          </i-switch>
                        </Form-item>
                    </Col>

                    <Col :span="6" :offset="1">
                       <Form-item  label="Iteration Parallel Run:"  class="settings-form-item">
                         <i-switch v-model="batchRunSettingModalOptions.isIterationParallel" size="small">

                         </i-switch>
                       </Form-item>
                    </Col>

                  </Row>

                  <Row >

                    <Col :span="11" :offset="1">
                     <Form-item  label="Browser:"  class="settings-form-item">

                        <RadioGroup v-model='batchRunSettingModalOptions.selectedBrowserType'  >
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
                        <RadioGroup v-model='batchRunSettingModalOptions.selectedWebDriverType'>
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

            <Row type="flex" justify="center" :gutter="10" style="margin-top: 10px">
              <Col>
                <Button type="ghost"
                  @click="validateTestSuite(testsuiteStartRun)"
                  :loading="batchRunSettingModalOptions.isLoadingForSubmit"
                > Run
                </Button>
              </Col>
              <Col>
                <Button type="ghost"
                  @click="validateTestSuite(saveTestSuite)"
                  :loading="batchRunSettingModalOptions.isLoadingForSubmit"
                > Save
                </Button>
              </Col>
            </Row>

            <div slot="footer">
                <Button  @click="closeBatchRunSettingModal">Close</Button>
            </div>

          </Modal>
      </div>
    </Layout>

  </div>
</template>
<script>
  import {mapGetters, mapMutations, mapActions} from 'vuex'
  import Layout from '../../components/common/Layout'
  export default {
    data () {
      return {
        testcaseListTableOption: {
          isLoading: false,
          noDataText: 'No data present',
          columns: [
            {
              type: 'selection',
              width: 60,
              align: 'center',
              fixed: 'left'
            },
            {
              title: 'Testcase',
              // width: 200,
              // key: 'name'
              render: this.renderTestcaseNameAndDescription
            },
            {
              title: 'Componnet',
              // width: 200,
              key: 'componentName'
            },
            {
              title: 'Tags',
              width: 150,
              align: 'center',
              render: this.renderTestecaseTags
            },
            {
              title: 'Author',
              width: 100,
              align: 'center',
              key: 'author'
            },
            {
              title: 'Type',
              width: 150,
              align: 'center',
              render: this.renderTestcaseType
            },
            {
              title: 'Actions',
              align: 'center',
              render: this.renderTestcaseListActions
            }
          ]
        },
        searchOptions: {
          keyword: ''
        },
        selectedTestcaseList: [],
        testSuiteInfoValidateRules: {
          testSuiteName: [
            {required: true, message: 'Please set testsuite name', trigger: 'blur', type: 'string'}
          ]
        },
        batchRunSettingModalOptions: {
          testSuiteName: '',
          testcaseList: [],
          display: false,
          title: 'Batch Run',
          selectedEnv: undefined,
          isCaseParallel: true,
          isIterationParallel: true,
          selectedBrowserType: 'Chrome',
          selectedWebDriverType: 'Local',
          columns: [
            {
              title: 'name',
              // width: 200,
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
              render: this.renderBatchRunCaseListOption
            }
          ],
          isLoadingForSubmit: false
        }
      }
    },
    components: {
      Layout
    },
    created: function () {
      var vm = this
      // vm.testcaseListTableOption.noDataText = 'Loading...'
      vm.testcaseListTableOption.isLoading = true
      this.action_loadTestcaseList().then(res => {
        vm.testcaseListTableOption.noDataText = 'No data present'
        vm.testcaseListTableOption.isLoading = false
      })
    },
    mounted: function () {

    },
    computed: {
      ...mapGetters({
        testcaseList: 'testcaseList/testcaseList',
        method_getTypeDescription: 'testcaseList/method_getTypeDescription',
        environmentList: 'testcaseList/environmentList'
      }),
      filteredTestcaseList: function () {
        var keyword = this.searchOptions.keyword.toLowerCase()
        return this.testcaseList.filter(testcase => {
          return testcase.name.toLowerCase().match(keyword) ||
            this.isTagMatches(testcase, keyword)
        })
      }
    },
    methods: {
      ...mapMutations({
        mutation_updateCurrentTestcase: 'testcaseList/mutation_updateCurrentTestcase'
      }),
      ...mapActions({
        action_loadTestcaseList: 'testcaseList/action_loadTestcaseList',
        action_getEnvironmentList: 'testcaseList/action_getEnvironmentList',
        action_getTestSuiteExecutionResult: 'testcaseList/action_getTestSuiteExecutionResult',
        action_runTestSuite: 'testSuiteList/action_runTestSuite',
        action_saveTestSuite: 'testSuiteList/action_saveTestSuite'
      }),
      isTagMatches: function (testcase, keyword) {
        var tags = testcase.tags
        if (tags.length > 0) {
          for (var i = 0; i < tags.length; i++) {
            if (tags[i].toLowerCase().match(keyword)) {
              return true
            }
          }
          return false
        } else {
          return false
        }
      },
      renderTestcaseNameAndDescription: function (h, params) {
        var testcase = params.row
        return h('Poptip', {
          props: {
            trigger: 'hover',
            // content: testcase.description
            placement: 'bottom',
            transfer: true
          }
        }, [
          h('div', [testcase.name]),
          h('div', {
            slot: 'content'
          }, [testcase.description])
        ])
      },
      renderTestecaseCategory: function (h, params) {
        return h('div', [
          this.getCategory(params.row.category)
        ])
      },
      renderTestecaseTags: function (h, params) {
        return h('Poptip', {
          props: {
            trigger: 'hover',
            // title: params.row.tags.,
            placement: 'bottom'
          }
        },
          [
            h('Tag', params.row.tags.length > 0 ? params.row.tags.length + ' tags' : 'n/a'),
            h('div', {
              slot: 'content'
            }, [
              h('ul', params.row.tags.map(tag => {
                return h('li', {
                  style: {
                    textAlign: 'left',
                    padding: '4px'
                  }
                }, tag)
              }))
            ])
          ])
      },
      renderTestcaseType: function (h, params) {
        var type = params.row.caseType
        return h('div', [
          this.method_getTypeDescription(type)
        ])
      },
      renderTestcaseListActions: function (h, params) {
        return h('div', [
          this.renderAddToTestcaseListAction(h, params),
          this.renderViewDetailAction(h, params)
        ])
      },
      renderAddToTestcaseListAction: function (h, params) {
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
              var testcase = params.row
              this.batchRunSettingModalOptions.testcaseList.push(testcase)
            }
          }
        }, 'Add')
      },
      renderViewDetailAction: function (h, params) {
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
              var testcase = params.row
              // console.log(testcase)
              window.location.href = `/testcaseDetail/${testcase.componentName}/entryPoint/${testcase.entryPoint}`
            }
          }
        }, 'Detail')
      },
      renderBatchRunCaseListOption: function (h, params) {
        return h('Button', {
          props: {
            type: 'primary',
            size: 'small'
          },
          on: {
            click: () => {
              var index = params.index
              this.batchRunSettingModalOptions.testcaseList.splice(index, 1)
            }
          }
        }, 'Remove')
      },
      getCategory: function (packageFullName) {
        var list = packageFullName.split('.')
        var simpleName = list[list.length - 1]
        return simpleName
      },
      onTestcaseListSelectChange: function (selection) {
        this.selectedTestcaseList = selection
        // console.log(selection)
      },
      deselectAll: function () {
        this.$refs.testcaseListTable.selectAll(false)
      },
      batchAddToSuite: function () {
        if (this.selectedTestcaseList.length > 0) {
          // console.log(this.selectedTestcaseList)
          this.batchRunSettingModalOptions.testcaseList = this.batchRunSettingModalOptions.testcaseList.concat(this.selectedTestcaseList)
          console.log(this.batchRunSettingModalOptions.testcaseList)
          this.deselectAll()
        }
      },
      clearSuite: function () {
        this.batchRunSettingModalOptions.testcaseList = []
        this.batchRunSettingModalOptions.testSuiteName = ''
      },
      beginBatchExecution: function () {
        if (this.batchRunSettingModalOptions.testcaseList.length === 0) {
          this.$Message.error('Please add to testsuite first.')
          return
        }
        this.openBatchRunSettingModal()
      },
      openBatchRunSettingModal: function () {
        console.log('openmodal')
        var vm = this
        this.action_getEnvironmentList().then(() => {
          var lastEnvOption = this.environmentList[this.environmentList.length - 1]
          this.batchRunSettingModalOptions.selectedEnv = lastEnvOption.envName
          this.action_getTestSuiteExecutionResult().then((res) => {
            if (res.success) {
              if (res.result.isRunning) {
                vm.$Message.error('The previous suite is still running.')
                // todo to navigate to run result page
                return
              } else {
                this.batchRunSettingModalOptions.display = true
              }
            } else {
              if (res.errMsg === 'No suite is running') {
                this.batchRunSettingModalOptions.display = true
              }
            }
          })
        })
      },
      closeBatchRunSettingModal: function () {
        this.batchRunSettingModalOptions.display = false
      },
      onEnvSelected: function () {
        // console.log(this.batchRunSettingModalOptions.selectedEnv)
      },
      validateTestSuite: function (callback) {
        var vm = this
        this.$refs.testSuiteInfoForm.validate(function (valid) {
          if (valid) {
            if (vm.batchRunSettingModalOptions.testcaseList.length === 0) {
              vm.$Message.error('Testsuite cannot be empty!')
              return
            }

            callback()
          }
        })
      },
      testsuiteStartRun: function () {
        var env = this.environmentList.find(e => e.envName === this.batchRunSettingModalOptions.selectedEnv)
        if (!env) {
          this.$Message.error('Please select test environment')
          return
        }
        // start execution
        env.applicationVariables.common = {
          BrowserType: this.batchRunSettingModalOptions.selectedBrowserType,
          WebDriverType: this.batchRunSettingModalOptions.selectedWebDriverType
        }
        var testsuite = {
          name: this.batchRunSettingModalOptions.testSuiteName,
          testcaseList: this.batchRunSettingModalOptions.testcaseList,
          environmentVariable: env,
          isCaseParallel: this.batchRunSettingModalOptions.isCaseParallel,
          isIterationParallel: this.batchRunSettingModalOptions.isIterationParallel
        }
        // console.log(testsuite)
        this.batchRunSettingModalOptions.isLoadingForSubmit = true
        this.action_runTestSuite(testsuite).then(res => {
          this.$Message.success('Test suite is successfully started')
          this.batchRunSettingModalOptions.isLoadingForSubmit = false
          // redirect
          setTimeout(() => {
            window.location.href = '/testsuiteExecutionResult'
          }, 1000)
        }).catch(res => {
          this.$Message.error(res.errMsg)
          this.batchRunSettingModalOptions.isLoadingForSubmit = false
        })
      },
      saveTestSuite: function () {
        var env = this.environmentList.find(e => e.envName === this.batchRunSettingModalOptions.selectedEnv)
        if (!env) {
          this.$Message.error('Please select test environment')
          return
        }
        // start execution
        env.applicationVariables.common = {
          BrowserType: this.batchRunSettingModalOptions.selectedBrowserType,
          WebDriverType: this.batchRunSettingModalOptions.selectedWebDriverType
        }
        var testsuite = {
          name: this.batchRunSettingModalOptions.testSuiteName,
          environmentVariable: env,
          testcaseList: this.batchRunSettingModalOptions.testcaseList,
          isCaseParallel: this.batchRunSettingModalOptions.isCaseParallel,
          isIterationParallel: this.batchRunSettingModalOptions.isIterationParallel
        }
        var vm = this
        this.batchRunSettingModalOptions.isLoadingForSubmit = true
        this.action_saveTestSuite(testsuite).then(res => {
          if (res.success) {
            vm.$Message.success('TestSuite saved successfully.')
            vm.batchRunSettingModalOptions.testcaseList = []
            vm.batchRunSettingModalOptions.display = false
            vm.batchRunSettingModalOptions.testSuiteName = ''
          } else {
            vm.$Message.error(res.errMsg)
          }
          this.batchRunSettingModalOptions.isLoadingForSubmit = false
        })
      }
    }
  }
</script>
