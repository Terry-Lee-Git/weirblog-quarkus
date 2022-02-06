var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?b9d1eefb48ae8ca6fe3e1266694102c2";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

function getCurrentDate() {
    var myDate = new Date();
    var year = myDate.getFullYear(); //年
    var month = myDate.getMonth() + 1; //月
    var day = myDate.getDate(); //日
    var days = myDate.getDay();
    switch(days) {
          case 1:
                days = '星期一';
                break;
          case 2:
                days = '星期二';
                break;
          case 3:
                days = '星期三';
                break;
          case 4:
                days = '星期四';
                break;
          case 5:
                days = '星期五';
                break;
          case 6:
                days = '星期六';
                break;
          case 0:
                days = '星期日';
                break;
    }
    var str = year + "年" + month + "月" + day + "日  " + days;
    return str;
}
function getCurrentYear() {
    var myDate = new Date();
    var year = myDate.getFullYear(); //年
    return year;
}
$(document).ready(function(){
	// console.log("------------------------------------------------22222")
	  $("#now_date").html(getCurrentDate);
	  $("#shuoming_next").html(getCurrentYear);
	});
	
	
