/**
 * Module Verify-Img v1.0
 * 图形验证码的封装，主要用于刷新
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
		log('Module VerifyImg is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}

	var VerifyImg = function(selector){
		log('VerifyImg is initializing...');

		this.version = '0.0.1';
		this.selector = selector || '.verifyimg-box';
		if(typeof this.selector === 'string'){
			this.container = $(this.selector);
		}
		if(this.container.length === 0){
			warn('Can not find', this.selector);
			return this;
		}

		this.input = this.container.find('input');
		this.img = this.container.find('img');

		this._init();
		log('VerifyImg is initialized.');
		return this;
	};

	VerifyImg.prototype = {
		_init: function(){
			var self = this;
			this.img.click(function(){
				self.update();
			});
			return self;
		},

		update: function(){
			var url = this.img.attr('src').split('?')[0];
			url += '?' + Math.random();
			this.img.attr('src', url);
			// log('图片验证码已经刷新');

			return this;
		}
	};

	module.exports = VerifyImg;
	log('Module VerifyImg is loaded.');
});