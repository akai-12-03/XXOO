/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("myhome",["form","verify-tel","verify-img"],function(e){var r=e("form");new r("#jypwd-form",{isBlurValidate:!0,rules:[]});var i=e("verify-tel");new i(void 0,{url:ROOT_URL+"/sendcode.html",telphone:"#telphone",type:"loginname"});var n=e("verify-img");new n});