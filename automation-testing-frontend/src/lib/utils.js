import $ from 'jQuery'
import deepEqual from 'deep-equal'
import dateformat from 'dateformat'
export default {
  getGUID: function () {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
      var r = Math.random() * 16 | 0
      var v = c === 'x' ? r : (r & 0x3 | 0x8)
      return v.toString(16)
    })
  },
  deepCloneObject: function (obj) {
    return $.extend(true, {}, obj)
  },
  isObjectIdentical: function (sourceObj, targetObj) {
    var result = deepEqual(sourceObj, targetObj, {strict: false})
    // console.log('compare obj result:' + result)
    return result
  },
  formatDateFromMilliseconds: function (milliSecond, expression) {
    if (milliSecond) {
      if (expression) {
        return dateformat(milliSecond, expression)
      } else {
        return dateformat(milliSecond, 'yyyy-mm-dd')
      }
    } else {
      return ''
    }
  },
  formatDurationFromMilliseconds: function (millisecond) {
    if (millisecond >= 1000) {
      var seconds = millisecond / 1000
      return Math.floor(seconds * 100) / 100 + ' s'
    } else {
      return millisecond + ' ms'
    }
  },
  scrollToForm: function (form, type) {
    var position = form.$el.offsetParent.offsetTop
    if (type === 'body') {
      $('html, body').animate({
        scrollTop: position + 'px'
      }, 'fast')
    } else if (type === 'modal') {
      console.log(position)
      $('.ivu-modal-body').animate({
        scrollTop: position + 'px'
      }, 400)
    }
  },
  scrollTop: function () {
    $(window).scrollTop(0)
  }
}
