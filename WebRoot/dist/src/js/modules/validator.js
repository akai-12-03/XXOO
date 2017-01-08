/**
 * Module Validator v1.0
 * 表单校验器的封装
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * @copyright 2015 Jerry
 */
define(function(require, exports, module){
	"use strict";
	
	var $ = require('jQuery');
	var utils = require('utils');
	var _ = require('log');
	var log = _.log;
	var error = _.error;
	var warn = _.warn;

	var lang = require('i18n/{locate}/user');

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Validator is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}

	// var regLenMin = /^.+{0,5}$/;		//长度不足6位
	// var regLenMax = /^.+{17,}$/;		//长度超过15位
	var regHasBlank = /\s/;				//密码中包含空格
	var regOnlyNumber = /^\d+$/;		//密码为纯数字
	var regOnlyLetter = /^[a-zA-Z]+$/;	//密码为纯字母
	var regStrong = /^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$/;
	var regMedium = /^(?=.{7,})(?:(?:(?=.*[a-z])(?=.*[A-Z]))|(?:(?=.*[a-z])(?=.*[0-9]))|(?:(?=.*[a-z])(?=.*[^a-zA-Z0-9]))|(?:(?=.*[A-Z])(?=.*[0-9]))|(?:(?=.*[A-Z])(?=.*[^a-zA-Z0-9]))|(?:(?=.*[0-9])(?=.*[^a-zA-Z0-9]))).*$/;
	var regEnough = /^(?=.{6,}).*$/;

	var setReturnJson = utils.setReturnJson;

	var Validator = {
		username: function(text, validator, $input){
			var usernameRules = this.usernameRules;
			var lengthCheckResult = utils.inRange(text, usernameRules.min, usernameRules.max);
			if(!lengthCheckResult.status){
				return lengthCheckResult;
			}
			return setReturnJson(true);
		},
		password: function(text, validator, $input){
			var passwordRules = this.passwordRules;
			var pw = utils.trim(text);
			var lengthCheckResult = utils.inRange(pw, passwordRules.min, passwordRules.max);
			if(!lengthCheckResult.status){
				return lengthCheckResult;
			}

			if(passwordRules.hasBlank){
				if(regHasBlank.test(pw)){
					return setReturnJson(false, '密码不能含有空格');
				}
			}
			if(passwordRules.onlyNumber){
				if(regOnlyNumber.test(pw)){
					return setReturnJson(false, '密码不能为纯数字');
				}
			}
			if(passwordRules.onlyLetter){
				if(regOnlyLetter.test(pw)){
					return setReturnJson(false, '密码不能为纯字母');
				}
			}
			return setReturnJson(true);
		},
		repassword: function(text, validator, $input, $passwordInput){
			var passwordRules = this.passwordRules;
			var pwa = utils.trim(text);
			var pw = $passwordInput.val().trim();

			if($passwordInput.length === 0){
				warn('Can not find password input');
				return setReturnJson(false, '');
			}

			var resultOfValidatePassword = this.password($passwordInput.val(), this, $passwordInput);
			if(!resultOfValidatePassword.status){
				log(resultOfValidatePassword);
				return setReturnJson(false, '');
			}

			var lengthCheckResult = utils.inRange(pwa, passwordRules.min, passwordRules.max);
			if(!lengthCheckResult.status){
				return lengthCheckResult;
			}

			/*if(passwordRules.hasBlank){
				if(!regHasBlank.test(pwa)){
					return setReturnJson(false, '密码不能含有空格');
				}
			}
			if(passwordRules.onlyNumber){
				if(!regOnlyNumber.test(pwa)){
					return setReturnJson(false, '密码不能为纯数字');
				}
			}
			if(passwordRules.onlyLetter){
				if(!regOnlyLetter.test(pwa)){
					return setReturnJson(false, '密码不能为纯字母');
				}
			}*/

			if(pw !== pwa){
				return setReturnJson(false, '两次密码不一致');
			}
			return setReturnJson(true);
		},
		telphone: function(text, validator, $input){
			text = text.trim();
			if(utils.isEmpty(text)){
				return setReturnJson(false, "手机号不能为空");
			}
			var result1 = utils.inRange(text, 11, 11);
			var result2 = /\d{11}/.test(text);

			if(!result1.status || !result2){
				return setReturnJson(false, "手机号格式不正确");
			}

			var ret = utils.checkMobile(text);

			return setReturnJson(true);
		},
		referee: function(text, validator, $input){
			text = text.trim();
			var result1 = utils.inRange(text, 11, 11);
			var result2 = /\d{11}/.test(text);
			if(!utils.isEmpty(text)){
				if(!result1.status || !result2){
				return setReturnJson(false, "手机号格式不正确");
			}
		}
		var ret = utils.checkMobile(text);

			return setReturnJson(true);
		},
		code: function(text, validator, $input){
			text = text.trim();
			if(utils.isEmpty(text)){
				return setReturnJson(false,"验证码不为空");
			}
			return setReturnJson(true);
		},
		isEnough:function(text){
            text = text.trim();
	        var reg = /^(?=.{6,}).*$/;
	        return reg.test(text);
		},
		isMedium:function(text){
			text = text.trim();
	        var reg = /^(?=.{7,})(?:(?:(?=.*[a-z])(?=.*[A-Z]))|(?:(?=.*[a-z])(?=.*[0-9]))|(?:(?=.*[a-z])(?=.*[^a-zA-Z0-9]))|(?:(?=.*[A-Z])(?=.*[0-9]))|(?:(?=.*[A-Z])(?=.*[^a-zA-Z0-9]))|(?:(?=.*[0-9])(?=.*[^a-zA-Z0-9]))).*$/;
	        return reg.test(text);
		},
		isStrong:function(text){
			text = text.trim();
	        var reg = /^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$/;
	        return reg.test(text);
		},
		init: function(option){
			var defaultOption = {
				usernameRules: {
					min: 6,
					max: 16
				},
				passwordRules: {
					min: 6,
					max: 15,
					hasBlank: true,
					onlyNumber: true,
					onlyLetter: true
				}
			};
			option = $.extend({}, option, defaultOption);
			this.usernameRules = $.extend({}, option.usernameRules);
			this.passwordRules = $.extend({}, option.passwordRules);
			// this.telphoneRules = $.extend({}, option.telphoneRules);

			// delete this.init;
			return this;
		}
	};

	module.exports = function(option){
		return Validator.init(option);
	};
	log('Module Validator is loaded.');
});