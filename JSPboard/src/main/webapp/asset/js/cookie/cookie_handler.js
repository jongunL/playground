/** 이름, 값, 만료기간 입력하면 쿠키를 생성 */
var setCookie = function(name, value, exp) {
    var date = new Date();
    date.setTime(date.getTime() + exp*24*60*60*1000);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};
/** 입력된 이름에 해당하는 쿠키값 반환 */
var getCookie = function(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
};
/** 입력된 이름에 해당하는 쿠키 삭제 */
var deleteCookie = function(name) {
     document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
};