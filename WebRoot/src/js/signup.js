"use strict";
define("signup", function(require) {	
	var $ = require('jQuery');
	var VerifyTel = require("verify-tel");
	new VerifyTel(undefined, {
			url: ROOT_URL + '/sendcode.html',
			'telphone': '#telphone'
		});
	var VerifyImg = require("verify-img");
	new VerifyImg();
	var Form = require("form");
    new Form('#signup-form', {
			isBlurValidate: true,
			'rules': [
		]
		});
	var closewin=require('modules/closewin');
	new closewin();
	$(function ($) {
		//弹出登录
		$("#agreement").hover(function () {
			$(this).stop().animate({
				opacity: '1'
			}, 600);
		}, function () {
			$(this).stop().animate({
				opacity: '0.9'
			}, 1000);
		}).on('click', function () {
			$("body").append("<div id='mask'></div>");
			$("#mask").addClass("mask").fadeIn("slow");
			$("#LoginBox1").fadeIn("slow");
		});
		//
		
		//关闭
		$(".close_btn").hover(function () { $(this).css({ color: 'black' }) }, function () { $(this).css({ color: '#999' }) }).on('click', function () {
			$("#LoginBox1").fadeOut("fast");
			$("#mask").css({ display: 'none' });
		});
	});
	$(function ($) {
		//弹出注册成功框
		$("#success").hover(function () {
			$(this).stop().animate({
				opacity: '1'
			}, 600);
		}, function () {
			$(this).stop().animate({
				opacity: '0.9'
			}, 1000);
		}).on('click', function () {
			$("body").append("<div id='mask'></div>");
			$("#mask").addClass("mask").fadeIn("slow");
			$("#successBox").fadeIn("slow");
		});
		//
		
		//关闭
		$(".close_btn").hover(function () { $(this).css({ color: 'black' }) }, function () { $(this).css({ color: '#999' }) }).on('click', function () {
			$("#successBox").fadeOut("fast");
			$("#mask").css({ display: 'none' });
		});
	});
})