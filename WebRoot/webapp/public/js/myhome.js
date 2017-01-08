/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("myhome",["form","verify-tel","verify-img"],function(e){var r=e("form");new r("#changepwd-form",{isBlurValidate:!0,rules:[]}),new r("#jypwd-form",{isBlurValidate:!0,rules:[]});var i=e("verify-tel");new i(void 0,{url:ROOT_URL+"sendcode.html",telphone:"#telphone",type:"jyname",jyname:"#telphone"});var n=e("verify-img");new n});