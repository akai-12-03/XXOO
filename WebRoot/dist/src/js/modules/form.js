/**
 * Module Form v1.0
 * 表单校验的封装
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * @copyright 2015 Jerry
 */
define(function(require, exports, module){
	"use strict";

	var utils = require('utils');
	var validator;

	var $ = require('jQuery');
	var _ = require('log');
	var log = _.log;
	var error = _.error;
	var warn = _.warn;

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Form is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}

	var defaultOptions = {
		isBlurValidate: false,
		isKeyUpValidate: false,
		usernameRules: {
			min: 6,
			max: 16
		},
		passwordRules: {
			min: 6,
			max: 16,
			hasBlank: false,
			onlyNumber: true,
			onlyLetter: true
		}
	};
	var Form = function(selector, option){
		log('form is initializing...');
		this.selector = selector || '.form';

		if(typeof this.selector === 'string'){
			this.form = $(this.selector).first();
		}
		else{
			return this;
		}

		if(this.form.length === 0){
			warn('Can not find Form, please initialize Form by yourself.','[' + this.selector + ']');
			return this;
		}

		var ret = this._init(option);
		log('form is initialized.');
		return ret;
	};

	Form.prototype = {
		_init: function(option){
			var validatorMaker = require('validator');

			var $this = this;
			var $form = this.form;

			this.options = $.extend(true, {}, defaultOptions , option);
			validator = validatorMaker(this.options);
			// log(validator)
			this.rules = this.options.rules || [];

			this.inputs = $form.find('input:not(disabled):not([type=hidden]):not([type=checkbox]):not([type=radio])');

			// log('inputs:', this.inputs)
			if(this.options.isBlurValidate){
				this.inputs.blur(function(event){
					var input = $(this);
					// log('blur:', input);
					$this._validate(input);
				});
			}


			this.inputs.focus(function(){
				$this.clearNotice($(this));
			});
			
			$form.submit(function(event){
				var mustchecked=$("input[data-validator='mustchecked']");
				if (mustchecked.length>0) {
					
					if(!mustchecked.prop("checked")){
						event.preventDefault();
						alert("请先阅读注册协议！")
					}
					
				};
				for(var i = 0, len = $this.inputs.length;i < len;i++){
					var input = $this.inputs.eq(i);

					if(!$this._validate(input)){
						event.preventDefault();
						return false;
					}
				}
			});

			return this;
		},
		_getNoticeContainer: function($el){
			// console.log($el);
			if($el.find('~.tip').length > 0){
				return $el.find('~.tip').first();
			}
			else if($el.parent().find('~.tip').length > 0){
				return $el.parent().find('~.tip').first();
			}
			else{
				return $();
			}
		},
		notice: function(msg, $el){
			var $noticeContainer = this._getNoticeContainer($el);
			if($noticeContainer.length > 0){
				$noticeContainer.text(msg);
			}
			else{
				warn($el, 'has no notice container.');
			}
		},
		clearNotice: function($el){
			var $noticeContainer = this._getNoticeContainer($el);
			if($noticeContainer.length > 0){
				$noticeContainer.text('');
			}
			else{
				warn($el, 'has no notice container.');
			}
		},
		_validate: function($input){
			// log($input);
			var $this = this;
			var inputName = $input.attr('name');

			if(typeof inputName === 'undefined'){
				warn($input, 'has no attribute name.');
			}

			var validatorType = $input.data('validator');

			if(typeof validatorType === 'undefined'){
				warn($input, 'has no validator type.');
			}

			var rules = $this.rules;
			var fnArray = [];
			var i = 0;
			var len;
			var result;

			for(len = rules.length;i<len;i++){
				var rule = rules[i];
				if(rule.name === inputName){
					fnArray.push(rule.validator);
					break;
				}
			}

			var retStatus = true;

			if(validatorType in validator){
				if(validatorType === 'repassword'){
					result = validator[validatorType]($input.val(), validator, $input, $('input[data-validator=password]'));
				}
				else{
					result = validator[validatorType]($input.val(), validator, $input);
				}
				// log('default: ', result);
				$this.notice(result.msg, $input);
				if(!(result.status)){
					retStatus = false;
				}
			}
			else{
				warn($input, 'has no default validator.');
			}
			for(i = 0, len = fnArray.length;i < len; i++){
				var fn = fnArray[i];
				result = fn($input.val(), validator, $input);

				// log('extension: ', result);
				$this.notice(result.msg, $input);
				if(!(result.status)){
					retStatus = false;
					break;
				}
			}

			return retStatus;
		},
		submit: function(callback){
			this.form.submit(callback);
			return this;
		}
	};

	module.exports = Form;
	log('Module Form is loaded.');
});