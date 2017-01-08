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
//			{
//			'oldpassword':'password',
//			validator: function(text, validator, input){
//                  // console.log('password: %s', text);
//                  var status = Validator.password(text);
//                  var sr = status.result;
//                  var xtip = input.siblings('.xtip');
//                  var status2 = validator.isInRange(text, 8, 16);
//                  // var sr2 = status2.result;
//                  if(!sr){
//                      xtip.text(status.msg);
//                  }
//                  else if(text.length === 0){
//                      status.msg = '请输入密码';
//                      status.result = false;
//                      xtip.text(status.msg);
//                  }
//                  else if(text.match(/^.{16,}$/)){
//                      status.msg = '密码需8-15位';
//                      status.result = false;
//                      xtip.text(status.msg);
//                  }
//                  else if(text.match(/^.{1,5}$/)){
//                      status.msg = '密码需8-15位';
//                      status.result = false;
//                      xtip.text(status.msg);
//                  }
//                  else if(text.match(/\s/)){
//                      status.msg = '密码不能包含空格';
//                      status.result = false;
//                      xtip.text(status.msg);
//                  }
//                  else if(text.match(/^\d+$/)){   
//                      status.msg = '密码不能为纯数字';
//                      status.result = false;
//                      xtip.text(status.msg);
//                  }
//                  else if(text.match(/^[a-zA-Z]+$/)){ 
//                      status.msg = '密码不能为纯字母';
//                      status.result = false;
//                      xtip.text(status.msg);
//                  }
//                  else{
//                      xtip.text('');
//                  }
//                  return status;
//             }
//			},
//			{
//	                'onemorepassword': 'repassword',
//	                validator: function(text, validator, input){
//	                    // var status = Validator.password(text);
//	                    var pw = $('input[id=password]').val();
//	                    var status = Validator.password(pw);
//	                    var sr = status.result;
//	                    var status2 = validator.isTheSame(pw, text);
//	                    var xtip = input.siblings('.xtip');
//	                    if(!sr){
//	                        // xtip.text('两次密码不一致');
//	                    }
//	                    else if(sr && !status2){
//	                        xtip.text('两次密码不一致');
//	                    }
//	                    else{
//	                        xtip.text('');
//	                    }
//	                    return status;
//	                }
//             }
//			
		]
		});
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