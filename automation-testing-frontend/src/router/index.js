import Vue from 'vue'
import Router from 'vue-router'
// import Hello from '@/components/Hello'

import testcaseListPage from '../pages/testcaseList/app'
import testcaseDetailPage from '../pages/testcaseDetail/app'
import testsuiteExecutionResultPage from '../pages/testsuiteExecutionResult/app'
import testReportListPage from '../pages/testReportList/app'
import testSuiteListPage from '../pages/testSuiteList/app'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: testcaseListPage
    },
    {
      path: '/testcaseList',
      name: 'Testcase List',
      component: testcaseListPage
    },
    {
      path: '/testcaseDetail/:componentName/entryPoint/:entryPoint',
      name: 'Testcase Detail',
      component: testcaseDetailPage
    },
    {
      path: '/testsuiteExecutionResult',
      name: 'Test Suite Execution Result',
      component: testsuiteExecutionResultPage
    },
    {
      path: '/testReportList',
      name: 'Test Report List',
      component: testReportListPage
    },
    {
      path: '/testSuiteList',
      name: 'Test Suite List',
      component: testSuiteListPage
    }
  ]
})
