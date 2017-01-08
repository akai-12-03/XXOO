/**
 * Module Utils v0.0.1
 * some util functions and polyfill
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * Copyright 2015
 */
define(function(require, exports, module){
	var lang = require('i18n/{locate}/user');

	var _ = require('log');
	var log = _.log;
	//polyfill
	/**
	 * 获取字符串的长度(1个汉字算作2个字符)
	 */
	String.prototype.getLength = function(str){
		var len = 0;
		for(var i=0;i<this.length;i++){
			if(this.charCodeAt(i)>=0 && this.charCodeAt(i)<=128){
				len++;
			}
			else{
				len += 2;
			}
		}
		return len;
	};
	//trim
	if(!String.prototype.hasOwnProperty('trim')){
		String.prototype.trim = function(){
			return this.replace(/^\s+|\s+$/g, '');
		};
	}
	var isChinaMobile = /^134[0-8]\d{7}$|^(?:13[5-9]|147|15[0-27-9]|178|18[2-478])\d{8}$/; //Add 178 移动方面最新答复
	var isChinaUnion  = /^(?:13[0-2]|145|15[56]|176|18[56])\d{8}$/;
	var isChinaTelcom = /^(?:133|153|177|18[019])\d{8}$/; //^1349\d{7}$ 1349号段 电信方面没给出答复，视作不存在
	var isOtherTelphone	  = /^170([059])\d{7}$/;

	module.exports = {
		trim: function(text){
			if(String.prototype.hasOwnProperty('trim')){
				/*jshint eqnull:true*/
				return text == null ? '' : (text + '').trim();
			}
			else{
				return text == null ? '' : (text + '').replace(/^\s+|\s+$/g, '');
			}
		},
		isEmpty: function(text){
			if(text === undefined || text === null){
				return true;
			}
			if(typeof text === 'string' && this.trim(text).length === 0){
				return true;
			}
			else{
				return false;
			}
		},
		inRange: function(text, min, max){
			var len = text.getLength();
			if(len==0){
				return this.setReturnJson(false, '不能为空');
			}
			else if(len < min){
				return this.setReturnJson(false, '长度不足' + min + '位');
			}
			else if(len > max){
				return this.setReturnJson(false, '长度超过了' + max + '位');
			}
			else{
				return this.setReturnJson(true);
			}
		},
		checkMobile: function(telphone){
			telphone = this.trim(telphone);
			if(telphone.length !== 11){
				return this.setReturnJson(false, '未检测到正确的手机号码');
			}
			else{
				if(isChinaMobile.test(telphone)){
					return this.setReturnJson(true, '移动', {name: 'ChinaMobile'});
				}
				else if(isChinaUnion.test(telphone)){
					return this.setReturnJson(true, '联通', {name: 'ChinaUnion'});
				}
				else if(isChinaTelcom.test(telphone)){
					return this.setReturnJson(true, '电信', {name: 'ChinaTelcom'});
				}
				else if(isOtherTelphone.test(telphone)){
					var num = isOtherTelphone.exec(telphone);
					return this.setReturnJson(true, '', {name: ''});
				}
				else{
					return this.setReturnJson(false, '未检测到正确的手机号码');
				}
			}
		},
		/**
		 * set return object
		 * @param: status string
		 */
		setReturnJson: function(status, msg, data){
			if(typeof status !== 'boolean' && typeof status !== 'number'){
				status = false;
			}
			if(typeof msg !== 'string'){
				msg = '';
			}
			return {
				'status': status,
				'msg': msg,
				'data': data
			};
		}
	};
});