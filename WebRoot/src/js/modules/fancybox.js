/**
 * Module Fancybox v1.1
 * 一个异步加载fancybox的简单封装
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * @copyright 2015 Jerry
 */
define(function(require, exports, module){
	"use strict";

	var $ = require('jQuery');
	var _ = require('log');
	var log = _.log;
	var error = _.error;

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Fancybox is loading...');
	}
	else{
		error('Can not find jQuery.');
		return false;
	}

	var linkTpl = '<link rel="stylesheet" href="{$link}">';

	log('fancybox css is loading...');
	for(var i = 0, len = seajs.data.alias.fancyboxCss.length; i < len; i++){
		var link = seajs.data.alias.fancyboxCss[i];
		$('head').append(linkTpl.replace('{$link}', link));
	}

	log('fancybox-mousewheel is loading...');
	require('fancybox-mousewheel');
	log('fancybox is loading...');
	require('fancybox');
	log('fancybox-buttons is loading...');
	require('fancybox-buttons');

	module.exports = {};
	log('Module Fancybox is loaded.');
});