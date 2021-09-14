/**
 * 判断字符串是否为空
 * 	为空，返回true
 * 	不为空，返回false
 * @param str
 * @returns {Boolean}
 */
function isEmpty(str) {
	if (str == null || str.trim() == "") {
		return true;
	}
	return false;
}