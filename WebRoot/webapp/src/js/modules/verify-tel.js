/**
 * Module Verify-Tel v1.0
 * 短信验证码的封装
 */
define(function(require, exports, module){
	"use strict";

	var $ = require('jQuery');
	var _ = require('log');
	var log = _.log;
	var error = _.error;
	var warn = _.warn;

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module VerifyTel is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}
	var Validator = require('validator')();

	var defaultOption = {
		'target': '#telphone',
		'username': '#username',
		'totalTime': 60,
		'type': 'normal',
		'url': ROOT_URL + '/sendvcode.html',
		'imgcode':'#imgCode'
	};
	var errorStatus = {
		'1': null,
		'2': '该手机已被注册',
		'3': '系统错误',
		'4': '用户不存在',
		'5': '手机号码已存在',
		'13': '该手机已被注册',
		'98':'图片验证码错误',
		'99':'一分钟内，验证码只能发送一次'
	};

	var VerifyTel = function(selector, option){
		log('VerifyTel is initializing...');

		this.version = '0.1.0';

		this.selector = selector || '.verifytel-box';
		if(typeof this.selector === 'string'){
			this.container = $(this.selector).first();
		}
		else{
			return this;
		}

		if(this.container.length === 0){
			return this;
		}

		this.input = this.container.find('input');
		this.btn = this.container.find('button');
		this.btn.prop('disabled', false);
		
		this.setConfig(option);

		this._init();
		log('VerifyTel is initialized.');
		return this;
	};

	VerifyTel.prototype = {
		_init: function(){
			var $this = this;

			$this.btn.removeProp('disabled');
			$this.btn.click(function(){
				$this.sendCode();
			});

			return $this;
		},

		setConfig: function(option){
			var $this = this;

			var dataOption = {
				'telphone': $this.container.data('target'),
				'username': $this.container.data('username'),
				'imgCode':  $this.container.data('imgcode')
			};
			this.params = $.extend(true, {}, defaultOption, option);
			this.params.url = option.url || '/sendvcode.html';

			this.params.done = option.done || function(res){
				var curSeconds = $this.params.totalTime;
				var id = window.setInterval(function(){
					var strs = curSeconds + '秒后可重发';
					$this.btn.text(strs);
					if(curSeconds < 0){
						window.clearInterval(id);
						$this.btn.text('重新发送');
						$this.btn.prop('disabled', false);
					}
					--curSeconds;
				}, 1000);
			};
			this.params.error = option.error || function(errorStatus, res){
				$this._alert(errorStatus[res]);
			};

			return $this;
		},
		_getNoticeContainer: function($el){
			if($el.find('.tip').length > 0){
				return $el.find('.tip').first();
			}
			else if($el.find('+.tip').length > 0){
				return $el.find('+.tip').first();
			}
			else if($el.parent().find('+.tip').length > 0){
				return $el.parent().find('+.tip').first();
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
				warn('vTel#130', $el, 'has no notice container.');
			}
		},

		_alert: function(msg){
			var tip = this._getNoticeContainer(this.container);
			if(tip.length > 0){
				tip.first().text(msg);
			}
			else{
				alert(msg);
				warn('vTel#142', 'Can not find notice container.', 'at input.verifytel-box');
			}
			return this;
		},

		sendCode: function(){
			if(this.ajaxReq){
				this.ajaxReq.abort();
			}

			var $this = this;

			var $telphoneInput = $($this.params.telphone);
			if($telphoneInput.length === 0){
				warn('Can not find telphone input.');
				return false;
			}

			var telNumber = $.trim($telphoneInput.val());
			// if(!!!telNumber.length){
			// 	$this._alert('请先填写手机号');
			// 	$($this.params.telphone);
			// 	return $this;
			// }

            var $imgcode=$($this.params.imgcode);
            //console.log($imgcode)
            var imgcode=$.trim($imgcode.val());
			var result = Validator.telphone(telNumber, Validator, $telphoneInput);
			var code=Validator.code(imgcode,Validator,$imgcode);
			// warn(result);
			if(!result.status){
				$this.notice(result.msg, $telphoneInput);
				return false;
			}
            if(!code.status&&$imgcode.length!=0){
            	$this.notice(code.msg, $imgcode);
				return false;
            }
			var data = {
				phone: telNumber,
				imgcode:imgcode
				//forgetname:telNumber
			};

			if('forget' === $this.params.type){
				data.forgetname = telNumber;
				data.cto = 'xxoo';
				var status2 = Validator.telphone(data.forgetname);
				if(!status2.status){
					$this._alert(status2.msg);
					return false;
				}
			}
			else if('loginname' === $this.params.type){
				data.loginname = $($this.params.loginname).val().trim();
			}
			else if('jyname' == $this.params.type){
				data.jyname = $($this.params.jyname).val().trim();
			}

			// 汉鼎项目中需要的部分
			if($('.verifyimg-box input').length > 0){
				var verifyCode = $.trim($('.verifyimg-box input').val());
				if(verifyCode.length !== 4){
					$('.verifyimg-box input').trigger('blur');
					return false;
				}
				// data.verify = verifyCode;
				data.imgcode = verifyCode;
			}
			else{
				alert('当前页面已经被非法纂改，请刷新后重试');
				return false;
			}

			this.ajaxReq = $.ajax({
				url: $this.params.url + '?t=' + Math.random(),
				data: data,
				dataType: 'json',
				success: function(res){
					var result = res;
					if(1 === result){
						$this.btn.prop('disabled', true);
						$this.params.done(res);
					}
					else{
						$this.params.error(errorStatus, res);						
					}
				},
				error: function(){
					alert('短信发送失败，可能是网络原因');
				}
			});
		}
	};

	module.exports = VerifyTel;
	log('Module VerifyTel is loaded.');
});