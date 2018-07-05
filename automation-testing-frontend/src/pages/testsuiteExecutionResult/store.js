import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

// const methods = {
//
// }

const state = {
  testsuite: undefined
}

const getters = {
  testsuite: state => {
    return state.testsuite
  },
  runningCaseCount: state => {
    if (!state.testsuite) {
      return 0
    }
    var runningList = state.testsuite.testcaseRuntimeInstanceList.filter(tr => {
      return tr.status === 'Running'
    })
    return (runningList || []).length
  },
  passedCaseCount: state => {
    if (!state.testsuite) {
      return 0
    }
    var passedList = state.testsuite.testcaseRuntimeInstanceList.filter(tr => {
      return tr.status === 'Passed'
    })
    return (passedList || []).length
  },
  failedCaseCount: state => {
    if (!state.testsuite) {
      return 0
    }
    var failedList = state.testsuite.testcaseRuntimeInstanceList.filter(tr => {
      return tr.status === 'Failed'
    })
    return (failedList || []).length
  },
  caseRunMode: state => {
    if (!state.testsuite) {
      return ''
    }
    return state.testsuite.isCaseParallel ? 'Parallel' : 'Sequential'
  },
  iterationRunMode: state => {
    if (!state.testsuite) {
      return ''
    }
    return state.testsuite.isIterationParallel ? 'Parallel' : 'Sequential'
  }
}
const actions = {
  action_getTestSuiteExecutionResult: (context, callback) => {
    api.getTestSuiteExecutionResult().then(res => {
      if (res.success) {
        context.state.testsuite = res.result
        console.log(res.result)
        if (context.state.testsuite.isRunning) {
          setTimeout(function () {
            context.dispatch('action_getTestSuiteExecutionResult')
          }, 500)
        }
      } else {
        callback(res)
      }
    }).catch(error => {
      context.rootState.ajaxErrorHandler(error)
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
