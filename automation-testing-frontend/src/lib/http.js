import axios from 'axios'
axios.defaults.withCredentials = true

export default {
  get: function (url) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'GET',
        url: url
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((error) => {
        this.onError(error)
        // reject(error)
      })
    })
  },
  post: function (url, data) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'POST',
        url: url,
        data: data
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((error) => {
        this.onError(error)
        // reject(error)
      })
    })
  },
  formPost: function (url, data) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'POST',
        url: url,
        data: data,
        headers: {
          'X-Requested-With': 'XMLHttpRequest'
        }
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((error) => {
        reject(error)
      })
    })
  },
  onError: undefined
}
