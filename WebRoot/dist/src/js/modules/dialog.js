/**
 * Module Dialog v2.0
 * a simple dialog module
 * you can set type, title, content and width
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
		log('Module Dialog is loading...');
	}
	else{
		error('Can not find jQuery.');
		return false;
	}

	var defaultOptions = {
		'type': 'text',
		'title': 'This is jui dialog title',
		'content': 'This is jui dialog body.',
		'href': '/',
		'width': 500,
	};
	var dialogMask = '<div class="jui-dialog-mask"></div>';
	var jDialog = 
		'<div class="jui-dialog">'+
			'<div class="jui-dialog-hd">'+
				'<div class="jui-dialog-title"></div>'+
				'<a class="jui-dialog-close-btn">×</a>'+
			'</div>'+
			'<div class="jui-dialog-bd"></div>'+
		'</div>';


	module.exports = function(options){
		log('dialog is initializing...');
		var $this = this;

		var option = $.extend({}, defaultOptions, options);

		if($('.jui-dialog-mask').length === 0){
			$('body').append(dialogMask);
		}
		if($('.jui-dialog').length === 0){
			$('body').append(jDialog);
		}

		var mask = $('.jui-dialog-mask');
		var dialog = $('.jui-dialog');
		var closeBtn = $('.jui-dialog-close-btn');

		$('.jui-dialog-title').text(option.title);

		switch(option.type){
			case 'text':{
				$('.jui-dialog-bd').text(option.content);
				break;				
			}
			case 'html':{
				$('.jui-dialog-bd').html(option.content);
				break;
			}
			case 'ajax':{
				$.ajax({
					'url': option.href,
					'async': false,
					'type': 'get',
					'dataType': 'html',
					success: function(res){
						$('.jui-dialog-bd').html(res);
					},
					error: function(res){
						error(message);
					}
				});
				break;
			}
			default:{
				break;
			}
		}

		$('.jui-dialog-bd').css('width', option.width);

		var dh = dialog.height();
		var dw = dialog.width();
		var _h = $(window).height();
		var _w = $(window).width();


		dialog.css({
			'top': (_h - dh)/2,
			'left': (_w - dw)/2,
		});

		var resize = null;
		var sid = 0;
		var rid = 0;
		$('.jui-dialog-mask,.jui-dialog').show(0, function(){
			$(window).on('scroll', function(){
				$('.jui-dialog').css({
					'top': ($(window).height() - $('.jui-dialog').height()) / 2 + window.scrollY,
					'left': ($(window).width() - $('.jui-dialog').width()) / 2,
				});
			});

			$(window).on('resize', function(){
				if(typeof resize === 'number' && resize >= 0){
					clearTimeout(resize);
				}
				resize = setTimeout(function(){
					var _h = $(window).height();
					var _w = $(window).width();

					var cssData = {
						'top': (_h - dh) / 2 + window.scrollY,
						'left': (_w - dw) / 2 + window.scrollX
					};

					if(_h < dh){
						var newdh = _h * 0.8;
						cssData = $.extend(true, cssData, {
							'height': newdh,
							'top': (_h - newdh) / 2 + window.scrollY,
						});
						cssData2 = $.extend(true, cssData2, {
							'height': newdh
						});
					}
					else{
						cssData = $.extend(true, cssData, {
							'height': dh,
							'top': (_h - dh) / 2 + window.scrollY,
						});
					}
					if(_w < dw - 20){
						var newdw = _w * 0.8;
						cssData = $.extend(true, cssData, {
							'width': newdw + 20,
							'left': (_w - newdw) / 2 + window.scrollX
						});
					}
					else{
						cssData = $.extend(true, cssData, {
							'width': dw + 20,
							'left': (_w - dw) / 2 + window.scrollX
						});
					}
					$('.jui-dialog').css(cssData);
					$('.jui-dialog-bd').css(cssData2);
				}, 500);
			});
		});
		$('.jui-dialog-mask,.jui-dialog-close-btn').one('click', function(){
			$('.jui-dialog-mask,.jui-dialog').hide(0, function(){
				$(window).off('scroll resize');
			});
		});

		log('dialog is initialized.');
	};

	log('Module Dialog is loaded.');
});