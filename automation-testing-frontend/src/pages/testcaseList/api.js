import http from '../../lib/http'
http
export default {
  getTestCaseList: () => {
    return http.get('/api/testcase/list')
  },
  runTestSuite: (testsuite) => {
    return http.post('/api/testsuite/run', testsuite)
  },
  getTestSuiteExecutionResult: () => {
    return http.get('/api/testsuite/getExecutionResult')
  },
  getEnvironmentList: () => {
    return http.get('/api/environment/list')
  }
  // saveTestSuite: (testsuite) => {
  //   return http.post('/api/testsuite/save', testsuite)
  // }
}
