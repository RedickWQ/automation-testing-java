import http from '../../lib/http'

export default {
  loadTestcaseDetail: (componentName, entryPoint) => {
    return http.get('/api/testcase/detail?componentName=' + componentName + '&entryPoint=' + entryPoint)
  },
  runTestcase: (testcase, isParallel, logLevel) => {
    return http.post('/api/testcase/run?isParallel=' + isParallel + '&logLevel=' + logLevel, testcase)
  },
  getExecutionResult: (componentName, entryPoint) => {
    return http.get('/api/testcase/getExecutionResult?componentName=' + componentName + '&entryPoint=' + entryPoint)
  },
  getEnvironmentList: () => {
    return http.get('/api/environment/list')
  },
  saveTestData: (testcase) => {
    return http.post('/api/testcase/saveData', testcase)
  }
}
