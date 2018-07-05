import http from '../../lib/http'
http
export default {
  getReportList: (id) => {
    return http.get('/api/report/list')
  }
}
