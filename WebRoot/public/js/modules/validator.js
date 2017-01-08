/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/validator",["jQuery","utils","log","i18n/{locate}/user"],function(t,e,n){"use strict";var r=t("jQuery"),s=t("utils"),i=t("log"),a=i.log,u=i.error,o=i.warn;t("i18n/{locate}/user");if("undefined"==typeof r||"undefined"==typeof r.fn||"string"!=typeof r.fn.jquery)return void u("Can not find jQuery.");i.$(),a("Module Validator is loading...");var l=/\s/,d=/^\d+$/,m=/^[a-zA-Z]+$/,f=s.setReturnJson,c={username:function(t,e,n){var r=this.usernameRules,i=s.inRange(t,r.min,r.max);return i.status?f(!0):i},password:function(t,e,n){var r=this.passwordRules,i=s.trim(t),a=s.inRange(i,r.min,r.max);return a.status?r.hasBlank&&l.test(i)?f(!1,"密码不能含有空格"):r.onlyNumber&&d.test(i)?f(!1,"密码不能为纯数字"):r.onlyLetter&&m.test(i)?f(!1,"密码不能为纯字母"):f(!0):a},repassword:function(t,e,n,r){var i=this.passwordRules,u=s.trim(t),l=r.val().trim();if(0===r.length)return o("Can not find password input"),f(!1,"");var d=this.password(r.val(),this,r);if(!d.status)return a(d),f(!1,"");var m=s.inRange(u,i.min,i.max);return m.status?l!==u?f(!1,"两次密码不一致"):f(!0):m},telphone:function(t,e,n){if(t=t.trim(),s.isEmpty(t))return f(!1,"手机号不能为空");var r=s.inRange(t,11,11),i=/\d{11}/.test(t);if(!r.status||!i)return f(!1,"手机号格式不正确");s.checkMobile(t);return f(!0)},referee:function(t,e,n){t=t.trim();var r=s.inRange(t,11,11),i=/\d{11}/.test(t);if(!(s.isEmpty(t)||r.status&&i))return f(!1,"手机号格式不正确");s.checkMobile(t);return f(!0)},code:function(t,e,n){return t=t.trim(),s.isEmpty(t)?f(!1,"验证码不为空"):f(!0)},isEnough:function(t){t=t.trim();var e=/^(?=.{6,}).*$/;return e.test(t)},isMedium:function(t){t=t.trim();var e=/^(?=.{7,})(?:(?:(?=.*[a-z])(?=.*[A-Z]))|(?:(?=.*[a-z])(?=.*[0-9]))|(?:(?=.*[a-z])(?=.*[^a-zA-Z0-9]))|(?:(?=.*[A-Z])(?=.*[0-9]))|(?:(?=.*[A-Z])(?=.*[^a-zA-Z0-9]))|(?:(?=.*[0-9])(?=.*[^a-zA-Z0-9]))).*$/;return e.test(t)},isStrong:function(t){t=t.trim();var e=/^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$/;return e.test(t)},init:function(t){var e={usernameRules:{min:6,max:15},passwordRules:{min:6,max:15,hasBlank:!0,onlyNumber:!0,onlyLetter:!0}};return t=r.extend({},t,e),this.usernameRules=r.extend({},t.usernameRules),this.passwordRules=r.extend({},t.passwordRules),this}};n.exports=function(t){return c.init(t)},a("Module Validator is loaded.")});