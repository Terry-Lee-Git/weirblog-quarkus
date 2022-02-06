/**
 * 初始化  serializeObject
 */
function initSerializeObject() {
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
}
/**
 * 清空表单
 * @param id
 * @author weir
 */
function fromReset(id) {
	$("#" + id).find(':input').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
}
/**
 * 扩展Date的format方法 
 */
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	}
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
/**  
 *转换日期对象为日期字符串  
 * @param date 日期对象  
 * @param isFull 是否为完整的日期数据,  
 *               为true时, 格式如"2000-03-05 01:05:04"  
 *               为false时, 格式如 "2000-03-05"  
 * @return 符合要求的日期字符串  
 */
function getSmpFormatDate(date, isFull) {
	var pattern = "";
	if (isFull == true || isFull == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	} else {
		pattern = "yyyy-MM-dd";
	}
	return getFormatDate(date, pattern);
}
/**  
 *转换当前日期对象为日期字符串  
 * @param date 日期对象  
 * @param isFull 是否为完整的日期数据,  
 *               为true时, 格式如"2000-03-05 01:05:04"  
 *               为false时, 格式如 "2000-03-05"  
 * @return 符合要求的日期字符串  
 */

function getSmpFormatNowDate(isFull) {
	return getSmpFormatDate(new Date(), isFull);
}
/**  
 *转换long值为日期字符串  
 * @param l long值  
 * @param isFull 是否为完整的日期数据,  
 *               为true时, 格式如"2000-03-05 01:05:04"  
 *               为false时, 格式如 "2000-03-05"  
 * @return 符合要求的日期字符串  
 */

function getSmpFormatDateByLong(l, isFull) {
	return getSmpFormatDate(new Date(l), isFull);
}
/**  
 *转换long值为日期字符串  
 * @param l long值  
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss  
 * @return 符合要求的日期字符串  
 */

function getFormatDateByLong(l, pattern) {
	return getFormatDate(new Date(l), pattern);
}
/**  
 *转换日期对象为日期字符串  
 * @param l long值  
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss  
 * @return 符合要求的日期字符串  
 */
function getFormatDate(date, pattern) {
	if (date == undefined) {
		date = new Date();
	}
	if (pattern == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	}
	return date.format(pattern);
}

Date.prototype.Format = function(formatStr)   
{   
    var str = formatStr;   
    var Week = ['日','一','二','三','四','五','六'];  
  
    str=str.replace(/yyyy|YYYY/,this.getFullYear());   
    str=str.replace(/yy|YY/,(this.getYear() % 100)>9?(this.getYear() % 100).toString():'0' + (this.getYear() % 100));   
  
    str=str.replace(/MM/,this.getMonth()>9?this.getMonth().toString():'0' + this.getMonth());   
    str=str.replace(/M/g,this.getMonth());   
  
    str=str.replace(/w|W/g,Week[this.getDay()]);   
  
    str=str.replace(/dd|DD/,this.getDate()>9?this.getDate().toString():'0' + this.getDate());   
    str=str.replace(/d|D/g,this.getDate());   
  
    str=str.replace(/hh|HH/,this.getHours()>9?this.getHours().toString():'0' + this.getHours());   
    str=str.replace(/h|H/g,this.getHours());   
    str=str.replace(/mm/,this.getMinutes()>9?this.getMinutes().toString():'0' + this.getMinutes());   
    str=str.replace(/m/g,this.getMinutes());   
  
    str=str.replace(/ss|SS/,this.getSeconds()>9?this.getSeconds().toString():'0' + this.getSeconds());   
    str=str.replace(/s|S/g,this.getSeconds());   
  
    return str;   
}