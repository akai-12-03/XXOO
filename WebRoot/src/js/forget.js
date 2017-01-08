"use strict";
define("forget", function(require) {
	var Form = require("form");
	new Form('#forget-form', {
		isBlurValidate: true,
		'rules': [{
			'name': 'password'
		}]
	});
	var VerifyImg = require("verify-img");
	new VerifyImg();
	var VerifyTel = require("verify-tel");
	new VerifyTel(undefined, {
		url: ROOT_URL + '/sendcode.html',
		'telphone': '#telphone',
		'type': 'forget'
	});
	var closewin=require('modules/closewin');
	new closewin();
	$(function ($) {
		//弹出登录
		$("#agree").click(function () {
			$("body").append("<div id='mask'></div>");
			$("#mask").addClass("mask").fadeIn("slow");
			$("#tipBox").fadeIn("slow");
		});
		//
		
		//关闭
		$(".close_btn").click(function () { 
			$("#tipBox").fadeOut("fast");
			$("#mask").css({ display: 'none' });
		});
	});
})