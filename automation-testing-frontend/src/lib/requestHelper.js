
export default {
  // 返回URL上的参数列表 包含name, value
  getParameters: function (url) {
    var search
    if (!url) {
      search = window.location.search.substr(1) // 去掉 ‘？’
    } else {
      if (url.indexOf('?') !== -1) {
        search = url.split('?')[1]
      } else {
        return []
      }
    }
    var parameterList = search.split('&')
      .map(item => {
        return {
          name: item.split('=')[0],
          value: item.split('=')[1]
        }
      }).filter(item => {
        return item.name !== ''
      })
    // console.log(parameterList)
    return parameterList
  },
  // 获取URL上整个参数部分的字符串，
  getParamStr: function (url) {
    if (!url) url = window.location.href
    var urlObj = new URL(url)
    let paramStr = new URLSearchParams(urlObj.search.slice(1)).toString()
    return paramStr
  },
  // 把当前页面url上的参数全部追加到一个没有参数的URL上
  getNewReqUrlWithParameters: function (url) {
    var paramStr = this.getParamStr()
    if (paramStr) {
      url += '?' + paramStr
    }
    return url
  },
  getParameterByName: function (name, url) {
    if (!url) url = window.location.href
    name = name.replace(/[[\]]/g, '\\$&')
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)')
    var results = regex.exec(url)
    if (!results) return null
    if (!results[2]) return ''
    return decodeURIComponent(results[2].replace(/\+/g, ' '))
  },
  // 把当前页面URL里的一个参数追加到新的URL上
  // paramValue 为可选参数，当没有值时，从当前页的参数列表中搜索
  appendReqParameter: function (url, paramName, paramValue) {
    var parameter
    if (!paramValue) {
      var parameterList = this.getParameters()
      parameter = parameterList.find(p => {
        return p.name === paramName
      })
    } else {
      parameter = {
        name: paramName,
        value: paramValue
      }
    }

    if (parameter) {
      if (this.isUrlHavingParameters(url)) {
        url += '&' + parameter.name + '=' + parameter.value
      } else {
        url += '?' + parameter.name + '=' + parameter.value
      }
    }
    return url
  },
  // 判断当前URL是否包含参数
  isUrlHavingParameters: function (url) {
    if (this.getParameters(url).length > 0) {
      return true
    } else {
      return false
    }
  },
  // URL上追加新的参数列表， 已有的参数被替换
  getNewReqUrlByParameterList: function (url, parameterList) {
    // console.log(parameterList)
    var urlMain
    if (url.indexOf('?') !== -1) {
      urlMain = url.split('?')[0]
    } else {
      urlMain = url
    }
    var newUrl = urlMain + '?'
    if (parameterList.length > 0) {
      parameterList.forEach(parameter => {
        newUrl += parameter.name + '=' + parameter.value + '&'
      })
      // 去除最后一个字符 ‘&’
      newUrl = newUrl.slice(0, -1)
    }
    // console.log('new url with parameter list')
    return newUrl
  },
  resetParameterValueInList: function (parameterList, paramName, paramValue) {
    // console.log('parameterList before update')
    // console.log(parameterList)
    var parameter = parameterList.find(p => {
      return p.name === paramName
    })
    if (parameter) {
      // console.log('paramName update')
      parameter.value = paramValue
    } else {
      parameter = {
        name: paramName,
        value: paramValue
      }
      parameterList.push(parameter)
    }
    // console.log('parameterList after update')
    // console.log(parameterList)
    return parameterList
  },
  getURLParameter (name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null
  }
}
