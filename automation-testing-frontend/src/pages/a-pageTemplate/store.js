import Vue from 'vue'
import Vuex from 'vuex'
import api from './api'
api
Vue.use(Vuex)

// const methods = {
//
// }

const state = {

}

const getters = {
  // foo: state => {
  //   return state.xxx
  // }
}

const actions = {
  // foo: (context, payload) => {
  //   api.xxxx().then(res => {
  //     payload.callback(res)
  //   })
  // }
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
