import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

// const methods = {
//
// }

const state = {
  testcase: undefined,
  executionResult: undefined,
  environmentList: []
}

const getters = {
  testcase: state => {
    return state.testcase
  },
  resultList: state => {
    if (state.executionResult) {
      return state.executionResult.resultList
    } else {
      return []
    }
  },
  executionResult: state => {
    return state.executionResult
  },
  isCaseRunning: state => {
    var isRunning = false
    if (state.executionResult) {
      state.executionResult.iterationList.forEach(iteration => {
        if (iteration.status === 'Running') {
          isRunning = true
        }
      })
    }
    return isRunning
  },
  environmentList: state => {
    return state.environmentList
  }
}

const actions = {
  action_getEnvironmentList: (context) => {
    api.getEnvironmentList().then(res => {
      context.state.environmentList = res.result
    })
  },
  action_loadTestcaseDetail: (context, params) => {
    api.loadTestcaseDetail(params.componentName, params.entryPoint).then(res => {
      context.state.testcase = res.result
    })
  },
  action_runTestcase: (context, run) => {
    return new Promise((resolve, reject) => {
      api.runTestcase(run.testcase, run.isParallel, run.logLevel).then(res => {
        resolve(res)
      }).catch(error => reject(error))
    })
  },
  action_saveTestData: (context, testcase) => {
    return new Promise((resolve, reject) => {
      api.saveTestData(testcase).then(res => {
        resolve(res)
      }).catch(error => reject(error))
    })
  },
  action_getExecutionResult: (context, payload) => {
    api.getExecutionResult(payload.componentName, payload.entryPoint).then(res => {
      if (res.success) {
        context.state.executionResult = res.result
        // console.log('isRunning:')
        // console.log(context.getters.isCaseRunning)
        if (context.getters.isCaseRunning) {
          setTimeout(function () {
            context.dispatch('action_getExecutionResult', payload)
          }, 300)
        }
      } else {
        context.state.executionResult.iterationList.forEach(iteration => {
          if (iteration.status === 'Running') {
            iteration.status === 'Timeout'
          }
        })
      }
    })
  }
}

const mutations = {
  mutation_updateExecutionResult: (state, result) => {
    state.executionResult = result
  },
  mutation_updateTestcase: (state, testcase) => {
    state.testcase = testcase
  }
}

export default {
  state,
  actions,
  getters,
  mutations
}
