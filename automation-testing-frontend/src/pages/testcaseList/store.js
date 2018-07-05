import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

const methods = {
  getTypeDescription: type => {
    switch (type) {
      case 1:
        return 'RestfulApiTest'
      case 2:
        return 'AjaxApiTest'
      case 3:
        return 'WebGuiTest'
      default:
        return 'RestfulApiTest'
    }
  }
}

const state = {
  testcaseList: [],
  environmentList: []
}

const getters = {
  testcaseList: state => {
    return state.testcaseList
  },
  method_getTypeDescription: state => {
    return methods.getTypeDescription
  },
  environmentList: state => {
    return state.environmentList
  }
}

const actions = {
  action_loadTestcaseList: (context) => {
    return new Promise((resolve, reject) => {
      api.getTestCaseList().then(res => {
        if (res.success) {
          context.state.testcaseList = res.result
        }
        resolve(res)
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
    })
  },
  action_getEnvironmentList: (context) => {
    return new Promise((resolve, reject) => {
      api.getEnvironmentList().then(res => {
        if (res.success) {
          context.state.environmentList = res.result
          resolve()
        }
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
    })
  },
  action_runTestSuite: (context, testsuite) => {
    return new Promise((resolve, reject) => {
      api.runTestSuite(testsuite).then(res => {
        if (res.success) {
          resolve(res)
        } else {
          reject(res)
        }
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
    })
  },
  action_getTestSuiteExecutionResult: (context) => {
    return new Promise((resolve, reject) => {
      api.getTestSuiteExecutionResult().then(res => {
        resolve(res)
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
    })
  }
  // action_saveTestSuite: (context, testsuite) => {
  //   return new Promise((resolve, reject) => {
  //     api.saveTestSuite(testsuite).then(res => {
  //       resolve(res)
  //     }).catch(error => {
  //       context.rootState.ajaxErrorHandler(error)
  //     })
  //   })
  // }
}

const mutations = {
  mutation_updateCurrentTestcase: (state, testcase) => {
    state.testcase = testcase
  }
}

export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
