/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("myhome",["accordion","tab","verify-tel","verify-img","form"],function(e){var r=e("accordion");new r;var o=e("tab");new o;var i=e("verify-tel");new i(void 0,{url:ROOT_URL+"/sendcode.html",telphone:"#telphone"});var n=e("verify-img");new n;var a=e("form");new a("#common-form",{isBlurValidate:!0,rules:[]})});