import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

// const methods = {
//
// }

const state = {
  environmentList: []
}

const getters = {
  environmentList: state => {
    return state.environmentList
  }
}

const actions = {
  action_getEnvironmentList: (context) => {
    return new Promise((resolve, reject) => {
      api.getEnvironmentList().then(res => {
        if (res.success) {
          context.state.environmentList = res.result
          resolve()
        }
      }).catch(error => {
        context.rootState.ajaxErrorHandler(error)
      })
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
