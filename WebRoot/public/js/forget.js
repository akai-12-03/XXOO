/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("forget",["form","verify-img","verify-tel"],function(e){var i=e("form");new i("#forget-form",{isBlurValidate:!0,rules:[{name:"password"}]});var n=e("verify-img");new n;var o=e("verify-tel");new o(void 0,{url:ROOT_URL+"/sendcode.html",telphone:"#telphone",type:"forget"}),$(function(e){e("#agree").click(function(){e("body").append("<div id='mask'></div>"),e("#mask").addClass("mask").fadeIn("slow"),e("#tipBox").fadeIn("slow")}),e(".close_btn").click(function(){e("#tipBox").fadeOut("fast"),e("#mask").css({display:"none"})})})});