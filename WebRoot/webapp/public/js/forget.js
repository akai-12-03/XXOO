/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("forget",["form","verify-img","verify-tel"],function(e){var r=e("form");new r("#forget-form",{isBlurValidate:!0,rules:[]});var t=e("verify-img");new t;var f=e("verify-tel");new f(void 0,{url:ROOT_URL+"/sendcode.html",telphone:"#telphone",type:"forget"})});