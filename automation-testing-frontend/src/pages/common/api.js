import http from '../../lib/http'
http
export default {
  getEnvironmentList: () => {
    return http.get('/api/environment/list')
  }
}
