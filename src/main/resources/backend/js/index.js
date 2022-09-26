/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
  return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数
function requestUrlParam(argname){
  var url = location.href
    // 从？后面进行截取，按照&进行分割，分成不同的参数，根据前面的id=进行查询，目的是把id拿出来。
  var arrStr = url.substring(url.indexOf("?")+1).split("&")
  for(var i =0;i<arrStr.length;i++)
  {
      var loc = arrStr[i].indexOf(argname+"=")
      if(loc!=-1){
          return arrStr[i].replace(argname+"=","").replace("?","")
      }
  }
  return ""
}
