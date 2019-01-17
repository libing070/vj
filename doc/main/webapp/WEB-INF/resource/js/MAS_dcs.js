/**
 * Created by baoqy on 2017/11/30.
 */
(function (_window, _document, _navigator, _location) {
  if (_window.MAS && _window.MAS.hasLoaded) {
    return;
  }

  var MAS = {
    qryParams: "",
    hasLoaded: false, //是否加载过
    IE: "", //IE版本

    addEventListener: _window['addEventListener'] ?
      function (el, eType, fn) {
        el.addEventListener && el.addEventListener(eType, fn, false);
      } : //or if IE use attachEvent
      function (el, eType, fn) {
        el.attachEvent && el.attachEvent("on" + eType, fn, false);
      },
    //判断是否为 Function 类型
    isFn: function (what) {
      return Object.prototype.toString.call(what) === "[object Function]";
    },
    init: function () {
      //获取 IE 版本
      var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
      if (re.exec(_navigator.userAgent) != null)
        MAS.IE = parseFloat(RegExp.$1);
      if (_location.search) {
        MAS.qryParams = MAS.getQryParams(_location.search);
      }
      MAS.addEventListener(_window, 'load', function (e) {
        MAS.hasLoaded = true;
      });
    },
    //从请求路径中获取参数串
    getQryParams: function (query) {
      var resultStr = "";
      var keyValuePairs = query.split(/[&?]/g);
      try {
        for (var i = 0; i < keyValuePairs.length; ++i) {
          var keyValuePair = keyValuePairs[i].match(/^([^=]+)(?:=([\s\S]*))?/);
          if (keyValuePair && keyValuePair[1]) {
            var key = "";
            try {
              key = decodeURIComponent(keyValuePair[1]);
            } catch (e) {
              try {
                key = unescape(keyValuePair[1]);
              } catch (e) {
                key = keyValuePair[1];
              }
            }
            var value = "";
            try {
              value = decodeURIComponent(keyValuePair[2]);
            } catch (e) {
              try {
                value = unescape(keyValuePair[2]);
              } catch (e) {
                value = keyValuePair[2];
              }
            }
            resultStr += "&" + key + "=" + value
          }
        }
      } catch (e) {}
      return resultStr;
    },
    getAppMsg: function () {
      return "appCodeName=" + _navigator.appCodeName + "|" + //浏览器代码名
        "appName=" + _navigator.appName + "|" + //浏览器名称
        "cookieEnabled=" + _navigator.cookieEnabled + "|" + //是否开启cookie
        "platform=" + _navigator.platform + "|" + //操作系统平台
        "userAgent=" + _navigator.userAgent; //User-agent头部值

    },
    getQueryVal: function GetQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var params = window.location.search.substr(1).match(reg);
      if (params != null) return unescape(params[2]);
      return null;
    }
  };
  MAS.dcs = function () {
    this.images = this['images'] = [];
    this.config = this['config'] = {};
    this.plat = this['plat'] = ""; //渠道
    this.mateFuncId = this['mateFuncId'] = ""; //资源ID
    this.dcsDelay = 25; //延迟时间
    this.appPath = ""; //请求路径
    this.domain = "10.255.219.201"; //包含端口和域名的地址
    return this;
  };
  MAS.dcs.prototype = {
    init: function (config) {
      //从mate标签中获取渠道以及应用id
      this.title = _document.title ? _document.title.replace(/[|]/g, "-") : "";
      this.plat = _document.getElementsByTagName('meta')['plat'].content;
      this.funcId = MAS.getQueryVal("funcId");
      this.staffId = MAS.getQueryVal("staffId");
      this.config = config;
      this.msg = "";

      //初始化未进行传参配置的默认值
      var sd = function (prop, defaultValue) {
        return config.hasOwnProperty(prop) ? config[prop] : defaultValue
      };
      this.dcsDelay = sd("dcsDelay", 25);
      this.appPath = sd("appPath", "");
      this.domain = sd("domain", "10.255.219.201");
    },
    dcsCreateImage: function (dcsSrc, obj) {
      if (_document.images) {
        var img = new Image();
        this.images.push(img);
        if (arguments.length === 2 && obj && MAS.isFn(obj['callback'])) {
          var hasFinished = false;
          var callback = obj['callback'];
          var o_out = obj;
          var dcs = this;
          img.onload = function () {
            if (!hasFinished) {
              hasFinished = true;
              callback(dcs, o_out);
              return true;
            }
          };
          _window.setTimeout(function () {
            if (!hasFinished) {
              hasFinished = true;
              callback(dcs, o_out);
              return true;
            }
          }, MAS.dcsDelay);
        }
        img.src = dcsSrc;
      }
    },
    // 基本码
    sentBaseCode: function () {
      //拼接传输参数
      var imageSrc = "http" + (_location.protocol.indexOf('https:') > -1 ? 's' : '') + "://" + this.domain + (this.appPath == "" ? '' : '/' + this.appPath) + "/dcsb?";
      var appMsg = MAS.getAppMsg().replace(/\s/g, "").replace(/[|]/g, ";");
      var imgUrl = imageSrc + "appMsg=" + appMsg + "&title=" + escape(encodeURIComponent(this.title)) + "&plat=" + this.plat + "&funcId=" + this.funcId + "&staffId=" + this.staffId + "&v=" + Math.random().toString(36).substr(2);
      //请求图片发送参数
      this.dcsCreateImage(imgUrl, {
        callBack: function (_this, param) {

        }
      })
    },
    // 事件码
    addEventCode: function (code) {
      //拼接传输参数
      var imageSrc = "http" + (_location.protocol.indexOf('https:') > -1 ? 's' : '') + "://" + this.domain + (this.appPath == "" ? '' : '/' + this.appPath) + "/dcse?";
      var appMsg = MAS.getAppMsg().replace(/\s/g, "").replace(/[|]/g, ";");
      var imgUrl = imageSrc + "appMsg=" + appMsg + "&msg=" + escape(encodeURIComponent(this.msg)) + "&title=" + escape(encodeURIComponent(this.title)) + "&plat=" + this.plat + "&funcId=" + this.funcId + "&eventCode=" + code + "&staffId=" + this.staffId + "&v=" + Math.random().toString(36).substr(2);
      //请求图片发送参数
      this.dcsCreateImage(imgUrl, {
        callBack: function (_this, param) {
          this.msg = ""; //重新将详细信息置为空
        }
      })
      this.msg = ""; //重新将详细信息置为空
    },
    //设置详细信息（拼接到操作后面）
    setMsg: function (msg) {
      this.msg = msg;
    },
    //获取cookie
    getCookie: function (name) {
      var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
      if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
      else
        return null;
    }
  };
  _window['MAS'] = MAS;
  MAS.init()
})(window, window.document, window.navigator, window.location);
var dcs = new MAS.dcs();
dcs.init({
  domain: "10.248.12.24:8080",
  dcsDelay: 100,
  appPath: "/biTrack"
});
dcs.sentBaseCode();