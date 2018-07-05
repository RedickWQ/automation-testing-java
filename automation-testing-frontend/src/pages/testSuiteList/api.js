import http from '../../lib/http'
http
export default {
  loadTestSuiteList: () => {
    return http.get('/api/testsuite/list')
  },
  deleteTestSuite: (id) => {
    return http.post('/api/testsuite/delete?id=' + id)
  },
  saveTestSuite: (testsuite) => {
    return http.post('/api/testsuite/save', testsuite)
  },
  searchTestcase: (keyword) => {
    return http.get('/api/testcase/search?keyword=' + keyword)
  },
  runTestSuite: (testsuite) => {
    return http.post('/api/testsuite/run', testsuite)
  },
  getTestSuiteExecutionResult: () => {
    return http.get('/api/testsuite/getExecutionResult')
  }
}
