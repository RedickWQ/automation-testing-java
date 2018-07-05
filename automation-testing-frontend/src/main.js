// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './stores/store'

import iView from 'iview'
// import 'iview/dist/styles/iview.css'

import './theme/index.less'

import locale from 'iview/dist/locale/en-US'
Vue.config.productionTip = false

Vue.use(iView, { locale })

router.beforeEach((to, from, next) => {
  // console.log('to:')
  // console.log(to)
  if (to.name) {
    document.title = to.name
  }
  iView.LoadingBar.start()
  next()
})

router.afterEach(route => {
  iView.LoadingBar.finish()
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
