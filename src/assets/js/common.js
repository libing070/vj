export const BASE={
  getBrowserInfo:function (){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    var isIE = userAgent.indexOf("compatible") > -1
      && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    var isEdge = userAgent.indexOf("Edge") > -1; //判断是否IE的Edge浏览器
    var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
    var isSafari = userAgent.indexOf("Safari") > -1
      && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器
    var isChrome = userAgent.indexOf("Chrome") > -1
      && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器

    if (isIE) {
      var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
      reIE.test(userAgent);
      var fIEVersion = parseFloat(RegExp["$1"]);
      if (fIEVersion == 7) {
        return "IE7  "+"["+userAgent+"]";
      } else if (fIEVersion == 8) {
        return "IE8  "+"["+userAgent+"]";
      } else if (fIEVersion == 9) {
        return "IE9  "+"["+userAgent+"]";
      } else if (fIEVersion == 10) {
        return "IE10  "+"["+userAgent+"]";
      } else if (fIEVersion == 11) {
        return "IE11  "+"["+userAgent+"]";
      } else {
        return "0  "+"["+userAgent+"]";
      }//IE版本过低
      return "IE  "+"["+userAgent+"]";
    }
    if (isOpera) {
      return "Opera  "+"["+userAgent+"]";
    }
    if (isEdge) {
      return "Edge  "+"["+userAgent+"]";
    }
    if (isFF) {
      return "Firefox  "+"["+userAgent+"]";
    }
    if (isSafari) {
      return "Safari  "+"["+userAgent+"]";
    }
    if (isChrome) {
      return "Chrome  "+"["+userAgent+"]";
    }
  }
}
