import http from '../../lib/http'
http
export default {
  getTestSuiteExecutionResult: () => {
    return http.get('/api/testsuite/getExecutionResult')
  }
}
