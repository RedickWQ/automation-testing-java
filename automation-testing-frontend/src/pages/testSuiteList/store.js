import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

// const methods = {
//
// }

const state = {
  testSuiteList: []
}

const getters = {
  testSuiteList: state => {
    return state.testSuiteList
  }
}

const actions = {
  action_getTestSuiteList: (context) => {
    return new Promise((resolve, reject) => {
      api.loadTestSuiteList().then(res => {
        if (res.success) {
          context.state.testSuiteList = res.result
        }
        resolve(res)
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
    })
  },
  action_deleteTestSuite: (context, id) => {
    api.deleteTestSuite(id).then(res => {
      context.dispatch('action_getTestSuiteList').then(res => {
        if (res.success) {
          context.state.testSuiteList = res.result
        }
      })
    })
  },
  action_saveTestSuite: (context, testsuite) => {
    return new Promise((resolve, reject) => {
      api.saveTestSuite(testsuite).then(res => {
        resolve(res)
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
    })
  },
  action_searchTestcaseByKeyword: (context, keyword) => {
    return new Promise((resolve, reject) => {
      api.searchTestcase(keyword).then(res => {
        resolve(res)
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
}

const mutations = {
  // foo: (state, payload) => {
  //   // do something
  // }
}

export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
