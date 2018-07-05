import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

// const methods = {
//
// }

const state = {
  testReportList: []
}

const getters = {
  testReportList: state => {
    return state.testReportList
  }
}

const actions = {
  action_getReportList: (context, callback) => {
    api.getReportList().then(res => {
      if (res.success) {
        // context.state.testReportList = res.result.map(r => {
        //   return {
        //     name: r
        //   }
        // })
        context.state.testReportList = res.result
      }
      callback()
    })
  }
}

const mutations = {
  // foo: (state, payload) => {
  //   // do something
  // }
}

export default {
  namespaced: true,
  state,
  actions,
  getters,
  mutations
}
