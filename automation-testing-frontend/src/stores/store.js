import Vue from 'vue'
import Vuex from 'vuex'
import http from '../lib/http'

import testcaseList from '../pages/testcaseList/store.js'
import testcaseDetail from '../pages/testcaseDetail/store.js'
import testsuiteExecutionResult from '../pages/testsuiteExecutionResult/store.js'
import testReportList from '../pages/testReportList/store.js'
import testSuiteList from '../pages/testSuiteList/store.js'
import common from '../pages/common/store.js'

Vue.use(Vuex)

const globalState = {
  ajaxErrorHandler: undefined   // 具体值由主页面传递过来
}

const globalMutations = {
  setAjaxErrorHandler: (state, func) => {
    state.ajaxErrorHandler = func
    http.onError = func
  }
}

const store = new Vuex.Store({
  modules: {
    common,
    testcaseList,
    testcaseDetail,
    testsuiteExecutionResult,
    testReportList,
    testSuiteList
  },
  state: globalState,
  mutations: globalMutations
})

export default store
